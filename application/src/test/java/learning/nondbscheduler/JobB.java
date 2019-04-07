package learning.nondbscheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobB implements Job {
    public static final String ANSI_RED = "\u001B[31m";

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println(ANSI_RED+"This is the job B"+ANSI_RED);
    }

}