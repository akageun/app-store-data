package kr.geun.o.store.app.ios.review.item;

import kr.geun.o.store.app.ios.review.model.IosAppReviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

/**
 *
 *
 * @author akageun
 */
@Slf4j
public class IosAppReviewItemWriter implements ItemWriter<IosAppReviewVO>, InitializingBean {

	@Override
	public void write(List<? extends IosAppReviewVO> list) throws Exception {
		log.info("ItemWriter Start");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("ItemWriter afterPropertiesSet");
	}
}
