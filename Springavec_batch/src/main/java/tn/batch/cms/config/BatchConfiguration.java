package tn.batch.cms.config;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import tn.batch.cms.model.Book;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;
    
    @Value("${file.input}")
    private String fileInput;
    
    @Bean
    public FlatFileItemReader reader() {
        return new FlatFileItemReaderBuilder()
          .name("bookItemReader")
          .resource(new ClassPathResource(fileInput))
          .delimited()
          .delimiter(",")
          .names(new String[] {"id", "title", "author", "releaseDate" })
          .linesToSkip(1)
          .fieldSetMapper(new BeanWrapperFieldSetMapper() {{setTargetType(Book.class);}})
          .build();
    }
    
    @Bean
    public JdbcBatchItemWriter writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder()
          .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
          .sql("INSERT INTO book (id,title,author, releaseDate) VALUES (:id, :title, :author, :releaseDate)")
          .dataSource(dataSource)
          .build();
    }
    
    @Bean
    public Job importUserJob( Step step1) {
        return jobBuilderFactory.get("importUserJob")
          .incrementer(new RunIdIncrementer())
          .listener(new JobExecutionListener() {
			
			@Override
			public void beforeJob(JobExecution jobExecution) {
				if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
					logger.info("!!! JOB FINISHED! Time to verify the results");

			        String query = "SELECT id, title, author, releaseDate FROM book";
				}				
			}
			
			@Override
			public void afterJob(JobExecution arg0) {
				logger.info("Job Started");
				
			}
		})
          .flow(step1)
          .end()
          .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter writer) {
        return stepBuilderFactory.get("step1")
          .<Book, Book> chunk(10)
          .reader(reader())
          .processor(processor())
          .writer(writer)
          .build();
    }

    @Bean
    public BookItemProcessor processor() {
        return new BookItemProcessor();
    }
    
    
    
}
