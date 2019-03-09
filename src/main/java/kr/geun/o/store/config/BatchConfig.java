package kr.geun.o.store.config;

import kr.geun.o.store.config.batchListener.ChunkExecutionListener;
import kr.geun.o.store.config.batchListener.JobCompletionNotificationListener;
import kr.geun.o.store.config.batchListener.StepExecutionNotificationListener;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 *
 *
 * @author akageun
 */
@EnableBatchProcessing
@Configuration
public class BatchConfig {

	@Bean
	public BatchConfigurer batchConfigurer(@Autowired @Qualifier(value = DBConfig.ConstName.BATCH_BEAN_NM) DataSource batchDataSource,
		@Autowired @Qualifier(value = DBConfig.ConstName.BATCH_MANAGER) DataSourceTransactionManager transactionManager) {

		return new DefaultBatchConfigurer(batchDataSource) {
			@Override
			public PlatformTransactionManager getTransactionManager() {
				return transactionManager;
			}
		};
	}

	@Value("${spring.batch.max-threads}")
	private int maxThreads;

	@Bean
	public TaskExecutor batchTaskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setConcurrencyLimit(maxThreads);
		return taskExecutor;
	}

	@Bean
	public JobCompletionNotificationListener jobExecutionListener() {
		return new JobCompletionNotificationListener();
	}

	@Bean
	public StepExecutionNotificationListener stepExecutionListener() {
		return new StepExecutionNotificationListener();
	}

	@Bean
	public ChunkExecutionListener chunkListener() {
		return new ChunkExecutionListener();
	}
}
