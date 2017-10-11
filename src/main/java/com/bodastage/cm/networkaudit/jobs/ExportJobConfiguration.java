package com.bodastage.cm.networkaudit.jobs;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


@Configuration
@Component
@EnableBatchProcessing
public class ExportJobConfiguration {
	
	@Autowired
    public JobBuilderFactory jobBuilderFactory;
	
	@Autowired
    private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
    public DataSource dataSource;

	/**
	 * Create custom item reader for rules 
	 */
	@Bean
	@Qualifier("exportJobDatabaseItemReader")
	ItemReader< Map<String, Object>> databaseItemReader(DataSource dataSource) {
		JdbcCursorItemReader< Map<String, Object>> databaseReader = new JdbcCursorItemReader<>();
		
        databaseReader.setDataSource(dataSource);
        databaseReader.setSql("SELECT * FROM networkaudit.rule_missing_externals");
        databaseReader.setRowMapper(new ColumnMapRowMapper());
        
        return databaseReader;
	}
	
	@Bean
	@Qualifier("exportJobFileWriter")
    public ItemWriter<Map<String, Object>> fileWriter() {
		FlatFileItemWriter<Map<String, Object>> flatFileItemWriter = new FlatFileItemWriter<Map<String, Object>>();
		flatFileItemWriter.setName("file-writer");
		flatFileItemWriter.setResource(new FileSystemResource("resources/rule_missing_externals.csv"));
		
		DelimitedLineAggregator delimitedLineAggregator =  new DelimitedLineAggregator<Map<String, Object>>() {
            {
                setDelimiter(",");
                setFieldExtractor(stringObjectMap -> {
                	Iterator<Map.Entry<String, Object>> iter = stringObjectMap.entrySet().iterator();

                    Object [] values = new Object[stringObjectMap.entrySet().size()];
                	int idx = 0;
                    while(iter.hasNext()) {
                    	values[idx] = iter.next().getValue();
                    	idx++;
                    }

                    return values;
                });
            }
        };
        flatFileItemWriter.setLineAggregator(delimitedLineAggregator);

        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<String> fields = jdbcTemplate.query("SELECT * FROM networkaudit.rule_missing_externals LIMIT 1",new DBResultSetExtractor()) ;
        
        ExportFileHeaderCallback fileHeadeCallback = new ExportFileHeaderCallback();
        fileHeadeCallback.setFields(fields);
        flatFileItemWriter.setHeaderCallback(fileHeadeCallback);
        
       return flatFileItemWriter;
    }
	
	
   @Bean
   @Qualifier("exportJobStep")
   public Step step( ) {
     return stepBuilderFactory.get("step")
     .<Map<String,Object>, Map<String,Object>>chunk(10)
     .reader(this.databaseItemReader(dataSource))
     .writer(this.fileWriter())
     .build();
   }
	   
   @Bean(name="fileExportJob")
   @Qualifier("fileExportJob")
   public Job job(@Qualifier("exportJobStep") Step step) throws Exception {
     return jobBuilderFactory.get("fileExportJob")
         .incrementer(new RunIdIncrementer())
         .start(step)
         .build();
   }
	
}
