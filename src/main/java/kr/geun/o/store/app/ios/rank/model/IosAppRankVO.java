package kr.geun.o.store.app.ios.rank.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 *
 * @author akageun
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IosAppRankVO {
	private String appId;
	private String countryCd;
	private String typeCd;
	private String deviceCd;
	private String appName;
	private String appDesc;
	private String appImg;
	private String appLink;
	private String regYmdt;
	private String modYmdt;

	private String batchTimeId;
	private int appRank;
}
