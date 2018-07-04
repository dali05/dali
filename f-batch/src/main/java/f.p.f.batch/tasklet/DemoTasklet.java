package f.p.f.batch.tasklet;

import f.p.f.core.domain.Balance;
import f.p.f.core.domain.Collectivite;
import f.p.f.core.domain.TypeCollectivite;
import f.p.f.core.repository.CollectiviteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DemoTasklet implements Tasklet, StepExecutionListener {

   /* @Autowired
    CollectiviteRepository collectiviteRepository;*/

    List<Balance> balances;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        balances = (List<Balance>) stepExecution.getJobExecution().getExecutionContext().get("balances");
    }

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        log.info("Inside DemoTasklet :) ...");
        Collectivite collectivite = new Collectivite();
        collectivite.setCode("1");
        collectivite.setTypeCollectivite(TypeCollectivite.COMMUNE);
        for (Balance balance : balances) {
            collectivite.getBalances().add(balance);
            log.info("Balance N°{}", balances.indexOf(balance) + 1);
        }
        //collectiviteRepository.save(collectivite);
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }
}
