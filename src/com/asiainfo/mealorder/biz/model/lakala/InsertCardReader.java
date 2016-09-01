package com.asiainfo.mealorder.biz.model.lakala;

import android.os.IBinder;
import android.os.RemoteException;

import com.asiainfo.mealorder.utils.KLog;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.iccard.AidlICCard;

/**
 * Created by gjr on 2016/8/23 11:08.
 * mail : gjr9596@gmail.com
 */
public class InsertCardReader {
    /**
     * 接触式IC卡,读卡器实例
     */
    private AidlICCard mAidlICCard=null;

    public AidlICCard getAidlICCard() {
        return mAidlICCard;
    }

    public void setAidlICCard(AidlICCard pAidlICCard) {
        mAidlICCard = pAidlICCard;
    }

    /**
     * 初始化接触式IC卡
     * @return
     */
    public boolean initICCard(AidlDeviceService mService){
        if (mAidlICCard==null){
            boolean isSucess=true;
            try {
                IBinder insertIcCard=mService.getInsertCardReader();
                mAidlICCard=AidlICCard.Stub.asInterface(insertIcCard);
                KLog.i("接触式IC卡实例获取成功 mAidlICCard:"+mAidlICCard);
            } catch (RemoteException e) {
                isSucess=false;
                e.printStackTrace();
            }catch (Exception e){
                isSucess=false;
                e.printStackTrace();
            }finally {
                return isSucess;
            }
        }else{
            KLog.i("已存在接触式IC卡实例 mAidlICCard:"+mAidlICCard);
            return true;
        }
    }

    /**
     *向接触式读卡器卡发送指令
     */
    public void getICCardData(byte[] apdu){
        AidlICCard aidlICCard=getAidlICCard();
        if(aidlICCard!=null){
            try {
                if (aidlICCard.open()){
                    KLog.i("打开非接触式读卡器成功!");
                    if (aidlICCard.isExist()){
                        KLog.i("检测到卡片!");
                        byte[] resultData=aidlICCard.apduComm(apdu);

                    }else{
                        KLog.i("没有检测到卡片!");
                    }
                }else{
                    KLog.i("打开非接触式读卡器失败!");
                }
                aidlICCard.close();
            } catch (RemoteException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

}
