package com.ipayafrica.elipapower.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;


public class JobScheduler {
    protected final transient Log log = LogFactory.getLog(getClass());

	JobScheduler() {
		
	}
	@Value("${cron.frequency.jobwithsimpletrigger}")
    private long frequency;

	public void execute(JobExecutionContext jobExecutionContext) {
		log.info("Running JobWithSimpleTrigger | frequency {}" + frequency);
	}
	
	@Bean(name = "jobWithSimpleTriggerBean")
    public JobDetailFactoryBean sampleJob() {
        return ConfigureQuartz.createJobDetail(this.getClass());
    }

    @Bean(name = "jobWithSimpleTriggerBeanTrigger")
    public SimpleTriggerFactoryBean sampleJobTrigger(@Qualifier("jobWithSimpleTriggerBean") JobDetail jobDetail) {
    	return ConfigureQuartz.createTrigger(jobDetail,frequency);
    }


}
