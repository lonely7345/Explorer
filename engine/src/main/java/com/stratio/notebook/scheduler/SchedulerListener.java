package com.stratio.notebook.scheduler;

public interface SchedulerListener {
	public void jobStarted(Scheduler scheduler, Job job);
	public void jobFinished(Scheduler scheduler, Job job);
}
