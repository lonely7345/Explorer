/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.stratio.explorer.scheduler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class FIFOScheduler implements Scheduler{
	List<Job> queue = new LinkedList<Job>();
	private ExecutorService executor;
	private SchedulerListener listener;
	boolean terminate=false;
	Job runningJob = null;
	private String name;

	public FIFOScheduler(String name, ExecutorService executor, SchedulerListener listener){
		this.name = name;
		this.executor = executor;
		this.listener = listener;
	}
	
	public String getName(){
		return name;
	}
	public Collection<Job> getJobsWaiting(){
		List<Job> ret = new LinkedList<Job>();
		synchronized(queue){
			for(Job job : queue){
				ret.add(job);
			}
		}
		return ret;
	}
	
	public Collection<Job> getJobsRunning(){
		List<Job> ret = new LinkedList<Job>();
		Job job = runningJob;
		
		if(job!=null)
			ret.add(job);
			
		return ret;
	}
	
	
	
	public void submit(Job job){
		job.setStatus(Job.Status.PENDING);
		synchronized(queue){
			queue.add(job);
			queue.notify();
		}
	}
	
	public void run(){

		synchronized(queue){		
			while(terminate==false){			
				if(runningJob!=null || queue.isEmpty()==true){
					try {
						queue.wait(500);
					} catch (InterruptedException e) {
					}
					continue;
				}

				runningJob = queue.remove(0);
					
				final Scheduler scheduler = this;
				this.executor.execute(new Runnable(){
					public void run() {
						if(listener!=null) listener.jobStarted(scheduler, runningJob);
						runningJob.run();
						if(listener!=null) listener.jobFinished(scheduler, runningJob);
						runningJob = null;
						synchronized(queue){
							queue.notify();
						}
					}					
				});
			}
				

		}
	}
	
	public void stop(){
		terminate = true;
		synchronized(queue){
			queue.notify();
		}
	}
	
}