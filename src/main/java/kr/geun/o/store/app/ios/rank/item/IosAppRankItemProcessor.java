package kr.geun.o.store.app.ios.rank.item;

import kr.geun.o.store.app.ios.rank.model.IosAppRankPreVO;
import kr.geun.o.store.app.ios.rank.model.IosAppRankVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.InitializingBean;

/**
 *
 *
 * @author akageun
 */
@Slf4j
public class IosAppRankItemProcessor implements ItemProcessor<IosAppRankPreVO, IosAppRankVO>, InitializingBean {

	@Override
	public IosAppRankVO process(IosAppRankPreVO iosAppRankPreVO) throws Exception {
		log.info("Processor Start");
		return IosAppRankVO.builder().tmpValue(iosAppRankPreVO.getTmpValue()).tmpName("NAME :  " + iosAppRankPreVO.getTmpValue()).build();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Processor afterPropertiesSet");
	}
}
