package com.asiainfo.mealorder.biz.model.lakala;

import com.lkl.cloudpos.aidl.printer.PrintItemObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gjr on 2016/8/23 15:57.
 * mail : gjr9596@gmail.com
 */
public class MemberConsumeInfo implements PrintModel{

    /**
     * 头部居中标题
     */
    private String title;
    private String title_tag="";
    /**
     * 卡号:
     */
    private String cardId;
    private String cardId_tag="卡号:";
    /**
     * 持卡人:
     */
    private String name;
    private String name_tag="持卡人:";
    /**
     * 消费金额:
     */
    private String ConsumePrice;
    private String ConsumePrice_tag="消费金额:";
    /**
     * 当前余额:
     */
    private String balance;
    private String balance_tag="当前余额:";

    /**
     * 消费时间:
     */
    private String time;
    private String time_tag="消费时间:";

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String pCardId) {
        cardId = pCardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }

    public String getConsumePrice() {
        return ConsumePrice;
    }

    public void setConsumePrice(String pConsumePrice) {
        ConsumePrice = pConsumePrice;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String pTime) {
        time = pTime;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String pBalance) {
        balance = pBalance;
    }

    @Override
    public List<PrintItemObj> getPrintData() {
        List<PrintItemObj> lPrintItemObjs=new ArrayList<>();

        PrintItemObj titleItem=new PrintItemObj(title_tag+title);
        titleItem.setAlign(PrintItemObj.ALIGN.CENTER);
        titleItem.setBold(true);
        titleItem.setFontSize(12);
        lPrintItemObjs.add(titleItem);

        PrintItemObj lineItem=new PrintItemObj("");
        lineItem.setFontSize(10);
        lPrintItemObjs.add(lineItem);

        PrintItemObj cardIdItem=new PrintItemObj(cardId_tag+cardId);
        cardIdItem.setFontSize(10);
        lPrintItemObjs.add(cardIdItem);

        PrintItemObj nameItem=new PrintItemObj(name_tag+name);
        nameItem.setFontSize(10);
        lPrintItemObjs.add(nameItem);

        PrintItemObj ConsumePriceItem=new PrintItemObj(ConsumePrice_tag+ConsumePrice);
        ConsumePriceItem.setFontSize(10);
        lPrintItemObjs.add(ConsumePriceItem);

        PrintItemObj balanceItem=new PrintItemObj(balance_tag+balance);
        balanceItem.setFontSize(10);
        lPrintItemObjs.add(balanceItem);

        PrintItemObj timeItem=new PrintItemObj(time_tag+time);
        timeItem.setFontSize(10);
        timeItem.setUnderline(true);
        lPrintItemObjs.add(timeItem);

        return lPrintItemObjs;
    }
}
