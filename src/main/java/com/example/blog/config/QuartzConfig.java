package com.example.blog.config;

import com.example.blog.quartz.ArticleScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzConfig {

    // FactoryBean可简化Bean的实例化过程:
    // 1.通过FactoryBean封装Bean的实例化过程.
    // 2.将FactoryBean装配到Spring容器里.
    // 3.将FactoryBean注入给其他的Bean.
    // 4.该Bean得到的是FactoryBean所管理的对象实例.

    // 配置JobDetail  @Bean表示程序一启动就会被初始化，因此就会执行调度任务
    // 刷新博客分数任务
    @Bean
    public JobDetailFactoryBean articleScoreRefreshJobDetail() {
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(ArticleScoreRefreshJob.class);
        factoryBean.setName("articleScoreRefreshJob");
        factoryBean.setGroup("blogJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);
        return factoryBean;
    }

    // 配置Trigger
    //设置每5分钟执行一次
    @Bean
    public SimpleTriggerFactoryBean articleScoreRefreshTrigger(JobDetail articleScoreRefreshJobDetail) {
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(articleScoreRefreshJobDetail);
        factoryBean.setName("articleScoreRefreshTrigger");
        factoryBean.setGroup("blogTriggerGroup");
        factoryBean.setRepeatInterval(1000 * 60 * 5);   //5分钟
        factoryBean.setJobDataMap(new JobDataMap());
        return factoryBean;
    }
}
