package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView.ViewHolder;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.bean.settleaccount.UserCoupon;
import com.asiainfo.mealorder.utils.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/5/31 下午4:15
 */
public class MemberRecyclerAdapter extends RecyclerView.Adapter<MemberRecyclerAdapter.MyViewHolder> {

    private Context context;
    private  List<UserCoupon> userCouponList;
    private int selectPosition;

    public MemberRecyclerAdapter(Context context, List<UserCoupon> userCouponList, int selectPosition) {
        this.context = context;
        this.userCouponList = userCouponList;
        this.selectPosition = selectPosition;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.lvitem_coupon, viewGroup, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
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
    }

    @Override
    public int getItemCount() {
        return userCouponList.size();
    }

    public class MyViewHolder extends ViewHolder {
        TextView couponName, couponDate;
        ImageView couponImg;

        public MyViewHolder(View itemView) {
            super(itemView);
            couponName = (TextView) itemView.findViewById(R.id.coupon_name);
            couponDate = (TextView) itemView.findViewById(R.id.coupon_date);
            couponImg = (ImageView) itemView.findViewById(R.id.coupon_img);
        }
    }
}
