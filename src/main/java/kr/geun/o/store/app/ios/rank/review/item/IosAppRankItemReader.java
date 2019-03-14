package kr.geun.o.store.app.ios.rank.review.item;

import kr.geun.o.store.app.ios.rank.review.model.IosAppRankPreVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.InitializingBean;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *
 *
 * @author akageun
 */
@Slf4j
public class IosAppRankItemReader implements ItemReader<IosAppRankPreVO>, InitializingBean {

	private Queue<IosAppRankPreVO> itemList;

	@Override
	public IosAppRankPreVO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return itemList.poll();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Reader afterPropertiesSet Start");

		itemList = getList();

		log.info("Reader Size : {} ", itemList.size());
	}

	//country, id, count
	//https://rss.itunes.apple.com/api/v1/kr/ios-apps/top-grossing/all/100/explicit.json
	private final String URL_FORMAT = "https://itunes.apple.com/%s/rss/%s/limit=%d/xml";

	private final int MAX_RANK_VALUE = 100;

	private LinkedList<IosAppRankPreVO> getList() {

		String.format(URL_FORMAT, "", "", MAX_RANK_VALUE);

		return IntStream.rangeClosed(0, 150).mapToObj(i -> IosAppRankPreVO.builder().tmpValue(i).build()).collect(
			Collectors.toCollection(LinkedList::new));
	}
}
