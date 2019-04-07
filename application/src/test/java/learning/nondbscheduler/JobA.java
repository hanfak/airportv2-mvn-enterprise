package learning.nondbscheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobA implements Job {
    public static final String ANSI_GREEN = "\u001B[32m";

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {

        System.out.println(ANSI_GREEN+"This is the job A"+ANSI_GREEN);
    }

}
