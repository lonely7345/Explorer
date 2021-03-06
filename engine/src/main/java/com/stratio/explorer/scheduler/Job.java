/**
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.explorer.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.stratio.explorer.interpreter.AsyncInterpreterResult;
import com.stratio.explorer.interpreter.ResultHandler;

/**
 * Skeletal implementation of the Job concept:
 * - designed for inheritance
 * - should be run on a separate thread
 * - maintains internal state: it's status
 * - supports listeners who are updated on status change
 * <p/>
 * <p/>
 * Job class is serialized/deserialized and used server<->client commnunication and saving/loading jobs from disk.
 * Changing/adding/deleting non transitive field name need consideration of that.
 */
public abstract class Job {
    /**
     * Job status
     * <p/>
     * READY - Job is not running, ready to run.
     * PENDING - Job is submitted to scheduler. but not running yet
     * RUNNING - Job is running.
     * STREAMING,
     * FINISHED - Job finished run. with success
     * ERROR - Job finished run. with error
     * ABORT - Job finished by abort
     */
    public static enum Status {
        READY,
        PENDING,
        RUNNING,
        REFRESH_RESULT,
        FINISHED,
        ERROR,
        ABORT,;

        boolean isReady() {
            return this == READY;
        }

        boolean isRunning() {
            return this == RUNNING;
        }

        boolean isRefreshing() {
            return this == REFRESH_RESULT;
        }

        boolean isPending() {
            return this == PENDING;
        }
    }

    private String jobName;
    String id;

    Object result;

    Date dateCreated;
    Date dateStarted;
    Date dateFinished;
    Status status;
    transient boolean aborted = false;

    String errorMessage;

    transient private Throwable exception;
    transient private JobListener listener;
    private long progressUpdateIntervalMs;

    public Job(String jobName, JobListener listener) {
        this(jobName, listener, JobProgressPoller.DEFAULT_INTERVAL_MSEC);
    }

    public Job(String jobName, JobListener listener, long progressUpdateIntervalMs) {
        this.jobName = jobName;
        this.listener = listener;
        this.progressUpdateIntervalMs = progressUpdateIntervalMs;

        dateCreated = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        id = dateFormat.format(dateCreated) + "_" + super.hashCode();

        setStatus(Status.READY);
    }

    public String getId() {
        return id;
    }

    public int hashCode() {
        return id.hashCode();
    }

    public boolean equals(Object o) {
        return ((Job) o).hashCode() == hashCode();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if (this.status == status) {
            return;
        }
        Status before = this.status;
        Status after = status;
        if (listener != null) {
            listener.beforeStatusChange(this, before, after);
        }
        this.status = status;
        if (listener != null) {
            listener.afterStatusChange(this, before, after);
        }
    }

    public void setListener(JobListener listener) {
        this.listener = listener;
    }

    public JobListener getListener() {
        return listener;
    }

    public boolean isTerminated() {
        return !this.status.isReady() && !this.status.isRunning() && !this.status.isPending() && !this.status
                .isRefreshing();
    }

    public boolean isRunning() {
        return this.status.isRunning();
    }

    public boolean isRefreshing() {
        return this.status.isRefreshing();
    }

    public void run() {

        if (aborted) {
            setStatus(Status.ABORT);
            aborted = false;
            return;
        }
        JobProgressPoller progressUpdator = null;
        try {

            setStatus(Status.RUNNING);
            progressUpdator = new JobProgressPoller(this, progressUpdateIntervalMs);
            progressUpdator.start();
            dateStarted = new Date();
            Object runResult = jobRun();
            if (AsyncInterpreterResult.class.isInstance(runResult)) {
                AsyncInterpreterResult async = AsyncInterpreterResult.class.cast(runResult);
                ResultHandler handler = async.getResultHandler();
                while ((handler.isLastResult() == null || !handler.isLastResult()) && !aborted) {
                    Thread.sleep(500);
                    if (status == Status.ABORT) {
                        aborted=true;
                    }
                }
            } else {
                result = runResult;
            }
            dateFinished = new Date();
            progressUpdator.terminate();
            if (aborted) {
                setStatus(Status.ABORT);
            } else {
                setStatus(Status.FINISHED);
            }
        } catch (InterruptedException e) {
            logger().error("Job failed", e);
            progressUpdator.terminate();
            this.exception = e;
            result = e.getMessage();
            errorMessage = getStack(e);
            dateFinished = new Date();
            setStatus(Status.ERROR);
        } finally {
            aborted = false;
        }
    }

    public String getStack(Throwable e) {
        StackTraceElement[] stacks = e.getStackTrace();
        if (stacks == null) {
            return "";
        }
        String ss = "";
        for (StackTraceElement s : stacks) {
            ss += s.toString() + "\n";
        }

        return ss;
    }

    public Throwable getException() {
        return exception;
    }

    protected void setException(Throwable t) {
        exception = t;
    }

    public void setReturn(Object result) { // STREAMING MODE
        this.result = result;
    }

    public Object getReturn() {
        return result;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public abstract int progress();

    public abstract Map<String, Object> info();

    protected abstract Object jobRun();

    protected abstract boolean jobAbort();

    public void abort() {
        aborted = jobAbort();
    }

    public void resetResult(){
        this.status=Status.READY;
        this.result=null;
        this.dateStarted = null;
        this.dateFinished = null;
    }

    public boolean isAborted() {
        return aborted;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateStarted() {
        return dateStarted;
    }

    public Date getDateFinished() {
        return dateFinished;
    }

    private Logger logger() {
        return LoggerFactory.getLogger(Job.class);
    }
}
