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
package com.stratio.notebook.scheduler;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SchedulerFactory implements SchedulerListener{
	private final Logger logger = LoggerFactory.getLogger(SchedulerFactory.class);
	ScheduledExecutorService executor;
	Map<String, Scheduler> schedulers = new LinkedHashMap<String, Scheduler>();
	
	private static SchedulerFactory _singleton;
	private static Long singletonLock = new Long(0);
	public static SchedulerFactory singleton(){
		if (_singleton==null) {
			synchronized(singletonLock) {
				if (_singleton==null) {
					try {
						_singleton = new SchedulerFactory();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} 
		return _singleton;
	}
	
	public SchedulerFactory() throws Exception {
		executor = Executors.newScheduledThreadPool(100);		
	}
	
	public void destroy() {
		executor.shutdown();
	}

	public Scheduler createOrGetFIFOScheduler(String name) {
		synchronized(schedulers){
			if(schedulers.containsKey(name)==false){
				Scheduler s = new FIFOScheduler(name, executor, this);
				schedulers.put(name, s);
				executor.execute(s);
			}
			return schedulers.get(name);
		}
	}
	
	public Scheduler createOrGetParallelScheduler(String name, int maxConcurrency) {
		synchronized(schedulers){
			if(schedulers.containsKey(name)==false){
				Scheduler s = new ParallelScheduler(name, executor, this, maxConcurrency);
				schedulers.put(name, s);
				executor.execute(s);
			}
			return schedulers.get(name);
		}
	}

	public Scheduler removeScheduler(String name) {
		synchronized(schedulers){
			Scheduler s = schedulers.remove(name);
			if(s!=null){
				s.stop();
			}
		}
		return null;
	}

	public Collection<Scheduler> listScheduler(String name) {
		List<Scheduler> s = new LinkedList<Scheduler>();
		synchronized(schedulers){
			for(Scheduler ss :schedulers.values()){
				s.add(ss);
			}
		}
		return s;
	}

	public void jobStarted(Scheduler scheduler, Job job) {
		logger.info("Job "+job.getJobName()+" started by scheduler "+scheduler.getName());
		
	}

	public void jobFinished(Scheduler scheduler, Job job) {
		logger.info("Job "+job.getJobName()+" finished by scheduler "+scheduler.getName());
		
	}
	


}
