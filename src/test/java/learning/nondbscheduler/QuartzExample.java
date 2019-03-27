package learning.nondbscheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzExample {

    public static void main(String args[]) {

        SchedulerFactory schedFact = new StdSchedulerFactory();
        try {
//            List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
//            loggers.add(LogManager.getRootLogger());
//            for ( Logger logger : loggers ) {
//                logger.setLevel(Level.OFF);
//            }
            Scheduler sched = schedFact.getScheduler();

            JobDetail job = JobBuilder.newJob(SimpleJob.class)
                    .withIdentity("myJob", "group1")
                    .usingJobData("jobSays", "Hello World!")
                    .usingJobData("myFloatValue", 3.141f)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("myTrigger", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(8).repeatForever())
                    .build();

            JobDetail jobA = JobBuilder.newJob(JobA.class)
                    .withIdentity("jobA", "group2")
                    .build();

            JobDetail jobB = JobBuilder.newJob(JobB.class)
                    .withIdentity("jobB", "group2")
                    .build();

            Trigger triggerA = TriggerBuilder.newTrigger()
                    .withIdentity("triggerA", "group2")
                    .startNow()
                    .withPriority(15)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).repeatForever())
                    .build();

            Trigger triggerB = TriggerBuilder.newTrigger()
                    .withIdentity("triggerB", "group2")
                    .startNow()
                    .withPriority(10)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(5).repeatForever())
                    .build();

            sched.scheduleJob(job, trigger);
            sched.scheduleJob(jobA, triggerA);
            sched.scheduleJob(jobB, triggerB);
            sched.start();

        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
