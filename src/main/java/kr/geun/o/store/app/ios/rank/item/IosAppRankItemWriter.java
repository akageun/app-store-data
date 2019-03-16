package kr.geun.o.store.app.ios.rank.item;

import kr.geun.o.store.app.ios.rank.model.IosAppRankVO;
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
public class IosAppRankItemWriter implements ItemWriter<IosAppRankVO>, InitializingBean {

	@Override
	public void write(List<? extends IosAppRankVO> list) throws Exception {
		log.info("ItemWriter Start");
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("ItemWriter afterPropertiesSet");
	}
}
