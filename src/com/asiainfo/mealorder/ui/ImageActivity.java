package com.asiainfo.mealorder.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.utils.KLog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.uk.co.senab.photoview.PhotoViewAttacher;

import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/8/16 下午4:55
 */
public class ImageActivity extends BaseActivity {

    @InjectView(R.id.img_img)
    private ImageView imageView;

    private AppApplication BaseApp;
    private MerchantRegister merchantRegister;
    private PhotoViewAttacher attacher;

    /**
     * 菜单显示图片的加载参数
     * 默认是ARGB_8888， 使用RGB_565会比使用ARGB_8888少消耗2倍的内存，但是这样会没有透明度
     */
    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisk(false)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .imageScaleType(ImageScaleType.EXACTLY)
            .build();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_image);
        BaseApp = (AppApplication) getApplication();
        merchantRegister = (MerchantRegister) BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);
        String url = "http://115.29.35.199:27890/material_img/images/upload/20000080/membercard_intro_img.jpg";
        ImageLoader.getInstance().displayImage(url, imageView,
                options, mImageLoadingListener);
        attacher = new PhotoViewAttacher(imageView);

    }

    ImageLoadingListener mImageLoadingListener = new ImageLoadingListener() {
        @Override
        public void onLoadingStarted(String arg0, View arg1) {

        }

        @Override
        public void onLoadingFailed(String arg0, View img, FailReason failReason) {
            if(img!=null){
                switch (failReason.getType()) {
                    case IO_ERROR:
                        KLog.i("图片加载IO异常");
                        //服务器下载异常,修改缓存中数据,避免重复请求
                        break;
                    case DECODING_ERROR:
                        KLog.i("图片加载编码异常");
                        break;
                    case NETWORK_DENIED:
                        KLog.i("图片加载网络异常");
                        break;
                    case OUT_OF_MEMORY:
                        KLog.i("图片加载内存溢出");
                        break;
                    case UNKNOWN:
                        KLog.i("图片加载未知异常");
                        //服务器地址无效,修改缓存中数据,避免重复请求
                        break;
                    default:
                        break;
                }
            }else{
                KLog.i("视图已销毁,忽视!");
            }
        }

        @Override
        public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
        }

        @Override
        public void onLoadingCancelled(String arg0, View arg1) {
        }
    };
}
