package kr.geun.o.store.app.ios.review.batch;

import kr.geun.o.store.app.ios.review.repo.AppStoreReviewRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Basic Configuration
 *
 * @author akageun
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BasicConfiguration {

	private static final String BASIC_JOB_NM = "BASIC_JOB";
	private static final String BASIC_STEP_NM = "BASIC_TASKLET_STEP";

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	private AppStoreReviewRepo appStoreReviewRepo;

	/**
	 * Basic Job Configuration
	 *
	 * @return
	 */
	@Bean(name = BASIC_JOB_NM)
	public Job basicJob() {
		//@formatter:off
		return jobBuilderFactory
			.get(BASIC_JOB_NM)
				.incrementer(new RunIdIncrementer())
					.start(basicTaskletStep())
			.build();
		//@formatter:on
	}

	/**
	 * Basic Step Configuration
	 *
	 * @return
	 */
	@Bean(name = BASIC_STEP_NM)
	public Step basicTaskletStep() {
		//@formatter:off
		return stepBuilderFactory
			.get(BASIC_STEP_NM)
				.tasklet((stepContribution, chunkContext) -> {
					log.info("Tasklet Run!! {}", appStoreReviewRepo.selectStr());

					return RepeatStatus.FINISHED;
				})
			.build();
		//@formatter:on
	}
}