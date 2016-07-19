package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.listener.OnLeftBtnClickListener;
import com.asiainfo.mealorder.biz.listener.OnRightBtnClickListener;
import com.asiainfo.mealorder.config.SysEnv;
import com.asiainfo.mealorder.http.AddressState;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.ui.PoPup.CheckUserPwdDF;
import com.asiainfo.mealorder.ui.PoPup.ChooseServerAddressDF;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.utils.Tools;
import com.asiainfo.mealorder.widget.TitleView;

import roboguice.inject.InjectView;

/**
 * 营销活动,编辑预览页
 * Created by gjr on 2016/6/13 17:38.
 * mail : gjr9596@gmail.com
 */
public class SystemActivity extends BaseActivity{
    public final static String SystemActivity="SystemActivity";
    @InjectView(R.id.system_versionName)
    private TextView versionName;
    @InjectView(R.id.system_versionCode)
    private TextView versionCode;
    @InjectView(R.id.system_packagename)
    private TextView packagename;
    @InjectView(R.id.system_server_address)
    private TextView server_address;
    @InjectView(R.id.system_server_toinit)
    private Button system_server_toinit;
    @InjectView(R.id.marketing_underline06)
    private View marketing_underline06;
    @InjectView(R.id.marketing_title)
    private TitleView titleView;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.acitivity_system);
        initData();
        setTitleView();
    }

    private void initData(){
        int curVersionCode=Tools.getVersionCode(mActivity);
        String curVersionName=Tools.getVersionName(mActivity);
        String curPackageName=Tools.getPackageName(mActivity);
        versionName.setText("版本名称: "+curVersionName);
        versionCode.setText("版本号: "+curVersionCode);
        packagename.setText("包名: "+curPackageName);
        showServerAddress();
        server_address.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showCheckUserPwdDF();
                return false;
            }
        });
        system_server_toinit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean isInit=HttpController.getInstance().toInitAddress(mActivity);
                if (isInit)showShortTip("已经是默认配置了~~");
                else showShortTip("恢复默认设置成功!");
                showServerAddress();
            }
        });
    }

    private void setTitleView() {
        titleView.setCenterTxt("系统信息");
        titleView.setRightTxt("退出");
        titleView.setOnLeftBtnClickListener(onLeftBtnClickListener);
        titleView.setOnRightBtnClickListener(onRightBtnClickListener);
    }

    private OnLeftBtnClickListener onLeftBtnClickListener = new OnLeftBtnClickListener() {
        @Override
        public void onLeftBtnClick() {
            finish();
        }
    };

    private OnRightBtnClickListener onRightBtnClickListener = new OnRightBtnClickListener() {
        @Override
        public void onRightBtnClick() {
            finish();
        }
    };


    private void showServerAddress(){
        if(HttpController.getInstance().isInit()){
            server_address.setText("服务器地址: "+ HttpController.getInstance().getAddress().getTitle());
            system_server_toinit.setVisibility(View.GONE);
            marketing_underline06.setVisibility(View.GONE);
        }else{
            server_address.setText("服务器地址: "+ HttpController.getInstance().getAddress().getTitle()+" ("+"非默认升级无法自动更新环境地址"+")");
            system_server_toinit.setVisibility(View.VISIBLE);
            marketing_underline06.setVisibility(View.VISIBLE);

        }
    }
    /**
    * 显示选择服务器环境窗口
    * */
    private void showChooseServerAddressDF() {
        try {
            ChooseServerAddressDF lChooseCardLevelDF= ChooseServerAddressDF.newInstance();
            lChooseCardLevelDF.setOnFinishBackListener(mOnFinifhBackListener);
            lChooseCardLevelDF.show(getSupportFragmentManager(), "ChooseServerAddressDF");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    ChooseServerAddressDF.OnFinifhBackListener mOnFinifhBackListener=new ChooseServerAddressDF.OnFinifhBackListener() {
        @Override
        public void onFinifhBack(AddressState pAddressState) {
           HttpController.getInstance().setAddress(pAddressState);
            HttpController.getInstance().saveAddress(mActivity,pAddressState);
            showServerAddress();
        }
    };

    /**
     * 显示系统密码验证窗口
     * */
    private void showCheckUserPwdDF() {
        CheckUserPwdDF lCheckUserPwdDF = CheckUserPwdDF.newInstance();
        lCheckUserPwdDF.setOnCheckUserPwdListener(mOnCheckUserPwdListener);
        lCheckUserPwdDF.show(getSupportFragmentManager(), "CheckSysPwdDF");
    }

    CheckUserPwdDF.OnCheckUserPwdListener mOnCheckUserPwdListener=new CheckUserPwdDF.OnCheckUserPwdListener() {
        @Override
        public void onSelectBack(String pwd) {
            if (pwd.equals(SysEnv.ChangeServerAddressPwd)){
                showShortTip("修改服务器地址通行密码正确!");
                showChooseServerAddressDF();
            }else{
                showShortTip("修改服务器地址通行密码不正确!");
            }
        }
    };
}
