package kr.geun.o.store.app.ios.rank.item;

import kr.geun.o.store.app.ios.rank.model.IosAppRankPreVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.abdera.Abdera;
import org.apache.abdera.model.Document;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.apache.abdera.parser.Parser;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.InitializingBean;

import javax.xml.namespace.QName;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

		itemList = new LinkedList<>();
		getList();

		log.info("Reader Size : {} ", itemList.size());
	}

	//country, id, count
	//https://rss.itunes.apple.com/api/v1/kr/ios-apps/top-grossing/all/100/explicit.json
	private static final String URL_FORMAT = "https://rss.itunes.apple.com/api/v1/%s/ios-apps/%s/all/%d/explicit.rss";

	private static final int MAX_RANK_VALUE = 100;
	private static final String COUNTRY_CD = "kr";
	private static final int TIME_OUT = 3000; // 3초

	private void getList() throws Exception {

		try (CloseableHttpClient httpclient = HttpClients.createDefault();) {

			String callUrl = String.format(URL_FORMAT, "kr", "top-grossing", MAX_RANK_VALUE); //country, IosRankCategoryCd.class, limit count

			HttpGet httpGet = new HttpGet(callUrl);

			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT).setConnectionRequestTimeout(
				TIME_OUT).build();

			httpGet.setConfig(requestConfig);
			CloseableHttpResponse res = httpclient.execute(httpGet);

			if (res.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
				throw new Exception("Not Http Status Code 200 : Current Code : " + res.getStatusLine().getStatusCode());
			}

			HttpEntity entity = res.getEntity();

			Abdera abdera = new Abdera();
			Parser parser = abdera.getParser();

			Document<Feed> doc = parser.parse(commonReplaceInStrm(entity.getContent()));
			Feed feed = doc.getRoot();

			for (int i = 0; i < feed.getEntries().size(); i++) {
				Entry entry = feed.getEntries().get(i);

				try {
					//@formatter:off
					int rank = i;

					IosAppRankPreVO rankInfo = IosAppRankPreVO.builder()
						.countryCd(COUNTRY_CD)
						.typeCd("top-grossing")
						.deviceCd("IOS")
						.appId(entry.getIdElement().getAttributeValue(new QName("http://itunes.apple.com/rss", "id")))
						.appLink(entry.getLink("alternate").getAttributeValue("href"))
						.appDesc(removeSurrogateArea(entry.getSummary()))
						.appImg(entry.getSimpleExtension("http://itunes.apple.com/rss", "image", "im"))
						.appName(removeSurrogateArea(entry.getTitle()))
						.appRank(++rank)

						.build();
					//@formatter:on

					itemList.offer(rankInfo);

				} catch (Exception e) {
					log.error(e.getMessage() + e);
				}
			}

			EntityUtils.consume(entity);

		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * @param oriInStrm
	 * @return
	 * @throws Exception
	 */
	private InputStream commonReplaceInStrm(InputStream oriInStrm) throws Exception {
		String oriStr = StringUtils.replace(IOUtils.toString(oriInStrm, "UTF-8"), "\n", "");
		Pattern rg_list = Pattern.compile("<content type=\"html\">(.*?)</content>", Pattern.DOTALL);
		Matcher m_list = rg_list.matcher(oriStr);

		while (m_list.find()) {
			String preHtmlText = m_list.group(1);
			String aftHtmlText = "";
			aftHtmlText = StringUtils.replace(preHtmlText, "<", "&lt;");
			aftHtmlText = StringUtils.replace(aftHtmlText, ">", "&gt;");

			oriStr = StringUtils.replace(oriStr, preHtmlText, aftHtmlText);
		}

		rg_list = Pattern.compile("<content type=\"text\">(.*?)</content>", Pattern.DOTALL);
		m_list = rg_list.matcher(oriStr);

		while (m_list.find()) {
			String preHtmlText = m_list.group(1);
			String aftHtmlText = "";
			aftHtmlText = StringUtils.replace(preHtmlText, "<", "&lt;");
			aftHtmlText = StringUtils.replace(aftHtmlText, ">", "&gt;");

			oriStr = StringUtils.replace(oriStr, preHtmlText, aftHtmlText);
		}

		InputStream input = new ByteArrayInputStream(oriStr.getBytes("UTF-8"));
		return input;
	}

	/**
	 * 이모지 삭제
	 *
	 * @param val
	 * @return
	 */
	private String removeSurrogateArea(String val) {
		if (val == null)
			return null;

		StringBuffer buf = new StringBuffer();
		int len = val.length();

		for (int i = 0; i < len; i++) {
			char c = val.charAt(i);
			if (0xD800 <= c && c <= 0xDBFF || 0xDC00 <= c && c <= 0xDFFF) {

			} else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	private String getAppImage(String paramStr) {

		String rtnStr = paramStr;
		if (StringUtils.startsWith(paramStr, "https:") == false) {
			rtnStr = StringUtils.replace(paramStr, "//", "http://");
		}
		return rtnStr;
	}
}
