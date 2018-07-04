package f.p.f.batch.writer;

import com.google.common.collect.Lists;
import f.p.f.core.domain.Balance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class BalanceWriter implements ItemWriter<Balance>, StepExecutionListener {

    List<Balance> balances;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        balances = Lists.newArrayList();
    }

    @Override
    public void write(List<? extends Balance> list) throws Exception {
       /* for (Balance balance : list){
            //balance.set
        }*/
        balances.addAll(list);
        log.info("Balance size {}", balances.size());

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution.getJobExecution().getExecutionContext().put("balances", balances);
        return ExitStatus.COMPLETED;
    }
}
