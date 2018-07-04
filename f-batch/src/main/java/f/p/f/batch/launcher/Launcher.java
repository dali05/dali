package f.p.f.batch.launcher;

import f.p.f.batch.exception.BatchException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

@Slf4j
public class Launcher {

    private Launcher() {
        //No implementation
    }

    public static void launchWithConfig(String batchName, Class<?> configClass, boolean oncePerDayMax) throws JobExecutionException, BatchException {
        try {
            // Load properties as env properties
            /*InputStream propertiesInput = Launcher.class.getResourceAsStream("/batch.properties");
            Properties properties = new Properties();
            properties.load(propertiesInput);

            Enumeration<Object> keys = properties.keys();
            while(keys.hasMoreElements()) {
                String key = (String)keys.nextElement();
                System.setProperty(key, properties.getProperty(key));
            }*/

            String profiles = System.getProperty("spring.profiles.active");

            // Check the spring profiles used
            log.info("Start batch \"" + batchName + "\" with profiles : " + System.getProperty("spring.profiles.active"));

            // Load configuration
            @SuppressWarnings("resource")
            AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(configClass);

            JobLauncher jobLauncher = context.getBean(JobLauncher.class);
            Job job = context.getBean(Job.class);

            //Authorize only one execution of each job per day
            JobParameters jobParameters = new JobParameters();
            /*if(oncePerDayMax) {
                jobParameters = new JobParametersBuilder().addString("date", LocalDate.now().toString()).toJobParameters();
            } else {
                jobParameters = new JobParametersBuilder().addLong("timestamp", LocalDate.now().toString()).toJobParameters();
            }*/

            JobExecution execution = jobLauncher.run(job, jobParameters);

            if(!BatchStatus.COMPLETED.equals(execution.getStatus())) {
                throw new BatchException("Unknown error while executing batch : " + batchName);
            }
        }catch (Exception ex){
            log.error("Exception",ex);
            throw new BatchException(ex.getMessage());
        }
    }
}
