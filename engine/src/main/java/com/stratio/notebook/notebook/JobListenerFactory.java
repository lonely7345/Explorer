package com.stratio.notebook.notebook;

import com.stratio.notebook.scheduler.JobListener;

public interface JobListenerFactory {
	public JobListener getParagraphJobListener(Note note); 
}
