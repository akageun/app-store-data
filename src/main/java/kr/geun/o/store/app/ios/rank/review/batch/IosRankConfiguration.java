package kr.geun.o.store.app.ios.rank.review.batch;

import kr.geun.o.store.app.ios.rank.review.item.IosAppRankItemProcessor;
import kr.geun.o.store.app.ios.rank.review.item.IosAppRankItemReader;
import kr.geun.o.store.app.ios.rank.review.item.IosAppRankItemWriter;
import kr.geun.o.store.app.ios.rank.review.model.IosAppRankPreVO;
import kr.geun.o.store.app.ios.rank.review.model.IosAppRankVO;
import kr.geun.o.store.config.batchListener.ChunkExecutionListener;
import kr.geun.o.store.config.batchListener.StepExecutionNotificationListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/**
 * IOS App Review Batch Configuration
 *
 * @author akageun
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class IosRankConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private static final String IOS_APP_RANK_JOB = "IOS_APP_RANK_JOB";
	private static final String IOS_APP_RANK_STEP = "IOS_APP_RANK_STEP";

	@Autowired
	private StepExecutionNotificationListener stepExecutionNotificationListener;

	@Autowired
	private ChunkExecutionListener chunkExecutionListener;

	@Autowired
	private TaskExecutor batchTaskExecutor;

	@Value("${spring.batch.max-threads}")
	private int maxThreads;

	@Bean(name = IOS_APP_RANK_JOB)
	public Job job(@Autowired @Qualifier(value = IOS_APP_RANK_STEP) Step step) {

		//@formatter:off
		return jobBuilderFactory
			.get(IOS_APP_RANK_JOB)
				.incrementer(new RunIdIncrementer())
					.start(step)
			.build();
		//@formatter:on
	}

	@Bean(name = IOS_APP_RANK_STEP)
	public Step step(IosAppRankItemReader iosAppRankItemReader, IosAppRankItemProcessor iosAppRankItemProcessor,
		IosAppRankItemWriter iosAppRankItemWriter) {
		//@formatter:off
		return stepBuilderFactory
			.get(IOS_APP_RANK_STEP)
				.<IosAppRankPreVO, IosAppRankVO>chunk(100)
				.reader(iosAppRankItemReader)
				.processor(iosAppRankItemProcessor)
				.writer(iosAppRankItemWriter)

				.listener(stepExecutionNotificationListener)
				.listener(chunkExecutionListener)
				.taskExecutor(batchTaskExecutor)
				.throttleLimit(maxThreads)
			.build();
		//@formatter:on
	}

	@StepScope
	@Bean
	public IosAppRankItemReader iosAppRankItemReader() {
		return new IosAppRankItemReader();
	}

	@StepScope
	@Bean
	public IosAppRankItemProcessor iosAppRankItemProcessor() {
		return new IosAppRankItemProcessor();
	}

	@StepScope
	@Bean
	public IosAppRankItemWriter iosAppRankItemWriter() {
		return new IosAppRankItemWriter();
	}
}