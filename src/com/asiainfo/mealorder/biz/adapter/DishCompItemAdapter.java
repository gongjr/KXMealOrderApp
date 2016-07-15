package com.asiainfo.mealorder.biz.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.biz.entity.DishesComp;
import com.asiainfo.mealorder.biz.entity.DishesCompItem;
import com.asiainfo.mealorder.biz.entity.DishesProperty;
import com.asiainfo.mealorder.biz.entity.DishesPropertyItem;
import com.asiainfo.mealorder.biz.entity.MerchantDesk;
import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.helper.PropertySelectEntity;
import com.asiainfo.mealorder.biz.listener.OnDishesCompCheckedChangedListener;
import com.asiainfo.mealorder.biz.listener.OnEnsureCheckedPropertyItemsListener;
import com.asiainfo.mealorder.biz.listener.OnItemClickListener;
import com.asiainfo.mealorder.ui.PoPup.ChoosePropertyValueDF;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DishCompItemAdapter extends BaseAdapter {

    private static final String TAG = DishCompItemAdapter.class.getSimpleName();

    private Context mContext;
    private FragmentActivity mActivity;
    private LayoutInflater mInflater;
    private List<DishesCompItem> defDishesCompItemList;
    private List<OrderGoodsItem> curGoodsItemList;
    private List<DishesCompItem> mDataList;
    private int selectedPos = 0;
    private OnItemClickListener mOnItemClickListener, mOnPropertyDropDownClickListener;
    private Map<String, Boolean> mItemCheckedStateMap;
    private Map<String, String> maxSelect;
    private OnDishesCompCheckedChangedListener mOnDishesCompCheckedChangedListener;
    private DishesCompItem mDishesCompItem;
    private LoginUserPrefData mLoginUserPrefData;
    private MerchantDesk mCurDesk;
    private String DISHES_ID;
    private Gson gson = new Gson();
    private List<DishesComp> mDishCompsPartionDataList;

    public DishCompItemAdapter(Context mContext, FragmentActivity mActivity, List<DishesCompItem> mDataList,
                               List<OrderGoodsItem> curGoodsItemList, OnItemClickListener mOnItemClickListener,
                               Map<String, String> maxSelect, List<DishesComp> mDishCompsPartionDataList) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.curGoodsItemList = curGoodsItemList;
        this.mDataList = mDataList;
        this.mOnItemClickListener = mOnItemClickListener;
        this.mItemCheckedStateMap = new HashMap<String, Boolean>();
        this.maxSelect = maxSelect;
        this.mLoginUserPrefData = new LoginUserPrefData(mContext);
        this.mInflater = LayoutInflater.from(mContext);
        this.mDishCompsPartionDataList = mDishCompsPartionDataList;
        updateCheckedStateMap();
    }

    /**
     * 设置当前桌子
     */
    public void setCurDesk(MerchantDesk mCurDesk) {
        this.mCurDesk = mCurDesk;
    }

    /**
     * 设置套餐主菜的dishesid
     */
    public void setDishesId(String dishesId) {
        this.DISHES_ID = dishesId;
    }

    public void onRefreshDefCompItemList(List<DishesCompItem> defDishesCompItemList) {
        this.defDishesCompItemList = defDishesCompItemList;
        updateDefCompItemList();
    }

    /**
     * 更新默认选中的数据列表
     */
    private void updateDefCompItemList() {
        if (curGoodsItemList == null || curGoodsItemList.size() == 0) {
            if (defDishesCompItemList != null && defDishesCompItemList.size() > 0) {
                Log.d(TAG, "defDishesCompItemList size:" + defDishesCompItemList.size());
                for (int m = 0; m < defDishesCompItemList.size(); m++) {
                    DishesCompItem compItem = defDishesCompItemList.get(m);
                    mItemCheckedStateMap.put(compItem.getDishesId(), true);
                    addDishesToCurSelectedList(compItem.getDishesId());
                }
            }
        }
    }

    /**
     * 初始化套餐组合项选中状态
     */
    private void updateCheckedStateMap() {
        //首先置为false
        if (mDataList != null && mDataList.size() > 0) {
            for (int m = 0; m < mDataList.size(); m++) {
                DishesCompItem compItem = mDataList.get(m);
                mItemCheckedStateMap.put(compItem.getDishesId(), false);
            }
        }

        //如果有选中，则置为true
        if (curGoodsItemList != null && curGoodsItemList.size() > 0) {
            Log.d(TAG, "curGoodsItemList size:" + curGoodsItemList.size());
            for (int n = 0; n < curGoodsItemList.size(); n++) {
                OrderGoodsItem goodsItem = curGoodsItemList.get(n);
                mItemCheckedStateMap.put(goodsItem.getSalesId(), true);
            }
        }
    }

    /**
     * @param mOnPropertyDropDownClickListener
     */
    public void setOnPropertyDropDownClickListener(OnItemClickListener mOnPropertyDropDownClickListener) {
        this.mOnPropertyDropDownClickListener = mOnPropertyDropDownClickListener;
    }

    /**
     * 套餐菜项选中状态改变监听器
     *
     * @param mOnDishesCompCheckedChangedListener
     */
    public void setOnDishesCompCheckedChangedListener(OnDishesCompCheckedChangedListener mOnDishesCompCheckedChangedListener) {
        this.mOnDishesCompCheckedChangedListener = mOnDishesCompCheckedChangedListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lvitem_dishcomp_partion_item, parent, false);
            viewHolder.ll_propertyItems = (LinearLayout) convertView.findViewById(R.id.ll_properties);
            viewHolder.tv_dishCompName = (TextView) convertView.findViewById(R.id.tv_dish_comp_name);
            viewHolder.tv_property = (TextView) convertView.findViewById(R.id.tv_property);
            viewHolder.tv_propertyName = (TextView) convertView.findViewById(R.id.tv_property_name);
            viewHolder.chk_isItemSelect = (CheckBox) convertView.findViewById(R.id.chk_isItemSelect);
            viewHolder.img_dropDown = (ImageView) convertView.findViewById(R.id.img_drop_down);
            viewHolder.btn_ensure = (Button) convertView.findViewById(R.id.btn_ensure);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DishesCompItem dishesCompItem = mDataList.get(position);
        viewHolder.tv_dishCompName.setText(dishesCompItem.getDishesName());

        if (position == selectedPos) {
            mDishesCompItem = mDataList.get(position);
            //显示属性面板和内容
            if (dishesCompItem != null) {
                final List<DishesProperty> propertyList = dishesCompItem.getDishesItemTypelist();
                if (propertyList != null && propertyList.size() > 0) {
                    viewHolder.ll_propertyItems.setVisibility(View.VISIBLE);
                    viewHolder.ll_propertyItems.removeAllViews();
                    for (int i = 0; i < propertyList.size(); i++) {
                        DishesProperty dp = propertyList.get(i);
                        final int propertyPos = i;
                        LinearLayout.LayoutParams ll = new LayoutParams(LayoutParams.MATCH_PARENT, Constants.PROPERTY_LAYOUT_HEIGHT);
                        /*属性项全局视图*/
                        View propertyView = mInflater.inflate(R.layout.layout_dishes_property, null);
						/*属性名*/
                        TextView tv_propertyName = (TextView) propertyView.findViewById(R.id.tv_property_name);
						/*选择属性值的可点击区域*/
                        RelativeLayout rl_propertyItems = (RelativeLayout) propertyView.findViewById(R.id.rl_property_items);
						/*属性值默认显示*/
                        final TextView tv_propertyValue = (TextView) propertyView.findViewById(R.id.tv_property);

                        tv_propertyName.setText(dp.getItemTypeName());
                        List<DishesPropertyItem> dpItemList = dp.getItemlist(); //获取当前菜品的的属性

                        OrderGoodsItem curOrderGoodsItem = null;
                        for (int m = 0; m < curGoodsItemList.size(); m++) {
                            if (curGoodsItemList.get(m).getSalesId().equals(dishesCompItem.getDishesId())) {
                                curOrderGoodsItem = curGoodsItemList.get(m);
                                break;
                            }
                        }

                        //设置默认值选中值, 第一项
                        boolean isProperty = false;
                        if (curOrderGoodsItem != null && curOrderGoodsItem.getRemark() != null) {
                            for (String property : curOrderGoodsItem.getRemark()) {
                                PropertySelectEntity entity = gson.fromJson(property, PropertySelectEntity.class);
                                if (entity.getItemType().equals(dp.getItemType())) {
                                    if (entity.getmSelectedItemsList().size() > 0) {
                                        tv_propertyValue.setText(entity.getmSelectedItemsList().get(0).getItemName());
                                        isProperty = true;
                                        break;
                                    }
                                }
                            }
                        }
                        if (!isProperty && dpItemList != null && dpItemList.size() > 0) { //没有属性则设置默认属性项显示
                            DishesPropertyItem dpItem = dpItemList.get(0);
                            tv_propertyValue.setText(dpItem.getItemName());
                        }

                        tv_propertyValue.setTag(R.id.tag_first, viewHolder.chk_isItemSelect);
                        //final int curCount = curDishesSelectedCount(dishesCompItem); //套餐菜，默认每项数量为1
                        rl_propertyItems.setOnClickListener(new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /**
                                 * 取代监听器形式设置，直接在adapter里面处理，优化逻辑
                                 */
                                onPropertyItemsClick(1, tv_propertyValue, dishesCompItem, propertyPos);
                            }
                        });

                        viewHolder.ll_propertyItems.addView(propertyView, ll);
                    }
                }
            }
        } else {
            viewHolder.ll_propertyItems.setVisibility(View.GONE);
        }

        /**
         * 更改套餐项选中状态
         */
        viewHolder.chk_isItemSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "Dishes Name:  " + dishesCompItem.getDishesName() + "isChecked: " + isChecked);
                if (dishesCompItem.getDishesName().equals("餐前面包")) {
                    Log.d(TAG, "Dishes Name:  " + dishesCompItem.getDishesName() + "isChecked: " + isChecked);
                }
                String dishesId = dishesCompItem.getDishesId();
                onDishesCompCheckedChange(position, isChecked, dishesId);
            }
        });
        //重复事件监听,处理规则判断
        viewHolder.chk_isItemSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateCheckedStateMap();
                //每次点击都遍历curGoodsItemList选中数据更新标志集mItemCheckedStateMap
                String maxCount = maxSelect.get(mDataList.get(position).getDishesTypeCode());
                int count = 0;
                for (int j=0; j<mDataList.size(); j++) {
                    DishesCompItem mDishesCompItemTest = mDataList.get(j);
                    if (mDishesCompItemTest.getDishesTypeCode().equals(mDataList.get(position).getDishesTypeCode())) {
                        if (mItemCheckedStateMap.get(mDishesCompItemTest.getDishesId())) {
                            count ++;
                        }
                    }
                }

                //先判断本菜dishesid是否有选中标志
                if (!mItemCheckedStateMap.get(mDataList.get(position).getDishesId())) {
                    String dishesTypeCode = mDataList.get(position).getDishesTypeCode();
                    int size = mDishCompsPartionDataList.size();
                    String dishesType = "";
                    for (int i=0; i<size; i++) {
                        if (mDishCompsPartionDataList.get(i).getDishesType().equals(dishesTypeCode)){
                            dishesType = mDishCompsPartionDataList.get(i).getDishesTypeName();
                        }
                    }

                    if (Integer.valueOf(maxCount) == 1) {
                        Toast.makeText(mContext, dishesType + ":至少要选择一份", Toast.LENGTH_SHORT).show();
                        mItemCheckedStateMap.put(mDataList.get(position).getDishesId(), true);
                    } else {
                        count --;
                        if(count<0){
                            Toast.makeText(mContext, dishesType +":至少要选择一份", Toast.LENGTH_SHORT).show();
                            mItemCheckedStateMap.put(mDataList.get(position).getDishesId(),true);
                        }
                        else mItemCheckedStateMap.put(mDataList.get(position).getDishesId(),false);

                    }
                } else {
                    //初始dishesid没有被选中时的操作
                    //单选项
                    if (Integer.valueOf(maxCount) == 1) {
                        for (int j=0; j<mDataList.size(); j++) {
                            DishesCompItem mDishesCompItemTest = mDataList.get(j);
                            if (mDishesCompItemTest.getDishesTypeCode().equals(mDataList.get(position).getDishesTypeCode())) {
                                if (mItemCheckedStateMap.get(mDishesCompItemTest.getDishesId())) {
                                    mItemCheckedStateMap.put(mDishesCompItemTest.getDishesId(), false);//遍历清除旧单选项标志
                                }
                            }
                        }
                        mItemCheckedStateMap.put(mDataList.get(position).getDishesId(), true);//确认选中新单选项
                    } else {
                        //复选项
                        if (count > Integer.valueOf(maxCount)) {
                            String dishesTypeCode = mDataList.get(position).getDishesTypeCode();
                            int size = mDishCompsPartionDataList.size();
                            String dishesType = "";
                            for (int i=0; i<size; i++) {
                                if (mDishCompsPartionDataList.get(i).getDishesType().equals(dishesTypeCode)){
                                    dishesType = mDishCompsPartionDataList.get(i).getDishesTypeName();
                                }
                            }

                            Toast.makeText(mContext, dishesType + ":最多只能选择" + maxCount + "份", Toast.LENGTH_SHORT).show();
                            mItemCheckedStateMap.put(mDataList.get(position).getDishesId(), false);
                        } else {
                            mItemCheckedStateMap.put(mDataList.get(position).getDishesId(), true);//确认选中新单选项
                        }
                    }
                }
            }
        });

        Boolean isChecked = mItemCheckedStateMap.get(dishesCompItem.getDishesId());
        Log.d(TAG, "Dishes Name: " + dishesCompItem.getDishesName() + "  IsChecked: " + isChecked);
        if (dishesCompItem.getDishesName().equals("餐前面包")) {
            Log.d(TAG, "Dishes Name:  " + dishesCompItem.getDishesName() + "isChecked: " + isChecked);
        }
        if (isChecked == null || !isChecked) {
            //未选中
            viewHolder.chk_isItemSelect.setChecked(false);
        } else {
            //选中
            viewHolder.chk_isItemSelect.setChecked(true);
        }

        /**
         * 整个视图点击事件
         */
        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, position);
                }
            }
        });

        return convertView;
    }

    /**
     * 点击属性选择，直接在adapter里面处理，优化逻辑层次
     *
     * @param
     */
    public void onPropertyItemsClick(int curCount, View propertyValue, DishesCompItem dishesCompItem, int propertyPos) {
        try {
            List<DishesProperty> propertyList = dishesCompItem.getDishesItemTypelist();
            int curIndex = -1;
            for (int i = 0; i < curGoodsItemList.size(); i++) {
                if (curGoodsItemList.get(i).getSalesId().equals(dishesCompItem.getDishesId())) {
                    curIndex = i;
                    break;
                }
            }
            if (propertyList != null && propertyList.size() > propertyPos) {
                DishesProperty dpCur = propertyList.get(propertyPos);  //根据位置获取指定属性
                ChoosePropertyValueDF mChoosePropertyValueDF = ChoosePropertyValueDF.newInstance(Constants.CHOOSE_PROPERTY_DIALOG_FROM_MAKE_ORDER, dpCur);
                List<String> curSelectedPropertyList = new ArrayList<String>();
                if (curIndex != -1)
                    curSelectedPropertyList = curGoodsItemList.get(curIndex).getRemark();
                propertyValue.setTag(R.id.tag_second, curIndex);
                mChoosePropertyValueDF.setCurPropertyList(curSelectedPropertyList);

                mChoosePropertyValueDF.setOnEnsureCheckedPropertyItemsListener(curCount, propertyValue, mOnEnsureCheckedPropertyItemsListener, 0);
                mChoosePropertyValueDF.show(mActivity.getSupportFragmentManager(), "df_choose_property_value");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 确定所选的菜品属性值
     */
    private OnEnsureCheckedPropertyItemsListener mOnEnsureCheckedPropertyItemsListener = new OnEnsureCheckedPropertyItemsListener() {
        @Override
        public void returnCheckedItems(int curCount, View propertyValue, String propertyType, List<String> curSelectedPropertyList, List<DishesPropertyItem> checkedPropertyItems, int position) {
            Integer curIndex = (Integer) propertyValue.getTag(R.id.tag_second);
            CheckBox chk_isItemSelect = (CheckBox) propertyValue.getTag(R.id.tag_first);
            PropertySelectEntity mNewPropertyEntity = new PropertySelectEntity();
            mNewPropertyEntity.setItemType(propertyType);
            if (checkedPropertyItems != null && checkedPropertyItems.size() > 0) {
                ((TextView) propertyValue).setText(checkedPropertyItems.get(0).getItemName()); //显示选中属性值的第一项
            }
            mNewPropertyEntity.setmSelectedItemsList(checkedPropertyItems);
            updateDishesPropertyChoice(curCount, curSelectedPropertyList, mNewPropertyEntity, curIndex, chk_isItemSelect); //应该有默认选择，这里只做更改
        }
    };


    /**
     * 更新选择的属性数据
     * 全量的，替换当前属性的全部值
     */
    private void updateDishesPropertyChoice(int curCount, List<String> curSelectedPropertyList, PropertySelectEntity mNewPropertyEntity, int index, CheckBox chk_isItemSelect) {
        List<String> all = curSelectedPropertyList;
        String itemType = "";
        if (mNewPropertyEntity != null) {
            itemType = mNewPropertyEntity.getItemType();
        }
        PropertySelectEntity curPropertySelectEntity = null;
        int curIndex = -1;
        for (int i = 0; i < all.size(); i++) {
            curPropertySelectEntity = gson.fromJson(all.get(i), PropertySelectEntity.class);
            if (curPropertySelectEntity.getItemType().equals(itemType)) {
                //获得修改属性类型itemType，在当前属性列表中的index
                curIndex = i;
                break;
            }
        }
        if (curIndex == -1) {
            //修改类型itemtype，在当前属性列表中没有，则add
            curIndex = all.size();
            curPropertySelectEntity = new PropertySelectEntity();
            curPropertySelectEntity.setItemType(itemType);
            curPropertySelectEntity.setmSelectedItemsList(mNewPropertyEntity.getmSelectedItemsList());
            String allremark = gson.toJson(curPropertySelectEntity);
            all.add(allremark);
        } else {
            curPropertySelectEntity.setmSelectedItemsList(mNewPropertyEntity.getmSelectedItemsList());
            String allremark = gson.toJson(curPropertySelectEntity);
            all.set(curIndex, allremark);
            //修改类型itemtype，在当前属性列表中存在，则替换对应index
        }

        //刷新显示属性
        if (mNewPropertyEntity.getmSelectedItemsList() != null && mNewPropertyEntity.getmSelectedItemsList().size() > 0) {
            String dishesId = mNewPropertyEntity.getmSelectedItemsList().get(0).getDishesId();
            String mDishesId = mDishesCompItem.getDishesId();
            if (dishesId != null && mDishesId != null && dishesId.equals(mDishesId)) {
                updateDishesCompSelectionByIndex(mDishesCompItem, all, index, chk_isItemSelect);
                updateCheckedStateMap();
            }
        }
    }

    /**
     * 更新属性by在list中的index
     *
     * @param mDishesCompItem
     * @param mRemarkList
     * @param index
     */
    private void updateDishesCompSelectionByIndex(DishesCompItem mDishesCompItem, List<String> mRemarkList, int index, CheckBox chk_isItemSelect) {

        if (index != -1) {
            OrderGoodsItem goodsItem = curGoodsItemList.get(index);
            goodsItem.setRemark(mRemarkList);
            curGoodsItemList.set(index, goodsItem);
        } else {
            OrderGoodsItem goodsItem = new OrderGoodsItem();
            goodsItem.setCompId(DISHES_ID); //套餐菜统一设置成对应套餐的dishesId
            goodsItem.setTradeStaffId(mLoginUserPrefData.getStaffId());
            goodsItem.setDeskId(mCurDesk.getDeskId());
            goodsItem.setDishesPrice(mDishesCompItem.getDishesPrice());
            goodsItem.setDishesTypeCode(mDishesCompItem.getDishesTypeCode());
            goodsItem.setExportId(mDishesCompItem.getExportId());
            goodsItem.setInstanceId("" + System.currentTimeMillis());
            goodsItem.setInterferePrice("0");
            goodsItem.setOrderId("");
            goodsItem.setRemark(mRemarkList);
            goodsItem.setSalesId(mDishesCompItem.getDishesId());
            goodsItem.setSalesName(mDishesCompItem.getDishesName());
            goodsItem.setSalesNum(mDishesCompItem.getDishesNum());
            goodsItem.setSalesPrice("0");
            goodsItem.setSalesState("1");  //0稍后下单  1立即下单
            goodsItem.setIsCompDish("" + true); //套餐菜固定为true
            goodsItem.setAction("1");
            goodsItem.setIsZdzk(mDishesCompItem.getIsZdzk()); //整单折扣
            goodsItem.setMemberPrice(mDishesCompItem.getMemberPrice()); //会员价

            curGoodsItemList.add(goodsItem);
            chk_isItemSelect.setChecked(true);
        }
    }

    /**
     * 将属性一属性对象的json字符串形式保存
     *
     * @param mDishesPropertyChoice
     * @return
     */
    private List<String> updateOrderGoodsRemarkTypeObj(List<PropertySelectEntity> mDishesPropertyChoice) {
        List<String> remarkList = new ArrayList<String>();
        Gson gson = new Gson();
        for (int m = 0; m < mDishesPropertyChoice.size(); m++) {
            PropertySelectEntity psEntity = mDishesPropertyChoice.get(m);
            String propertyItem = gson.toJson(psEntity);
            remarkList.add(propertyItem);
        }

        return remarkList;
    }

    /**
     * 处理chk的选中状态事件
     *
     * @param isChecked
     * @param dishesId
     */
    private void onDishesCompCheckedChange(int position, Boolean isChecked, String dishesId) {
        Boolean isExist = false;

        for (Iterator<OrderGoodsItem> iter = curGoodsItemList.iterator(); iter.hasNext(); ) {
            OrderGoodsItem goodsItem = iter.next();
            if (goodsItem.getSalesId().equals(dishesId)) {
                if (isChecked) {
                } else {
                    iter.remove();
                }
                isExist = true;
                break;
            }
        }
        //如果尚未选择且变为选中状态，则添加到当选菜品中
        if (!isExist && isChecked) {
            addDishesToCurSelectedList(dishesId);
        }

        mItemCheckedStateMap.put(dishesId, isChecked);
        selectedPos = position;
        notifyDataSetChanged();
    }

    private void addDishesToCurSelectedList(String dishesId) {
        for (int m = 0; m < mDataList.size(); m++) {
            DishesCompItem compItem = mDataList.get(m);
            if (compItem.getDishesId().equals(dishesId)) {
                OrderGoodsItem goodsItem = new OrderGoodsItem();
                goodsItem.setCompId(DISHES_ID); //套餐菜统一设置成对应套餐的dishesId
                goodsItem.setTradeStaffId(mLoginUserPrefData.getStaffId());
                goodsItem.setDeskId(mCurDesk.getDeskId());
                goodsItem.setDishesPrice(compItem.getDishesPrice());
                goodsItem.setDishesTypeCode(compItem.getDishesTypeCode());
                goodsItem.setExportId(compItem.getExportId());
                goodsItem.setInstanceId("" + System.currentTimeMillis());
                goodsItem.setInterferePrice("0");
                goodsItem.setOrderId("");
                //获取默认的属性值
                goodsItem.setRemark(getDefPropertyChoice(compItem));
                goodsItem.setSalesId(compItem.getDishesId());
                goodsItem.setSalesName(compItem.getDishesName());
                goodsItem.setSalesNum(compItem.getDishesNum());
                goodsItem.setSalesPrice("0");
                goodsItem.setSalesState("1");  //0稍后下单  1立即下单
                goodsItem.setIsCompDish("" + true);  //套餐菜固定为true
                goodsItem.setAction("1");
                goodsItem.setIsZdzk(compItem.getIsZdzk()); //整单折扣
                goodsItem.setMemberPrice(compItem.getMemberPrice()); //会员价

                curGoodsItemList.add(goodsItem);
            }
        }
    }

    /**
     * 获取套餐子菜默认的属性值组合
     *
     * @param compItem
     * @return
     */
    private List<String> getDefPropertyChoice(DishesCompItem compItem) {
        List<String> defPropertyChoice = new ArrayList<String>();
        Gson gson = new Gson();
        List<DishesProperty> propList = compItem.getDishesItemTypelist();
        if (propList != null && propList.size() > 0) {
            for (int m = 0; m < propList.size(); m++) {
                DishesProperty property = propList.get(m);
                PropertySelectEntity propertyEntity = new PropertySelectEntity();
                propertyEntity.setItemType(property.getItemType());
                List<DishesPropertyItem> propItemList = new ArrayList<DishesPropertyItem>();
                if (property.getItemlist() != null && property.getItemlist().size() > 0) {
                    propItemList.add(property.getItemlist().get(0)); //默认选中第一项
                }
                propertyEntity.setmSelectedItemsList(propItemList);
                String remarkItem = gson.toJson(propertyEntity);
                defPropertyChoice.add(remarkItem);
            }
        }

        return defPropertyChoice;
    }

    public void setSelectedPos(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        }

        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mDataList != null) {
            return mDataList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    public void onRefresh(List<DishesCompItem> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    /**
     * 获取套餐菜的选中状态
     *
     * @return
     */
    public Map<String, Boolean> getDishesCompItemCheckedStateMap() {
        return mItemCheckedStateMap;
    }

    /**
     * 获取选中的套餐数据
     *
     * @return
     */
    public List<OrderGoodsItem> getSelectedCompItemData() {
        return curGoodsItemList;
    }

    static class ViewHolder {
        LinearLayout ll_propertyItems;
        TextView tv_dishCompName, tv_propertyName, tv_property;
        CheckBox chk_isItemSelect;
        //		CheckButton chk_isItemSelect;
        ImageView img_dropDown;
        Button btn_ensure;
    }

    public void onRefreshPartionList(List<DishesComp> mDishCompsPartionDataList) {
       this.mDishCompsPartionDataList = mDishCompsPartionDataList;
    }
}
