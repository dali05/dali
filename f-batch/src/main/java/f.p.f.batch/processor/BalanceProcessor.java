package f.p.f.batch.processor;

import f.p.f.core.domain.Balance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class BalanceProcessor implements ItemProcessor<Balance, Balance> {
    @Override
    public Balance process(Balance balance) throws Exception {
        //log.info("Inside BalanceProcessor : {} {} {}", balance.getExer(), balance.getIdent(), balance.getNDept());
        //return Balance.builder().exer(balance.getExer()).ident(balance.getIdent()).nDept(balance.getNDept()).build();
        return balance;
    }
}
