package kr.geun.o.store.app.ios.rank.cd;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 *
 * @author akageun
 */
@Getter
@AllArgsConstructor
public enum IosRankCategoryCd {

    //@formatter:off
    IOS_TOP_FREE("topfreeapplications", "IOS 무료 어플 랭킹"),
    IOS_TOP_PAID("toppaidapplications", "IOS 유료 어플 랭킹"),
    IOS_TOP_GROSSING("topgrossingapplications", "IOS 매출 랭킹"),
    IPAD_TOP_FREE("topfreeipadapplications", "IPAD 무료 어플 랭킹"),
    IPAD_TOP_PAID("toppaidipadapplications", "IPAD 유료 어플 랭킹"),
    IPAD_TOP_GROSSING("topgrossingipadapplications", "IPAD 매출 랭킹")
    //@formatter:on
    ;

    private String categoryCd;
    private String cdNm;

}
