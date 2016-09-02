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
     * 头部居中店名
     */
    private String merchantName;
    private String merchantName_tag="";

    /**
     * 头部居中标题
     */
    private String title;
    private String title_tag="会员消费单";

    private final String null_line="                               ";
    /**
     * 桌台名称
     */
    private String deskName;
    private String deskName_tag="桌台:";
    /**
     * 收银员
     */
    private String staffName;
    private String staffName_tag="收银员:";

    /**
     * 订单编号
     */
    private String orderId;
    private String orderId_tag="订单编号:";

    /**
     * 结账时间
     */
    private String finishTime;
    private String finishTime_tag="结账时间:";

    /**
     * 32个一行
     */
    private final String middle_line1="--------------------------------";
    /**
     * 卡号:
     */
    private String cardId;
    private String cardId_tag="会员卡号:";
    /**
     * 卡类型:
     */
    private String cardType;
    private String cardType_tag="会员类型:";

    /**
     * 上次剩余积分:
     */
    private String cardScore;
    private String cardScore_tag="上次剩余积分:";

    /**
     * 本次消费积分:
     */
    private String currentUsedScore;
    private String currentUsedScore_tag="本次消费积分:";

    /**
     * 本次剩余积分:
     */
    private String residueScore;
    private String residueScore_tag="本次剩余积分:";


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


    private String memberSign_tag="会员签字:";
    private Boolean isNeedMemberSign=false;


    public Boolean getIsNeedMemberSign() {
        return isNeedMemberSign;
    }

    public void setIsNeedMemberSign(Boolean pIsNeedMemberSign) {
        isNeedMemberSign = pIsNeedMemberSign;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String pMerchantName) {
        merchantName = pMerchantName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String pTitle) {
        title = pTitle;
    }

    public String getDeskName() {
        return deskName;
    }

    public void setDeskName(String pDeskName) {
        deskName = pDeskName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String pStaffName) {
        staffName = pStaffName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String pOrderId) {
        orderId = pOrderId;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String pFinishTime) {
        finishTime = pFinishTime;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String pCardId) {
        cardId = pCardId;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String pCardType) {
        cardType = pCardType;
    }

    public String getCardScore() {
        return cardScore;
    }

    public void setCardScore(String pCardScore) {
        cardScore = pCardScore;
    }

    public String getCurrentUsedScore() {
        return currentUsedScore;
    }

    public void setCurrentUsedScore(String pCurrentUsedScore) {
        currentUsedScore = pCurrentUsedScore;
    }

    public String getResidueScore() {
        return residueScore;
    }

    public void setResidueScore(String pResidueScore) {
        residueScore = pResidueScore;
    }

    public String getConsumePrice() {
        return ConsumePrice;
    }

    public void setConsumePrice(String pConsumePrice) {
        ConsumePrice = pConsumePrice;
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

        PrintItemObj merchantNameItem=new PrintItemObj(merchantName);
        merchantNameItem.setAlign(PrintItemObj.ALIGN.CENTER);
        merchantNameItem.setBold(true);
        merchantNameItem.setFontSize(16);
        lPrintItemObjs.add(merchantNameItem);

        PrintItemObj null_lineItem1=new PrintItemObj(null_line);
        null_lineItem1.setFontSize(4);
        lPrintItemObjs.add(null_lineItem1);

        PrintItemObj titleItem=new PrintItemObj(title_tag);
        titleItem.setAlign(PrintItemObj.ALIGN.CENTER);
        titleItem.setBold(true);
        titleItem.setFontSize(16);
        lPrintItemObjs.add(titleItem);

        PrintItemObj null_lineItem=new PrintItemObj(null_line);
        null_lineItem.setFontSize(8);
        lPrintItemObjs.add(null_lineItem);

        PrintItemObj deskNameItem=new PrintItemObj(deskName_tag+deskName);
        deskNameItem.setFontSize(12);
        lPrintItemObjs.add(deskNameItem);

        PrintItemObj staffNameItem=new PrintItemObj(staffName_tag+staffName);
        staffNameItem.setFontSize(10);
        lPrintItemObjs.add(staffNameItem);

        PrintItemObj orderIdItem=new PrintItemObj(orderId_tag+orderId);
        orderIdItem.setFontSize(10);
        lPrintItemObjs.add(orderIdItem);

        PrintItemObj finishTimeItem=new PrintItemObj(finishTime_tag+finishTime);
        finishTimeItem.setFontSize(10);
        lPrintItemObjs.add(finishTimeItem);

        PrintItemObj timeItem=new PrintItemObj(middle_line1);
        timeItem.setFontSize(10);
        lPrintItemObjs.add(timeItem);

        PrintItemObj cardIdItem=new PrintItemObj(cardId_tag+cardId);
        cardIdItem.setFontSize(10);
        lPrintItemObjs.add(cardIdItem);

        PrintItemObj cardTypeItem=new PrintItemObj(cardType_tag+cardType);
        cardTypeItem.setFontSize(10);
        lPrintItemObjs.add(cardTypeItem);

        PrintItemObj currentUsedScoreItem=new PrintItemObj(currentUsedScore_tag+currentUsedScore);
        currentUsedScoreItem.setFontSize(10);
        lPrintItemObjs.add(currentUsedScoreItem);

        PrintItemObj cardScoreItem=new PrintItemObj(cardScore_tag+cardScore);
        cardScoreItem.setFontSize(10);
        lPrintItemObjs.add(cardScoreItem);

        PrintItemObj residueScoreItem=new PrintItemObj(residueScore_tag+residueScore);
        residueScoreItem.setFontSize(10);
        lPrintItemObjs.add(residueScoreItem);

        lPrintItemObjs.add(timeItem);

        PrintItemObj null_lineItem2=new PrintItemObj(null_line);
        null_lineItem2.setFontSize(8);
        lPrintItemObjs.add(null_lineItem2);

        if (isNeedMemberSign){
            PrintItemObj memberSignItem=new PrintItemObj(memberSign_tag);
            memberSignItem.setFontSize(12);
            memberSignItem.setBold(true);
            lPrintItemObjs.add(memberSignItem);
        }


        PrintItemObj null_lineItem3=new PrintItemObj(null_line);
        null_lineItem3.setFontSize(8);
        lPrintItemObjs.add(null_lineItem3);

        PrintItemObj null_lineItem4=new PrintItemObj(null_line);
        null_lineItem4.setFontSize(8);
        lPrintItemObjs.add(null_lineItem4);

        PrintItemObj null_lineItem5=new PrintItemObj(null_line);
        null_lineItem5.setFontSize(8);
        lPrintItemObjs.add(null_lineItem5);

        PrintItemObj null_lineItem6=new PrintItemObj(null_line);
        null_lineItem6.setFontSize(8);
        lPrintItemObjs.add(null_lineItem6);


        PrintItemObj null_lineItem7=new PrintItemObj(null_line);
        null_lineItem7.setFontSize(8);
        lPrintItemObjs.add(null_lineItem7);

        PrintItemObj null_lineItem8=new PrintItemObj(null_line);
        null_lineItem8.setFontSize(8);
        lPrintItemObjs.add(null_lineItem8);


        return lPrintItemObjs;
    }
}
