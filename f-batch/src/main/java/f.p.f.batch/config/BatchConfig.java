package f.p.f.batch.config;

import f.p.f.batch.mapper.BalanceFieldSetMapper;
import f.p.f.batch.processor.BalanceProcessor;
import f.p.f.batch.tasklet.DemoTasklet;
import f.p.f.batch.writer.BalanceWriter;
import f.p.f.core.domain.Balance;
import f.p.f.core.repository.CollectiviteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.partition.StepExecutionSplitter;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Configuration
@EnableBatchProcessing
@ComponentScan({
        "f.p.f.batch",
        "f.p.f.batch.tasklet"
})
public class BatchConfig {

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private DemoTasklet demoTasklet;

    @Bean
    public ResourcelessTransactionManager transactionManager() {
        return new ResourcelessTransactionManager();
    }

    @Bean
    public JobRepository jobRepository(ResourcelessTransactionManager transactionManager) {
        MapJobRepositoryFactoryBean mapJobRepositoryFactoryBean = new MapJobRepositoryFactoryBean(transactionManager);
        mapJobRepositoryFactoryBean.setTransactionManager(transactionManager);
        try {
            return mapJobRepositoryFactoryBean.getObject();
        } catch (Exception ex) {
            log.error("Exception : {}", ex.getMessage(), ex);
            return null;
        }
    }


    @Bean
    //@StepScope
    public FlatFileItemReader<Balance> csvAnimeReader() {
        FlatFileItemReader<Balance> reader = new FlatFileItemReader<>();
        DefaultLineMapper lineMapper = new DefaultLineMapper();
        FieldSetMapper fieldSetMapper = new BalanceFieldSetMapper();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{
                "EXER",
                "IDENT",
                "NDEPT",
                "LBUDG",
                "INSEE",
                "SIREN",
                "CREGI",
                "NOMEN",
                "CTYPE",
                "CSTYP",
                "CACTI",
                "FINESS",
                "SECTEUR",
                "CBUDG",
                "CODBUD1",
                "COMPTE	",
                "BEDEB",
                "BECRE",
                "OBNETDEB",
                "OBNETCRE",
                "ONBDEB",
                "ONBCRE",
                "OOBDEB",
                "OOBCRE",
                "SD",
                "SC"
        });
        tokenizer.setDelimiter(";");

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(lineMapper);
        reader.setResource(new ClassPathResource("Balance_Commune_2017.csv"));
        reader.setLinesToSkip(1);
        return reader;
    }


    @Bean
    public ItemProcessor<Balance, Balance> CsvFileProcessor() {
        return new BalanceProcessor();
    }

    @Bean
    public BalanceWriter balanceWriter() {
        return new BalanceWriter();
    }

    @Bean
    //@StepScope
    public MultiResourcePartitioner partitioner() {
        MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
        ClassLoader cl = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
        try {
            Resource[] resources = resolver.getResources("Balance_Commune_2017.csv");
            partitioner.setResources(resources);
            partitioner.partition(1000);
            return partitioner;
        } catch (IOException e) {
            log.error("IO Exception : {}", e.getMessage(), e);
        }
        return null;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setCorePoolSize(20);
        taskExecutor.setQueueCapacity(50);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }

    @Bean
    public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
        SimpleJobLauncher simpleJobLauncher = new SimpleJobLauncher();
        simpleJobLauncher.setJobRepository(jobRepository);
        return simpleJobLauncher;
    }

    @Bean
    @Qualifier("masterStep")
    public Step masterStep() {
        return steps.get("masterStep")
                .partitioner(step1())
                .partitioner("step1", partitioner())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    public Step step1() {
        return steps.get("step1")
                .<Balance, Balance>chunk(100000)
                .reader(csvAnimeReader())
                .writer(balanceWriter())
                .build();
    }

    @Bean
    public Step step2() {
        return steps.get("step2")
                .tasklet(demoTasklet)
                .build();
    }

    @Bean
    public Job readCsvJob() {
        return jobBuilderFactory.get("readCsvJob")
                .incrementer(new RunIdIncrementer())
                .flow(masterStep())
                .next(step1())
                .next(step2())
                .end()
                .build();
    }
}
