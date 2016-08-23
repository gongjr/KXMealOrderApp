package com.asiainfo.mealorder.biz.model.lakala;

import android.os.IBinder;
import android.os.RemoteException;

import com.asiainfo.mealorder.utils.KLog;
import com.lkl.cloudpos.aidl.AidlDeviceService;
import com.lkl.cloudpos.aidl.rfcard.AidlRFCard;

/**
 * Created by gjr on 2016/8/23 11:08.
 * mail : gjr9596@gmail.com
 */
public class RfCardReader {

    /**
     * 非接触式IC卡,读卡器实例
     */
    private AidlRFCard mAidlRFCard=null;

    public AidlRFCard getAidlRFCard() {
        return mAidlRFCard;
    }

    public void setAidlRFCard(AidlRFCard pAidlRFCard) {
        mAidlRFCard = pAidlRFCard;
    }

    /**
     * 初始化非接触式卡
     * @return
     */
    public boolean initRFCard(AidlDeviceService mService){
        if (mAidlRFCard==null){
            boolean isSucess=true;
            try {
                IBinder rfCard=mService.getRFIDReader();
                mAidlRFCard= AidlRFCard.Stub.asInterface(rfCard);
                KLog.i("非接触式卡实例获取成功 mAidlRFCard:" + mAidlRFCard);
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
            KLog.i("已存在非接触式卡实例 mAidlRFCard:"+mAidlRFCard);
            return true;
        }
    }


    /**
     *向非接触式IC读卡器发送指令,向指定存储块写数据
     */
    public RfCardResult writeRfCardData(String wtiteData){
        AidlRFCard aidlRFCard=getAidlRFCard();
        RfCardResult lRfCardResult=new RfCardResult();
        lRfCardResult.setSucess(false);
        lRfCardResult.setInfo("开始执行");
        if(aidlRFCard!=null){
            try {
                if (aidlRFCard.open()){
                    KLog.i("打开非接触式读卡器成功!");
                    if (aidlRFCard.isExist()){
                        KLog.i("检测到卡片类型为:"+aidlRFCard.getCardType());
                        byte[] result_loadAuthor=aidlRFCard.apduComm(Util.toByteArray(ApduCommand.loadAuthor));
                        if (!Util.toHexString(result_loadAuthor).equals(ApduCommand.ErrorCode)){
                            KLog.i("APDU加密码加载成功!");
                            byte[] result_loginAuthor=aidlRFCard.apduComm(Util.toByteArray(ApduCommand.loginAuthor));
                            if (!Util.toHexString(result_loginAuthor).equals(ApduCommand.ErrorCode)){
                                KLog.i("APDU加密验证成功!");
                                byte[] result_writeData=aidlRFCard.apduComm(Util.toByteArray(ApduCommand.writeData));
                                String data=Util.toHexString(result_writeData);
                                if (!data.equals(ApduCommand.ErrorCode)){
                                    KLog.i("APDU写块数据成功!");
                                    lRfCardResult.setSucess(true);
                                    lRfCardResult.setInfo("APDU写块数据成功!");
                                    lRfCardResult.setResult(data);
                                }else{
                                    KLog.i("APDU写块数据失败!");
                                    lRfCardResult.setSucess(false);
                                    lRfCardResult.setInfo("APDU写块数据失败!");
                                    lRfCardResult.setResult(data);
                                }
                            }else{
                                KLog.i("APDU加密验证失败!");
                                lRfCardResult.setSucess(false);
                                lRfCardResult.setInfo("APDU加密验证失败!");
                            }
                        }else{
                            KLog.i("APDU密码加载失败!");
                            lRfCardResult.setSucess(false);
                            lRfCardResult.setInfo("APDU密码加载失败!");
                        }
                    }else{
                        KLog.i("没有检测到卡片!");
                        lRfCardResult.setSucess(false);
                        lRfCardResult.setInfo("没有检测到卡片!");
                    }
                }else{
                    lRfCardResult.setSucess(false);
                    lRfCardResult.setInfo("打开非接触式读卡器失败!");
                    KLog.i("打开非接触式读卡器失败!");
                }
                aidlRFCard.close();
            } catch (RemoteException e) {
                lRfCardResult.setSucess(false);
                lRfCardResult.setInfo("设备驱动异常!");
                e.printStackTrace();
            }catch (Exception e){
                lRfCardResult.setSucess(false);
                lRfCardResult.setInfo("执行异常!");
                e.printStackTrace();
            }
        }else{
            lRfCardResult.setSucess(false);
            lRfCardResult.setInfo("未初始化!");
        }
        return lRfCardResult;
    };

    /**
     *向非接触式IC读卡器发送指令,向指定存储块读取数据
     */
    public RfCardResult readRfCardData(){
        AidlRFCard aidlRFCard=getAidlRFCard();
        RfCardResult lRfCardResult=new RfCardResult();
        lRfCardResult.setSucess(false);
        lRfCardResult.setInfo("开始执行");
        if(aidlRFCard!=null){
            try {
                if (aidlRFCard.open()){
                    KLog.i("打开非接触式读卡器成功!");
                    if (aidlRFCard.isExist()){
                        KLog.i("检测到卡片类型为:"+aidlRFCard.getCardType());
                        byte[] result_loadAuthor=aidlRFCard.apduComm(Util.toByteArray(ApduCommand.loadAuthor));
                        if (!Util.toHexString(result_loadAuthor).equals(ApduCommand.ErrorCode)){
                            KLog.i("APDU加密码加载成功!");
                            byte[] result_loginAuthor=aidlRFCard.apduComm(Util.toByteArray(ApduCommand.loginAuthor));
                            if (!Util.toHexString(result_loginAuthor).equals(ApduCommand.ErrorCode)){
                                KLog.i("APDU加密验证成功!");
                                byte[] result_readData=aidlRFCard.apduComm(Util.toByteArray(ApduCommand.readData));
                                String data=Util.toHexString(result_readData);
                                if (!data.equals(ApduCommand.ErrorCode)){
                                    KLog.i("APDU读块数据成功!");
                                    lRfCardResult.setSucess(true);
                                    lRfCardResult.setInfo("APDU读块数据成功!");
                                    lRfCardResult.setResult(data);
                                }else{
                                    KLog.i("APDU读块数据失败!");
                                    lRfCardResult.setSucess(false);
                                    lRfCardResult.setInfo("APDU读块数据失败!");
                                    lRfCardResult.setResult(data);
                                }
                            }else{
                                KLog.i("APDU加密验证失败!");
                                lRfCardResult.setSucess(false);
                                lRfCardResult.setInfo("APDU加密验证失败!");
                            }
                        }else{
                            KLog.i("APDU密码加载失败!");
                            lRfCardResult.setSucess(false);
                            lRfCardResult.setInfo("APDU密码加载失败!");
                        }
                    }else{
                        KLog.i("没有检测到卡片!");
                        lRfCardResult.setSucess(false);
                        lRfCardResult.setInfo("没有检测到卡片!");
                    }
                }else{
                    lRfCardResult.setSucess(false);
                    lRfCardResult.setInfo("打开非接触式读卡器失败!");
                    KLog.i("打开非接触式读卡器失败!");
                }
                aidlRFCard.close();
            } catch (RemoteException e) {
                lRfCardResult.setSucess(false);
                lRfCardResult.setInfo("设备驱动异常!");
                e.printStackTrace();
            }catch (Exception e){
                lRfCardResult.setSucess(false);
                lRfCardResult.setInfo("执行异常!");
                e.printStackTrace();
            }
        }else{
            lRfCardResult.setSucess(false);
            lRfCardResult.setInfo("未初始化!");
        }
        return lRfCardResult;
    };

}
