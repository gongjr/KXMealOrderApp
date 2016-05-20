package com.asiainfo.mealorder.ui.base;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.entity.DishesProperty;
import com.asiainfo.mealorder.biz.entity.DishesPropertyItem;
import com.asiainfo.mealorder.biz.entity.MerchantDishes;
import com.asiainfo.mealorder.biz.listener.OnPriceRatioPropertyItemsListener;
import com.asiainfo.mealorder.biz.listener.OnPropertyCheckedChangeListener;
import com.asiainfo.mealorder.utils.Arith;

import java.util.ArrayList;
import java.util.List;

import roboguice.inject.InjectView;

/**
 * @author gjr
 *         <p/>
 *         2015年6月25日
 *         <p/>
 *         订单确认页面，价格修改的弹出框
 */
public class EditSinglePriceDF extends DialogFragmentBase {
    private static final String TAG = EditSinglePriceDF.class.getName();
    private View mView;
    private View tv_propertyValue;
    private int curCount; //本菜当前所选数量
    private int position = 0;
    private OnPriceRatioPropertyItemsListener mOnPriceRatioPropertyItemsListener;
    private List<String> curSelectedPropertyList; //当前选择的属性值
    private MerchantDishes mMerchantDishes;
    private Double curPrice;
    @InjectView(R.id.btn_ensure)
    private Button btn_ensure;
    @InjectView(R.id.img_close)
    private ImageView img_close;
    @InjectView(R.id.single_dishes_ratio_price)
    private EditText single_dishes_ratio_price;
    @InjectView(R.id.single_dishes_number_ratio)
    private EditText single_dishes_number_ratio;
    @InjectView(R.id.single_dishes_name)
    private TextView single_dishes_name;
    @InjectView(R.id.single_dishes_old_price)
    private TextView single_dishes_old_price;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMerchantDishes = (MerchantDishes) getArguments().getSerializable("mMerchantDishes");
        Log.d(TAG, "mDishesProperty Name:" + mMerchantDishes.getDishesName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mView = inflater.inflate(R.layout.df_edit_single_price, null);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setCancelable(true);
        initData();
        initListener();
    }


    public void initData() {
        single_dishes_name.setText(mMerchantDishes.getDishesName());
        final int price = Integer.valueOf(mMerchantDishes.getDishesPrice());
        single_dishes_old_price.setText(price + "/份");
        curPrice = 0.0;
        TextWatcher ratioWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String hint = s.toString().trim();
                if (hint.length() != 0&&!hint.substring(0,1).equals(".")) {
                    double ratio = Double.valueOf(hint);
                    curPrice = Arith.mul(price,ratio);
                    curPrice = Arith.round(curPrice,2);
                    single_dishes_ratio_price.setText(curPrice + "");
                } else {
                    curPrice = 0.0;
                    single_dishes_ratio_price.setText(curPrice + "");
                    showShortToast("无效输入!",500);
                }
            }
        };
        single_dishes_number_ratio.addTextChangedListener(ratioWatcher);

        /*String selectedPropItemStr = curSelectedPropertyList.get(0);
        Gson gson=new Gson();
        PropertySelectEntity propEntity = gson.fromJson(selectedPropItemStr, PropertySelectEntity.class);
        if(propEntity!=null&&propEntity.getItemType().equals("xiugai")&&propEntity.getmSelectedItemsList().size()>0){
            String s=propEntity.getmSelectedItemsList().get(0).getItemName();
            s.replace("份","");
            single_dishes_number_ratio.setText(s);
        }*/

    }

    /**
     * 设置当前选中的属性值
     */
    public void setCurPropertyList(List<String> curSelectedPropertyList) {
        this.curSelectedPropertyList = curSelectedPropertyList;
    }

    /**
     * 属性选择或取消的装填改变监听器设置
     */
    private OnPropertyCheckedChangeListener mOnPropertyCheckedChangeListener = new OnPropertyCheckedChangeListener() {
        @Override
        public void onPropertyCheckedChange(DishesProperty property,
                                            DishesPropertyItem propertyItem, Boolean isChecked) {
        }
    };

    /**
     * 设置返回所选属性的监听函数
     */
    public void setOnPriceRatioPropertyItemsListener(int curCount, View v, OnPriceRatioPropertyItemsListener OnPriceRatioPropertyItemsListener, int position) {
        this.curCount = curCount;
        this.tv_propertyValue = v;
        this.position = position;
        this.mOnPriceRatioPropertyItemsListener = OnPriceRatioPropertyItemsListener;
    }

    public void initListener() {
        img_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btn_ensure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (curPrice != 0.0) {
                    DishesProperty dishesProperty = initPublicAttrDishesProperty(single_dishes_number_ratio.getEditableText().toString() + "份");
                    List<DishesPropertyItem> checkedItemsList = dishesProperty.getItemlist();
                    Log.d(TAG, "checkedItemsList size: " + checkedItemsList.size());
                    if (mOnPriceRatioPropertyItemsListener != null) {
                        if (checkedItemsList.size() > 0) {
                            mOnPriceRatioPropertyItemsListener.returnCheckedItems(curCount, tv_propertyValue, checkedItemsList.get(0).getItemType(), curSelectedPropertyList, checkedItemsList, position,curPrice);
                        }
                    }
                } else {
                    showShortToast("设置无效!");
                }
            }
        });
    }

    /**
     * 将份数比例变化模拟为细项加入备注属性组
     *
     * @return
     */
    public DishesProperty initPublicAttrDishesProperty(String numberratio) {
        DishesProperty publicAttr = new DishesProperty();
        publicAttr.setDishesId(mMerchantDishes.getDishesId());
        publicAttr.setItemTypeName("价格比例");
        publicAttr.setItemType("xiugai");
        publicAttr.setMerchantId(mMerchantDishes.getMerchantId());
        List<DishesPropertyItem> dishesPropertyItems = new ArrayList<DishesPropertyItem>();
        DishesPropertyItem dishesPropertyItem = new DishesPropertyItem();
        dishesPropertyItem.setMerchantId(mMerchantDishes.getMerchantId());
        dishesPropertyItem.setItemTypeName("价格比例");
        dishesPropertyItem.setItemType("xiugai");
        dishesPropertyItem.setDishesId(mMerchantDishes.getDishesId());
        dishesPropertyItem.setItemName(numberratio);
        dishesPropertyItem.setItemId("10002");
        dishesPropertyItems.add(dishesPropertyItem);
        publicAttr.setItemlist(dishesPropertyItems);
        return publicAttr;
    }
}
