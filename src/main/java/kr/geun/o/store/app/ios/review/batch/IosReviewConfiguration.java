package kr.geun.o.store.app.ios.review.batch;

import kr.geun.o.store.app.ios.review.item.IosAppReviewItemProcessor;
import kr.geun.o.store.app.ios.review.item.IosAppReviewItemReader;
import kr.geun.o.store.app.ios.review.item.IosAppReviewItemWriter;
import kr.geun.o.store.app.ios.review.model.IosAppReviewPreVO;
import kr.geun.o.store.app.ios.review.model.IosAppReviewVO;
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
public class IosReviewConfiguration {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private static final String IOS_APP_REVIEW_JOB_NM = "IOS_APP_REVIEW_JOB";
	private static final String IOS_APP_REVIEW_STEP_NM = "IOS_APP_REVIEW_STEP";

	@Autowired
	private StepExecutionNotificationListener stepExecutionNotificationListener;

	@Autowired
	private ChunkExecutionListener chunkExecutionListener;

	@Autowired
	private TaskExecutor batchTaskExecutor;

	@Value("${spring.batch.max-threads}")
	private int maxThreads;

	@Bean(name = IOS_APP_REVIEW_JOB_NM)
	public Job job(@Autowired @Qualifier(value = IOS_APP_REVIEW_STEP_NM) Step step) {

		//@formatter:off
		return jobBuilderFactory
			.get(IOS_APP_REVIEW_JOB_NM)
				.incrementer(new RunIdIncrementer())
					.start(step)
			.build();
		//@formatter:on
	}

	@Bean(name = IOS_APP_REVIEW_STEP_NM)
	public Step step(IosAppReviewItemReader iosAppReviewItemReader, IosAppReviewItemProcessor iosAppReviewItemProcessor,
		IosAppReviewItemWriter iosAppReviewItemWriter) {
		//@formatter:off
		return stepBuilderFactory
			.get(IOS_APP_REVIEW_STEP_NM)
				.<IosAppReviewPreVO, IosAppReviewVO>chunk(100)
				.reader(iosAppReviewItemReader)
				.processor(iosAppReviewItemProcessor)
				.writer(iosAppReviewItemWriter)

				.listener(stepExecutionNotificationListener)
				.listener(chunkExecutionListener)
				.taskExecutor(batchTaskExecutor)
				.throttleLimit(maxThreads)
			.build();
		//@formatter:on
	}

	@StepScope
	@Bean
	public IosAppReviewItemReader iosAppReviewItemReader() {
		return new IosAppReviewItemReader();
	}

	@StepScope
	@Bean
	public IosAppReviewItemProcessor iosAppReviewItemProcessor() {
		return new IosAppReviewItemProcessor();
	}

	@StepScope
	@Bean
	public IosAppReviewItemWriter iosAppReviewItemWriter() {
		return new IosAppReviewItemWriter();
	}
}