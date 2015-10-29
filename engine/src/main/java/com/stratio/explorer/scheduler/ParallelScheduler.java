/**
 * Copyright (C) 2013 Stratio (http://stratio.com)
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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class ParallelScheduler implements Scheduler{
	List<Job> queue = new LinkedList<Job>();
	List<Job> running = new LinkedList<Job>();
	private ExecutorService executor;
	private SchedulerListener listener;
	boolean terminate=false;
	private String name;
	private int maxConcurrency;

	public ParallelScheduler(String name, ExecutorService executor, SchedulerListener listener, int maxConcurrency){
		this.name = name;
		this.executor = executor;
		this.listener = listener;
		this.maxConcurrency = maxConcurrency;
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
		synchronized(queue){
			for(Job job : running){
				ret.add(job);
			}
		}
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
				if(running.size()>=maxConcurrency || queue.isEmpty()==true){
					try {
						queue.wait(500);
					} catch (InterruptedException e) {
					}
					continue;
				}

				Job job = queue.remove(0);				
				running.add(job);
				Scheduler scheduler = this;
				
				executor.execute(new JobRunner(scheduler, job));
			}
				

		}
	}
	
	public void setMaxConcurrency(int maxConcurrency){
		this.maxConcurrency = maxConcurrency;
		synchronized(queue){
			queue.notify();
		}
	}
	
	private class JobRunner implements Runnable{
		private Scheduler scheduler;
		private Job job;

		public JobRunner(Scheduler scheduler, Job job){
			this.scheduler = scheduler;
			this.job = job;
		}

		public void run() {
			if(listener!=null) listener.jobStarted(scheduler, job);
			job.run();
			if(listener!=null) listener.jobFinished(scheduler, job);
			synchronized(queue){
				running.remove(job);
				queue.notify();
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