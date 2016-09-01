package com.asiainfo.mealorder.biz.model.lakala;

import android.os.IBinder;
import android.os.RemoteException;

import com.asiainfo.mealorder.utils.KLog;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.magcard.AidlMagCard;
import com.lkl.cloudpos.aidl.magcard.MagCardListener;
import com.lkl.cloudpos.aidl.magcard.TrackData;

/**
 * Created by gjr on 2016/8/23 11:12.
 * mail : gjr9596@gmail.com
 */
public class MagCardReader {
    /**
     * 磁条卡,读卡器实例
     */
    private AidlMagCard mAidlMagCard=null;

    public AidlMagCard getAidlMagCard() {
        return mAidlMagCard;
    }

    public void setAidlMagCard(AidlMagCard pAidlMagCard) {
        mAidlMagCard = pAidlMagCard;
    }

    public boolean initMagCard(AidlDeviceService mService){
        if (mAidlMagCard==null){
            boolean isSucess=true;
            try {
                IBinder magCard=mService.getMagCardReader();
                mAidlMagCard=AidlMagCard.Stub.asInterface(magCard);
                KLog.i("磁条卡实例获取成功 mAidlMagCard:" + mAidlMagCard);
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
            KLog.i("已存在磁条卡实例 mAidlMagCard:"+mAidlMagCard);
            return true;
        }
    }


    /**
     *读取磁条卡数据
     */
    public void getMagCardWithWait(int timeout,MagCardListener pMagCardListener){
        AidlMagCard aidlMagCard=getAidlMagCard();
        if(aidlMagCard!=null){
            try {
                aidlMagCard.searchCard(timeout,pMagCardListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    /**
     * 停止磁条卡的读取状态
     */
    public void stopMagCardbyWait(){
        AidlMagCard aidlMagCard=getAidlMagCard();
        if(aidlMagCard!=null){
            try {
                aidlMagCard.stopSearch();
                KLog.i("取消寻卡,触发cancle");
            } catch (RemoteException e) {
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    MagCardListener.Stub magCardListener =new MagCardListener.Stub() {
        @Override
        public void onTimeout() throws RemoteException {
            KLog.i("onTimeout");
        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            KLog.i("onError");
        }

        @Override
        public void onCanceled() throws RemoteException {
            KLog.i("onCanceled");
        }

        @Override
        public void onSuccess(TrackData trackData) throws RemoteException {
            KLog.i("Cardno:"+trackData.getCardno());
            KLog.i("ExpiryDate:"+trackData.getExpiryDate());
            KLog.i("FirstTrackData:"+trackData.getFirstTrackData());
            KLog.i("SecondTrackData:"+trackData.getSecondTrackData());
            KLog.i("ThirdTrackData:"+trackData.getThirdTrackData());
            KLog.i("ServiceCode:"+trackData.getServiceCode());
            KLog.i("FormatTrackData:"+trackData.getFormatTrackData());
        }

        @Override
        public void onGetTrackFail() throws RemoteException {

        }

        @Override
        public IBinder asBinder() {
            return this;
        }
    };
}
