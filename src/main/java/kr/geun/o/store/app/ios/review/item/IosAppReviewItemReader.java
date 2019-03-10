package kr.geun.o.store.app.ios.review.item;

import kr.geun.o.store.app.ios.review.model.IosAppReviewPreVO;
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
public class IosAppReviewItemReader implements ItemReader<IosAppReviewPreVO>, InitializingBean {

	private Queue<IosAppReviewPreVO> itemList;

	@Override
	public IosAppReviewPreVO read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return itemList.poll();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("Reader afterPropertiesSet Start");

		itemList = getList();

		log.info("Reader Size : {} ", itemList.size());
	}

	//country, id, pageS
	private final String URL_FORMAT = "https://itunes.apple.com/%s/rss/customerreviews/id=%s/sortBy=mostRecent/page=%s/xml";

	private final int START_PAGE_NUM = 1;
	private final int END_PAGE_NUM = 10;

	private LinkedList<IosAppReviewPreVO> getList() {
		return IntStream.rangeClosed(0, 150).mapToObj(i -> IosAppReviewPreVO.builder().tmpValue(i).build()).collect(
			Collectors.toCollection(LinkedList::new));
	}
}
