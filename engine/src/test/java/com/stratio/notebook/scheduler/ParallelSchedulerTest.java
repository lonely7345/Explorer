package com.stratio.notebook.scheduler;


import junit.framework.TestCase;
public class ParallelSchedulerTest extends TestCase {

	private SchedulerFactory schedulerSvc;

	public void setUp() throws Exception{
		schedulerSvc = new SchedulerFactory();
	}
	
	public void tearDown(){
		
	}
	
	public void testRun() throws InterruptedException{
		Scheduler s = schedulerSvc.createOrGetParallelScheduler("test", 2);
		assertEquals(0, s.getJobsRunning().size());
		assertEquals(0, s.getJobsWaiting().size());
		
		Job job1 = new SleepingJob("job1", null, 500);
		Job job2 = new SleepingJob("job2", null, 500);
		Job job3 = new SleepingJob("job3", null, 500);		
		
		s.submit(job1);
		s.submit(job2);
		s.submit(job3);
		Thread.sleep(200);

		TestCase.assertEquals(Job.Status.RUNNING, job1.getStatus());
		TestCase.assertEquals(Job.Status.RUNNING, job2.getStatus());
		TestCase.assertEquals(Job.Status.PENDING, job3.getStatus());
		assertEquals(2, s.getJobsRunning().size());
		assertEquals(1, s.getJobsWaiting().size());
		
		Thread.sleep(500);
		
		TestCase.assertEquals(Job.Status.FINISHED, job1.getStatus());
		TestCase.assertEquals(Job.Status.FINISHED, job2.getStatus());
		TestCase.assertEquals(Job.Status.RUNNING, job3.getStatus());
		assertEquals(1, s.getJobsRunning().size());
		assertEquals(0, s.getJobsWaiting().size());

	}

}
