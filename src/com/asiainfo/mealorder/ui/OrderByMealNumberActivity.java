package com.asiainfo.mealorder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.OrderByMealNumberAdapter;
import com.asiainfo.mealorder.biz.entity.DeskOrder;
import com.asiainfo.mealorder.biz.entity.MerchantDesk;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.utils.StringUtils;

import roboguice.inject.InjectView;

/**
 * @author skynight(skynight@dingtalk.com)
 * @creatTime 16/6/21 下午2:58
 */
public class OrderByMealNumberActivity extends BaseActivity implements View.OnClickListener {

    @InjectView(R.id.om_center_text)
    TextView centerTxt;
    @InjectView(R.id.om_total_count)
    TextView totalCount;
    @InjectView(R.id.om_total_money)
    TextView totalMoney;
    @InjectView(R.id.om_listview)
    ListView listView;
    @InjectView(R.id.om_back_btn)
    Button backBtn;
    @InjectView(R.id.om_sure_btn)
    Button sureBtn;
    private DeskOrder deskOrder;
    private MerchantDesk desk;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_order);
        initData();
        initListener();
    }

    private void initData() {
        deskOrder = gson.fromJson(getIntent().getStringExtra("deskOrder"), DeskOrder.class);
        desk = (MerchantDesk) getIntent().getSerializableExtra("desk");
        String peopleNumber = getIntent().getStringExtra("peopleNumber");
        centerTxt.setText(desk.getDeskName() + "[" + peopleNumber + "]");
        totalCount.setText("共" + deskOrder.getAllGoodsNum() + "个菜");
        String price = deskOrder.getOriginalPrice();
        totalMoney.setText("总价" + StringUtils.str2Double(price)/100 + "元");
        OrderByMealNumberAdapter adapter = new OrderByMealNumberAdapter(this, deskOrder.getOrderGoods());
        listView.setAdapter(adapter);
    }

    private void initListener() {
        backBtn.setOnClickListener(this);
        sureBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.om_back_btn:
                finish();
                break;
            case R.id.om_sure_btn:
                break;
        }
    }
}
