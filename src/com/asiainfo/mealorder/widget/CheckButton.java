package com.asiainfo.mealorder.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;

public class CheckButton extends Button {
    /**
     * 选中状态下的背景
     */
    private int img_Checked; //@drawable/chk_img_select_s
    /**
     * 未选中状态下的背景
     */
    private int img_Unchecked; //@drawable/chk_img_select_n
    /**
     * checkbutton当前选中状态
     */
    private boolean checkState;
    /**
     * 点击事件
     */
    private OnClickListener checkbuttonListener;
    
    public CheckButton(Context context, int imgChecked, int imgUnchecked) {
        super(context);
        this.checkState = false;
        this.img_Checked = imgChecked;
        this.img_Unchecked = imgUnchecked;

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	checkState = !checkState;
                if (checkState) {
                    v.setBackgroundResource(img_Checked);
                } else {
                    v.setBackgroundResource(img_Unchecked);
                }
                if (checkbuttonListener != null)
                    checkbuttonListener.onClick(v);
            }
        });
    }
    
    public int getImg_Checked() {
		return img_Checked;
	}

	public void setImg_Checked(int img_Checked) {
		this.img_Checked = img_Checked;
	}

	public int getImg_Unchecked() {
		return img_Unchecked;
	}

	public void setImg_Unchecked(int img_Unchecked) {
		this.img_Unchecked = img_Unchecked;
	}

	public boolean getCheckedStete() {
        return checkState;
    }

    public void setCheckedState(boolean checkState) {
        this.checkState = checkState;
        if (this.checkState) {
            setBackgroundResource(img_Checked);
        } else {
            setBackgroundResource(img_Unchecked);
        }
    }

    public OnClickListener getCheckbuttonListener() {
        return checkbuttonListener;
    }

    public void setCheckbuttonListener(OnClickListener checkbuttonListener) {
        this.checkbuttonListener = checkbuttonListener;
    }
}
