package learning.nondbscheduler;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class SimpleJob implements Job {
    public static final String ANSI_BLUE = "\u001B[34m";


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();

        String jobSays = dataMap.getString("jobSays");
        float myFloatValue = dataMap.getFloat("myFloatValue");

        System.out.println(ANSI_BLUE+"Job says: " + jobSays + ", and val is: " + myFloatValue+ANSI_BLUE);
    }
}