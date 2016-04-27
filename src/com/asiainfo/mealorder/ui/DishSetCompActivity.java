package com.asiainfo.mealorder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.DishCompItemAdapter;
import com.asiainfo.mealorder.adapter.DishTypeAdapter;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.LoginUserPrefData;
import com.asiainfo.mealorder.entity.DishesComp;
import com.asiainfo.mealorder.entity.DishesCompItem;
import com.asiainfo.mealorder.entity.DishesProperty;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.MerchantDesk;
import com.asiainfo.mealorder.entity.MerchantDishes;
import com.asiainfo.mealorder.entity.OrderGoodsItem;
import com.asiainfo.mealorder.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.asiainfo.mealorder.ui.base.MakeOrderActivityBase;
import com.asiainfo.mealorder.widget.base.SimpleSectionedListAdapter;
import com.asiainfo.mealorder.widget.base.SimpleSectionedListAdapter.Section;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ouyang
 *         <p/>
 *         2015年6月27日
 *         <p/>
 *         选择套餐组合页面
 */
public class DishSetCompActivity extends MakeOrderActivityBase {
    private View mView;
    private Button btn_back;
    private TextView tv_headTitle, tv_dishCompPrice;
    private ListView lv_dishCompPartions; //套餐部分列表
    private ListView lv_mDishCompPartionItems; //套餐子项列表
    private RelativeLayout rl_operationArea;
    private Button btn_chooseDone;

    //套餐部分数据
    private MerchantDesk mCurDesk;
    private String DISHES_ID = null;
    private String DISHES_NAME = null;
    private String DISHES_TYPE_CODE = null;
    private String DISHES_PRICE = null;
    private String childMerchantId = null;
    private LoginUserPrefData mLoginUserPrefData;
    private List<DishesComp> mDishCompsPartionDataList;
    private DishTypeAdapter<DishesComp> mDishCompPartionAdapter;

    //套餐子项数据
    private List<DishesCompItem> mDishCompsPartionItemsDataList;
    private DishCompItemAdapter mDishCompPartionItemsAdapter;
    private SimpleSectionedListAdapter mSimpleSectionedGridAdapter;
    private String[] mHeaderNames;
    private Integer[] mHeaderPositions;
    private ArrayList<Section> sections = new ArrayList<Section>();
    private Map<String, String> maxSelectMap = new HashMap<String, String>();

    /**
     * 当前套餐的套餐数据
     **/
    private List<DishesCompItem> defDishesCompItemList;
    private List<OrderGoodsItem> curGoodsItemList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_dish_comps_items);
        if (getIntent() != null) {
            Object obj = getIntent().getSerializableExtra("CUR_DESK");
            if (obj != null) {
                mCurDesk = (MerchantDesk) obj;
            }
            DISHES_ID = getIntent().getStringExtra("DISHES_ID");
            DISHES_NAME = getIntent().getStringExtra("DISHES_NAME");
            DISHES_PRICE = getIntent().getStringExtra("DISHES_PRICE");
            DISHES_TYPE_CODE = getIntent().getStringExtra("DISHES_TYPE_CODE");
            childMerchantId = getIntent().getStringExtra("CHIDLMERCHANTID");
        }
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "initView");
    }

    public void initView() {
        Log.d(TAG, "initView");
        mView = (RelativeLayout) findViewById(R.id.main_content_view);

        btn_back = (Button) findViewById(R.id.btn_back);
        tv_headTitle = (TextView) findViewById(R.id.tv_dish_comp_name);
        tv_dishCompPrice = (TextView) findViewById(R.id.tv_dish_comp_price);

        rl_operationArea = (RelativeLayout) findViewById(R.id.rl_bottom);
        setOperationArea(rl_operationArea);
        lv_dishCompPartions = (ListView) findViewById(R.id.lv_dish_comp_partions);
        lv_dishCompPartions.setOnTouchListener(mOnTouchListener);
        btn_chooseDone = (Button) findViewById(R.id.btn_ensure_comps);
        lv_mDishCompPartionItems = (ListView) findViewById(R.id.pslv_dish_comp_items);
        lv_mDishCompPartionItems.setOnTouchListener(mOnTouchListener);
    }

    public void initData() {
        Log.d(TAG, "initData");
        tv_headTitle.setText(DISHES_NAME);
        tv_dishCompPrice.setText("￥" + DISHES_PRICE);
        mLoginUserPrefData = new LoginUserPrefData(mActivity);
        //左侧套餐组成部分列表数据准备
        mDishCompsPartionDataList = new ArrayList<DishesComp>();
        //右侧套餐组合部分列表数据准备
        mDishCompsPartionItemsDataList = new ArrayList<DishesCompItem>();
        //右侧已选套餐子菜数据
        defDishesCompItemList = new ArrayList<DishesCompItem>();
        if (curGoodsItemList == null) {
            curGoodsItemList = new ArrayList<OrderGoodsItem>();
        }

        mDishCompPartionAdapter = new DishTypeAdapter<DishesComp>(DishSetCompActivity.this,
                mDishCompsPartionDataList, 0, mOnDishCompPartionItemClickListener);
        lv_dishCompPartions.setAdapter(mDishCompPartionAdapter);

        //汇集所有套餐子项
        unionDishCompPartionItemsData();
        mDishCompPartionItemsAdapter = new DishCompItemAdapter(DishSetCompActivity.this, DishSetCompActivity.this, mDishCompsPartionItemsDataList,
                curGoodsItemList, mOnDishCompPartionItemsItemClickListener, maxSelectMap, mDishCompsPartionDataList);
        mDishCompPartionItemsAdapter.setOnPropertyDropDownClickListener(mOnPropertyDropDownClickListener);
        mSimpleSectionedGridAdapter = new SimpleSectionedListAdapter(this, mDishCompPartionItemsAdapter,
                R.layout.lvitem_dishcomp_header, R.id.tv_dish_comp_partion_name);
        mDishCompPartionItemsAdapter.setCurDesk(mCurDesk);
        mDishCompPartionItemsAdapter.setDishesId(DISHES_ID);
        mSimpleSectionedGridAdapter.setSections(sections.toArray(new Section[0]));
        lv_mDishCompPartionItems.setAdapter(mSimpleSectionedGridAdapter);

        mDishCompsPartionDataList = sqliteGetDishesCompDataByDishesId2(DISHES_ID);
        if (mDishCompsPartionDataList.size() > 0) {
            getDishesCompData(mDishCompsPartionDataList);
        } else {
            showCommonDialog("正在加载套餐详情···");
            httpGetDishesCompItemsData2(DISHES_ID);
        }
    }

    /**
     * 汇集所有套餐组合项
     */
    public void unionDishCompPartionItemsData() {
        for (int m = 0; m < mDishCompsPartionDataList.size(); m++) {
            List<DishesCompItem> dishesInfoList = mDishCompsPartionDataList.get(m).getDishesInfoList();
            String maxSelect = mDishCompsPartionDataList.get(m).getMaxSelect();
            String typeId = mDishCompsPartionDataList.get(m).getDishesType();
            maxSelectMap.put(typeId, maxSelect);
            int selectedCount = 0;
            if (dishesInfoList != null && dishesInfoList.size() > 0) {
                //套餐子菜默认选择
                for (int k=0; k<dishesInfoList.size(); k++) {
                    if (selectedCount < Integer.valueOf(maxSelect)) {
                        defDishesCompItemList.add(dishesInfoList.get(selectedCount));
                        selectedCount ++;
                    }
                }

            }
            //Log.d(TAG, "defDishesCompItemList size: " + defDishesCompItemList.size());
        }

        //汇集所有套餐子项
        for (int j = 0; j < mDishCompsPartionDataList.size(); j++) {
            mDishCompsPartionItemsDataList.addAll(mDishCompsPartionDataList.get(j).getDishesInfoList());
        }

        mHeaderNames = new String[mDishCompsPartionDataList.size()];  //套餐组成标题
        mHeaderPositions = new Integer[mDishCompsPartionDataList.size()]; //套餐组成标题位置
        int countSum = 0;
        for (int i = 0; i < mDishCompsPartionDataList.size(); i++) {
            DishesComp dc = mDishCompsPartionDataList.get(i);
            String headName = dc.getDishesTypeName() + "(" + dc.getMaxSelect() + "/" + dc.getDishesCount() + ")";
            mHeaderNames[i] = headName;
            int count = dc.getDishesInfoList() == null ? 0 : dc.getDishesInfoList().size();
            mHeaderPositions[i] = countSum;
            countSum = countSum + count;
        }
        //右侧套餐组合详情
        for (int i = 0; i < mHeaderPositions.length; i++) {
            sections.add(new Section(mHeaderPositions[i], mHeaderNames[i]));
        }
    }

    /**
     * 套餐组合部分点击事件
     */
    public OnItemClickListener mOnDishCompPartionItemsItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            mDishCompPartionItemsAdapter.setSelectedPos(position);
        }
    };

    /**
     * 套餐组合项点击事件
     */
    private OnItemClickListener mOnDishCompPartionItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            mDishCompPartionAdapter.setSelectedPos(position);
        }
    };

    /**
     * 套餐属性项下拉框点击事件，套餐菜属性暂时不考虑
     */
    public OnItemClickListener mOnPropertyDropDownClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            DishesCompItem dcItem = mDishCompsPartionItemsDataList.get(position);
            List<DishesProperty> pList = dcItem.getDishesItemTypelist();
            if (pList != null && pList.size() > 0) {
                Log.d(TAG, "选择套餐页面， 选择套餐页面属性");
                DishesProperty dp = pList.get(0);
                ChoosePropertyValueDF mChoosePropertyValueDF = ChoosePropertyValueDF.newInstance(Constants.CHOOSE_PROPERTY_DIALOG_FROM_DISHES_COMP, dp);
                mChoosePropertyValueDF.show(getSupportFragmentManager(), "df_choose_property_value");
            }
        }
    };

    //private static final int ACT_RES_CHOOSE_COMPS_REQ = 1;
    private static final int ACT_RES_CHOOSE_COMPS_RESP = 1;

    public void initListener() {
        Log.d(TAG, "initListener");
        btn_chooseDone.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDishCompsPartionDataList != null && mDishCompsPartionDataList.size() > 0) {
                    DishesCompSelectionEntity mDishesCompSelectionEntity = updateDishesCompSelection();  //选好时，直接获取当前选中的所有菜，覆盖现有选中菜
                    Gson gson = new Gson();
                    String curGoodsListJson = gson.toJson(mDishesCompSelectionEntity);
                    Intent intent = new Intent();
                    intent.putExtra("SELECTED_COMP_DISHES", curGoodsListJson);
                    Log.d(TAG, "curGoodsListJson: " + curGoodsListJson);
                    setResult(ACT_RES_CHOOSE_COMPS_RESP, intent);
                    finish();
                } else {
                    showShortTip("套餐数据有误,请确认后重试!");
                }
            }
        });

        btn_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 从本地数据库中获取套餐菜数据
     */
    private void getDishesCompData(List<DishesComp> tempList) {
        mDishCompsPartionDataList = tempList;
        mDishCompPartionAdapter.onRefresh(mDishCompsPartionDataList);
        unionDishCompPartionItemsData(); //汇集所有套餐子项数据
        mSimpleSectionedGridAdapter.onRefreshSection(sections.toArray(new Section[0]));
        mDishCompPartionItemsAdapter.onRefreshDefCompItemList(defDishesCompItemList);
        mDishCompPartionItemsAdapter.onRefresh(mDishCompsPartionItemsDataList);
        mDishCompPartionItemsAdapter.onRefreshPartionList(mDishCompsPartionDataList);
    }

    /**
     * 更新选中的菜品列表
     * 处理逻辑是，先从adapter中获得选中的compItem对应的dishesId
     * 然后从全部的套餐数据中获取对应的菜，将DishesCompItem转换成OrderGoodsItem,
     * 最后用选中的OrderGoodsItemList直接覆盖之前的list, 返回
     */
    private DishesCompSelectionEntity updateDishesCompSelection() {
        DishesCompSelectionEntity mDishesCompSelectionEntity = new DishesCompSelectionEntity();
        MerchantDishes mMerchantDishes = getMerchantDishesById(DISHES_TYPE_CODE, DISHES_ID);
        //从数据库中解析出对应的套餐菜主项，添加到订单菜中
        String instanceId = System.currentTimeMillis() + "";
        curGoodsItemList = mDishCompPartionItemsAdapter.getSelectedCompItemData(); //覆盖当前选中的数据,节省内存
        if (curGoodsItemList != null && curGoodsItemList.size() > 0) {
            for (int i = 0; i < curGoodsItemList.size(); i++) {
                OrderGoodsItem goodsItem = curGoodsItemList.get(i);
                goodsItem.setInstanceId(instanceId);
                curGoodsItemList.set(i, goodsItem);
            }
        }
        mDishesCompSelectionEntity.setCompItemDishes(curGoodsItemList);

        //套餐主菜名
        if (mMerchantDishes != null) {
            OrderGoodsItem goodsItem = new OrderGoodsItem();
            goodsItem.setCompId("0");
            goodsItem.setTradeStaffId(mLoginUserPrefData.getStaffId());
            goodsItem.setDeskId(mCurDesk.getDeskId());
            goodsItem.setDishesPrice(mMerchantDishes.getDishesPrice());
            goodsItem.setDishesTypeCode(mMerchantDishes.getDishesTypeCode());
            goodsItem.setExportId(mMerchantDishes.getExportId());
            goodsItem.setInstanceId(instanceId);
            goodsItem.setInterferePrice("0");
            goodsItem.setOrderId("");
            List<String> remarkList = new ArrayList<String>();
            goodsItem.setRemark(remarkList);
            goodsItem.setSalesId(mMerchantDishes.getDishesId());
            goodsItem.setSalesName(mMerchantDishes.getDishesName());
            goodsItem.setSalesNum(1);
            goodsItem.setSalesPrice(mMerchantDishes.getDishesPrice());
            goodsItem.setSalesState("1");  //0稍后下单  1立即下单
            goodsItem.setIsCompDish("" + false); //固定填false
            goodsItem.setAction("1");
            goodsItem.setIsZdzk(mMerchantDishes.getIsZdzk()); //整单折扣
            goodsItem.setMemberPrice(mMerchantDishes.getMemberPrice()); //会员价

            mDishesCompSelectionEntity.setmCompMainDishes(goodsItem); //添加到第一项，从第一项取
        } else {
            Log.d(TAG, "套餐菜数据有误！");
        }

        return mDishesCompSelectionEntity;
    }


    /**
     * 根据套餐菜的dishesId获取套餐的数据
     *
     * @param dishesId
     */
    private void httpGetDishesCompItemsData1(final String dishesId) {
        String url = "/appController/queryComboInfoForApp.do?dishesId=" + dishesId + "&childMerchantId=" + childMerchantId;
        Log.d(TAG, HttpController.HOST + url);
        JsonObjectRequest httpGetDishesCompItemsData = new JsonObjectRequest(
                HttpController.HOST + url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        Log.d(TAG, "data: " + data);
                        try {
                            if (!data.equals("") && !data.equals("{}")) {
                                String dataStr = data.getString("compDishesTypeList");
                                if (dataStr != null) {
                                    Gson gson = new Gson();
                                    List<DishesComp> mDishesCompList = gson.fromJson(dataStr, new TypeToken<List<DishesComp>>() {
                                    }.getType());
                                    //Log.d(TAG, "mDishCompsPartionDataList size: " + mDishesCompList.size());
                                    dismissCommonDialog();
                                    getDishesCompData(mDishesCompList);
                                    if (mDishesCompList != null) {
                                        for (int i = 0; i < mDishesCompList.size(); i++) {
                                            DishesComp dishesComp = mDishesCompList.get(i);
                                            dishesComp.setDishesId(dishesId);
                                            mDishesCompList.set(i, dishesComp);
                                        }

                                        DataSupport.saveAll(mDishesCompList);
                                        for (int i = 0; i < mDishesCompList.size(); i++) {
                                            DishesComp dishesComp = mDishesCompList.get(i);
                                            List<DishesCompItem> mCompItemsList = dishesComp.getDishesInfoList();
                                            DataSupport.saveAll(mCompItemsList);
                                            for (DishesCompItem dishesCompItem : mCompItemsList) {
                                                List<DishesProperty> dpList = dishesCompItem.getDishesItemTypelist();
                                                if (dpList != null && dpList.size() > 0) {

                                                    for (int j = 0; j < dpList.size(); j++) {
                                                        DishesProperty dpItem = dpList.get(j);
                                                        dpItem.setIsCompProperty("1");
                                                        dpItem.setDishesId(dishesCompItem.getDishesId());
                                                        dpList.set(j, dpItem);
                                                        DataSupport.saveAll(dpList); //缓存菜品属性类型数据
                                                        List<DishesPropertyItem> dpiList = dpItem.getItemlist();
                                                        for (int m = 0; m < dpiList.size(); m++) {
                                                            DishesPropertyItem item = dpiList.get(m);
                                                            item.setIsCompProperty("1");
                                                            item.setDishesId(dishesCompItem.getDishesId());
                                                            item.setItemType(dpItem.getItemType());
                                                            dpiList.set(m, item);
                                                        }
                                                        DataSupport.saveAll(dpiList); //缓存菜品属性值数据
                                                    }
                                                }
                                            }
                                        }
                                        Log.d(TAG, "DishesId = " + dishesId + " 同步套餐数据完成!");
                                    }
                                } else {
                                    dismissCommonDialog();
                                    Log.d(TAG, "套餐数据有误,请后台确认!");
                                    showShortTip("套餐数据有误,请后台确认!");
                                }
                            } else {
                                dismissCommonDialog();
                                showShortTip("套餐数据有误,请后台确认!");
                                Log.d(TAG, "套餐数据有误,请后台确认!");
                            }
                        } catch (JSONException e) {
                            dismissCommonDialog();
                            showShortTip("套餐数据有误,请后台确认!");
                            Log.d(TAG, "套餐数据有误,json解析错误!");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissCommonDialog();
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                    }
                });

        executeRequest(httpGetDishesCompItemsData);
    }

    /**
     * 根据套餐菜的dishesId获取套餐的数据
     * @param dishesId
     */
    private void httpGetDishesCompItemsData2(final String dishesId) {
        HttpController.getInstance().getDishesCompItemsData(childMerchantId,dishesId,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        Log.d(TAG, "data: " + data);
                        try {
                            if (!data.equals("") && !data.equals("{}")) {
                                String dataStr = data.getString("compDishesTypeList");
                                if (dataStr != null) {
                                    Gson gson = new Gson();
                                    List<DishesComp> mDishesCompList = gson.fromJson(dataStr, new TypeToken<List<DishesComp>>() {
                                    }.getType());
                                    //Log.d(TAG, "mDishCompsPartionDataList size: " + mDishesCompList.size());
                                    dismissCommonDialog();
                                    getDishesCompData(mDishesCompList);
                                    if (mDishesCompList != null) {
                                        for (int i = 0; i < mDishesCompList.size(); i++) {
                                            DishesComp dishesComp = mDishesCompList.get(i);
                                            dishesComp.setDishesId(dishesId);
                                            mDishesCompList.set(i, dishesComp);
                                        }

                                        DataSupport.saveAll(mDishesCompList);
                                        for (int i = 0; i < mDishesCompList.size(); i++) {
                                            DishesComp dishesComp = mDishesCompList.get(i);
                                            List<DishesCompItem> mCompItemsList = dishesComp.getDishesInfoList();
                                            DataSupport.saveAll(mCompItemsList);
                                            for (DishesCompItem dishesCompItem : mCompItemsList) {
                                                List<DishesProperty> dpList = dishesCompItem.getDishesItemTypelist();
                                                if (dpList != null && dpList.size() > 0) {

                                                    for (int j = 0; j < dpList.size(); j++) {
                                                        DishesProperty dpItem = dpList.get(j);
                                                        dpItem.setIsCompProperty("1");
                                                        dpItem.setDishesId(dishesCompItem.getDishesId());
                                                        dpList.set(j, dpItem);
                                                        DataSupport.saveAll(dpList); //缓存菜品属性类型数据
                                                        List<DishesPropertyItem> dpiList = dpItem.getItemlist();
                                                        for (int m = 0; m < dpiList.size(); m++) {
                                                            DishesPropertyItem item = dpiList.get(m);
                                                            item.setIsCompProperty("1");
                                                            item.setDishesId(dishesCompItem.getDishesId());
                                                            item.setItemType(dpItem.getItemType());
                                                            dpiList.set(m, item);
                                                        }
                                                        DataSupport.saveAll(dpiList); //缓存菜品属性值数据
                                                    }
                                                }
                                            }
                                        }
                                        Log.d(TAG, "DishesId = " + dishesId + " 同步套餐数据完成!");
                                    }
                                } else {
                                    dismissCommonDialog();
                                    Log.d(TAG, "套餐数据有误,请后台确认!");
                                    showShortTip("套餐数据有误,请后台确认!");
                                }
                            } else {
                                dismissCommonDialog();
                                showShortTip("套餐数据有误,请后台确认!");
                                Log.d(TAG, "套餐数据有误,请后台确认!");
                            }
                        } catch (JSONException e) {
                            dismissCommonDialog();
                            showShortTip("套餐数据有误,请后台确认!");
                            Log.d(TAG, "套餐数据有误,json解析错误!");
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissCommonDialog();
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                    }
                });
    }
}