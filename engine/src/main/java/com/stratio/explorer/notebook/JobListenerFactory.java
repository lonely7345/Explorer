package com.stratio.explorer.notebook;

import com.stratio.explorer.scheduler.JobListener;

public interface JobListenerFactory {
	public JobListener getParagraphJobListener(Note note); 
}
