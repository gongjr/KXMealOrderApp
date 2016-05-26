package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserCoupon;
import com.asiainfo.mealorder.utils.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/26 下午3:54
 */
public class MemberGridViewAdapter extends BaseAdapter {

    private Context context;
    private List<UserCoupon> userCouponList;
    private int selectPosition;

    public MemberGridViewAdapter(Context context, List<UserCoupon> userCouponList, int selectPosition) {
        this.context = context;
        this.userCouponList = userCouponList;
        this.selectPosition = selectPosition;
    }

    private class MemberGridViewAdapterViewHolder {
        TextView couponName, couponDate;
        ImageView couponImg;
    }

    @Override
    public int getCount() {
        return userCouponList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MemberGridViewAdapterViewHolder holder = null;
        if (convertView == null) {
            holder = new MemberGridViewAdapterViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.lvitem_coupon, null);
            holder.couponName = (TextView) convertView.findViewById(R.id.coupon_name);
            holder.couponDate = (TextView) convertView.findViewById(R.id.coupon_date);
            holder.couponImg = (ImageView) convertView.findViewById(R.id.coupon_img);
            convertView.setTag(holder);
        } else {
            holder = (MemberGridViewAdapterViewHolder) convertView.getTag();
        }

        if (position == selectPosition) {
            holder.couponImg.setVisibility(View.VISIBLE);
        } else {
            holder.couponImg.setVisibility(View.GONE);
        }
        UserCoupon userCoupon = userCouponList.get(position);
        holder.couponName.setText(userCoupon.getCouponName());
        Date startDate = new Date(userCoupon.getEffectStartDate());
        Date endDate = new Date(userCoupon.getEffectEndDate());
        holder.couponDate.setText("有效期: " + StringUtils.date2Str(startDate, StringUtils.DATE_FORMAT_3) + "-" + StringUtils.date2Str(endDate, StringUtils.DATE_FORMAT_3));
        return convertView;
    }
}
