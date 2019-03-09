package kr.geun.o.store.app.ios.review.item;

import kr.geun.o.store.app.ios.review.model.IosAppReviewPreVO;
import kr.geun.o.store.app.ios.review.model.IosAppReviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 *
 * @author akageun
 */
@Slf4j
public class IosAppReviewItemProcessor implements ItemProcessor<IosAppReviewPreVO, IosAppReviewVO>, InitializingBean {

	@Override
	public IosAppReviewVO process(IosAppReviewPreVO iosAppReviewPreVO) throws Exception {
		log.info("Processor Start");
		return IosAppReviewVO.builder().tmpValue(iosAppReviewPreVO.getTmpValue()).tmpName("NAME :  " + iosAppReviewPreVO.getTmpValue()).build();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Processor afterPropertiesSet");
	}
}
