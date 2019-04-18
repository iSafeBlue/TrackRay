package com.trackray.web.config;

import com.trackray.base.quartz.SpringJobFactory;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author 浅蓝
 * @email blue@ixsec.org
 * @since 2019/2/23 19:06
 */
@Configuration
public class QuartzConfig {

/*
    @Autowired
    private SpringJobFactory springJobFactory;
*/

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext)
    {
        SpringJobFactory jobFactory = new SpringJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }


    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(jobFactory);
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler(JobFactory jobFactory) {
        return schedulerFactoryBean(jobFactory).getScheduler();
    }



}