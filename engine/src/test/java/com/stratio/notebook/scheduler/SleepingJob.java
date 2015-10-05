/*
*Licensed to STRATIO (C) under one or more contributor license agreements.
*See the NOTICE file distributed with this work for additional information
*regarding copyright ownership.  The STRATIO (C) licenses this file
*to you under the Apache License, Version 2.0 (the
*"License"); you may not use this file except in compliance
*with the License.  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package com.stratio.notebook.scheduler;

import java.util.HashMap;
import java.util.Map;

public class SleepingJob extends Job{
	
	private int time;
	boolean abort = false;
	private long start;
	private int count;
	
	
	public SleepingJob(String jobName, JobListener listener, int time){
		super(jobName, listener);
		this.time = time;
		count = 0;
	}
	public Object jobRun() {
		start = System.currentTimeMillis();
		while(abort==false){
			count++;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			if(System.currentTimeMillis() - start>time) break;
		}
		return System.currentTimeMillis()-start;
	}

	public boolean jobAbort() {
		abort = true;
		return true;
	}

	public int progress() {
		long p = (System.currentTimeMillis() - start)*100 / time;
		if(p<0) p = 0;
		if(p>100) p = 100;
		return (int) p;
	}

	public Map<String, Object> info() {
		Map<String, Object> i = new HashMap<String, Object>();
		i.put("LoopCount", Integer.toString(count));
		return i;
	}


}
