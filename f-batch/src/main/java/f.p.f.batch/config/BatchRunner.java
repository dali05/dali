package f.p.f.batch.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
//@Import(BatchConfig.class)
public class BatchRunner {
    public static void main(String[] args) {
        /*try {
            Launcher.launchWithConfig("Demo Tasklet ...", BatchConfig.class, false);
        }catch (Exception ex){
            log.error("Batch Exception ...", ex);
        }*/
        SpringApplication.run(BatchRunner.class, args);
    }
}
