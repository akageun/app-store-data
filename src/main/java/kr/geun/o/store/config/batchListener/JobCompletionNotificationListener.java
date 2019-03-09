package kr.geun.o.store.config.batchListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

/**
 *
 *
 * @author akageun
 */
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		super.beforeJob(jobExecution);

		log.info("Job Started");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("Job Completed");
		}
	}

}
