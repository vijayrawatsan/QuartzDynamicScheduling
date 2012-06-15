package in.xebia.jobs;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import in.xebia.domain.JobData;
import in.xebia.repository.JobRepository;
import in.xebia.services.MailService;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PersistentJobSchedulerJob {
	
	private static Logger logger = Logger.getLogger("PersistentJobSchedulerJob");
	
	@Autowired
	private JobRepository jobRepository;
	
	@Autowired
	private MailService mailService;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Scheduled(fixedRate=30000)
	public void schedulePersistentJobs(){
		List<JobData> jobsData= jobRepository.findAll();
		logger.info("Retriving Jobs from Database and Scheduling One by One | Total Number of Jobs: "+jobsData.size());
		try{
			Scheduler scheduler = new StdSchedulerFactory().getScheduler(); 
	        scheduler.start();  
			for(JobData jobData: jobsData){
				JobDetail job = newJob(MailSenderJob.class)
								.withIdentity(jobData.getJobName())
								.usingJobData(getJobDataMap(jobData))
								.build();
				if(!jobData.getActive()){
					logger.info("Deleting a Job");
					if(scheduler.checkExists(new JobKey(jobData.getJobName())))
						scheduler.deleteJob(new JobKey(jobData.getJobName()));
					continue;
				}
				if(scheduler.checkExists(new JobKey(jobData.getJobName()))){
					logger.info("Rescheduling the Job");
					Trigger oldTrigger = scheduler.getTrigger(new TriggerKey(jobData.getJobName()+"Trigger"));
					TriggerBuilder tb = oldTrigger.getTriggerBuilder();
					Trigger newTrigger = tb.withSchedule(simpleSchedule()
							  .withIntervalInMilliseconds(jobData.getRepeatInterval()).
							  repeatForever())
							  .build();

					scheduler.rescheduleJob(oldTrigger.getKey(), newTrigger);
				}else{
					logger.info("Scheduling the Job");
					scheduler.scheduleJob(job,getTrigger(jobData));
				}
			}
		}catch (SchedulerException e) {
			logger.error("Scheduler Exception : "+e.getMessage());	
		}
	}

	private JobDataMap getJobDataMap(JobData jobData) {
		JobDataMap jobDataMap =  new JobDataMap();
		jobDataMap.put("recipients", jobData.getRecipients());
		jobDataMap.put("mailService", mailService);
		return jobDataMap;
	}
	
	private Trigger getTrigger(JobData jobData){
		SimpleTrigger simpleTrigger = newTrigger().withIdentity(jobData.getJobName()+"Trigger")
										.startAt(jobData.getStartDateTime())
									 	.withSchedule(simpleSchedule()
										  .withIntervalInMilliseconds(jobData.getRepeatInterval()).
										  repeatForever())
										  .build();
		return simpleTrigger;
	}
}
