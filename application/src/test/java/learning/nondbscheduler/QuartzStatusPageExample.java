package learning.nondbscheduler;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

public class QuartzStatusPageExample {

    public static void main(String args[]) {
        SchedulerFactory schedFact = new StdSchedulerFactory();
        try {
            Scheduler sched = schedFact.getScheduler();

            JobKey jobKey = new JobKey("DatabaseProbe", "group3");
            JobDetail databaseProbeJob = JobBuilder.newJob(DatabaseProbe.class)
                    .withIdentity(jobKey)
                    .build();

            TriggerKey trigerKey = new TriggerKey("databaseProbeTrigger", "group3");
            Trigger databaseProbeTrigger =  TriggerBuilder.newTrigger()
                    .withIdentity(trigerKey)
                    .startNow()
                    .withPriority(10)
                    .withSchedule(simpleSchedule().withIntervalInSeconds(30).repeatForever())
                    .build();

            sched.start();
            sched.scheduleJob(databaseProbeJob, databaseProbeTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
