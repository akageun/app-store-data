package kr.geun.o.store.config.batchListener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;

/**
 *
 *
 * @author akageun
 */
@Slf4j
public class ChunkExecutionListener extends ChunkListenerSupport {

	@Override
	public void afterChunk(ChunkContext context) {
		log.info("After chunk");
		super.afterChunk(context);
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		super.beforeChunk(context);
	}
}
