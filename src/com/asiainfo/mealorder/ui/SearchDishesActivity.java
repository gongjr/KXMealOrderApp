package com.asiainfo.mealorder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.biz.adapter.SearchDishesAdapter;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.db.MerchantDishesEntityService;
import com.asiainfo.mealorder.biz.entity.MerchantDesk;
import com.asiainfo.mealorder.biz.entity.MerchantDishes;
import com.asiainfo.mealorder.biz.entity.MerchantRegister;
import com.asiainfo.mealorder.biz.entity.OrderGoodsItem;
import com.asiainfo.mealorder.biz.entity.OrderSubmit;
import com.asiainfo.mealorder.biz.entity.eventbus.EventMain;
import com.asiainfo.mealorder.biz.entity.eventbus.post.OrderListEntity;
import com.asiainfo.mealorder.biz.entity.helper.DishesCompSelectionEntity;
import com.asiainfo.mealorder.biz.entity.helper.PropertySelectEntity;
import com.asiainfo.mealorder.biz.listener.OnChangeDishCountListener;
import com.asiainfo.mealorder.biz.listener.OnItemClickListener;
import com.asiainfo.mealorder.biz.listener.OnSearchDishesCompUpdateListener;
import com.asiainfo.mealorder.ui.base.BaseActivity;
import com.asiainfo.mealorder.utils.Arith;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectView;

/**
 * @author gjr
 *
 * 2015年8月25日
 *
 * 搜索菜品点餐页面
 */
public class SearchDishesActivity extends BaseActivity{
    @InjectView(R.id.search_top_num)
    private TextView order_num;
    @InjectView(R.id.search_top_total)
    private TextView order_total;
    @InjectView(R.id.edit_content)
	private EditText edit_searchInput;
    @InjectView(R.id.btn_cancel)
    private TextView tv_cancel;
    @InjectView(R.id.tv_search_desc)
    private TextView tv_searchDesc;
    @InjectView(R.id.lv_search_result_dishes)
	private ListView lv_searchResultDishes;

	private MerchantDishesEntityService mMerchantDishesEntityService;
	private SearchDishesAdapter mSearchDishesAdapter;
	private List<MerchantDishes> mDishesList;
    private List<OrderGoodsItem> mNormalDishDataList;
    private List<DishesCompSelectionEntity> mDishesCompDataList;
    private OrderSubmit mOrderSubmit;
    private MerchantDesk mCurDesk;
    private int ORDER_CONFIRM_TYPE = Constants.ORDER_CONFIRM_TYPE_NEW_ORDER;
    private String deskOrderPrice = "0";
    private MerchantRegister merchantRegister;

	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_search_dishes);
		initData();
		initListener();
	}

	private void initData(){
        //将相关数据传过来,与confirmorder页面相同
        Bundle mBundle = getIntent().getBundleExtra("DATA_BUNDLE");
        mOrderSubmit = (OrderSubmit)mBundle.getSerializable("ORDER_SUBMIT");
        mCurDesk = (MerchantDesk)mBundle.getSerializable("MERCHANT_DESK");
        String dishesCompJsonStr = mBundle.getString("ORDER_DISHES_COMP");
        deskOrderPrice = mBundle.getString("DESK_ORDER_PRICE");
        ORDER_CONFIRM_TYPE = mBundle.getInt("ORDER_CONFIRM_TYPE");
        mDishesCompDataList = gson.fromJson(dishesCompJsonStr, new TypeToken<List<DishesCompSelectionEntity>>() {
        }.getType());
        mNormalDishDataList=mOrderSubmit.getOrderGoods();
        merchantRegister=(MerchantRegister)baseApp.gainData(baseApp.KEY_GLOABLE_LOGININFO);
        updateViewWithNumAndTotal();

		setSearchDescValue("");
        edit_searchInput.setFocusable(true);
        edit_searchInput.requestFocus();
        InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mMerchantDishesEntityService = new MerchantDishesEntityService();
		mDishesList = new ArrayList<MerchantDishes>();
		mSearchDishesAdapter = new SearchDishesAdapter(this, mActivity, mOnItemClickListener, mDishesList);
		mSearchDishesAdapter.setOnChangeDishesSimpCountListener(mOnChangeDishCountListener); 
		mSearchDishesAdapter.setOnSearchDishesCompUpdateListener(mOnSearchDishesCompUpdateListener);
		lv_searchResultDishes.setAdapter(mSearchDishesAdapter);
	}
	
	private void initListener(){
		edit_searchInput.addTextChangedListener(searchWatcher);
		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

                if((mOrderSubmit==null || mOrderSubmit.getOrderGoods()==null
                        || mOrderSubmit.getOrderGoods().size()==0) && (mNormalDishDataList==null || mNormalDishDataList.size()==0) ){
                    if(mDishesCompDataList==null||mDishesCompDataList.size()==0){
                        showShortTip("还没有点菜哦~");
                        return;
                    }
                }
                mOrderSubmit.setOrderGoods(mNormalDishDataList);
                String dishesCompJsonStr =  gson.toJson(mDishesCompDataList);
                Intent intent = new Intent(SearchDishesActivity.this, ConfirmOrderActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("ORDER_SUBMIT", mOrderSubmit);
                mBundle.putSerializable("MERCHANT_DESK", mCurDesk);
                mBundle.putString("ORDER_DISHES_COMP", dishesCompJsonStr);
                mBundle.putString("DESK_ORDER_PRICE", deskOrderPrice);
                mBundle.putInt("ORDER_CONFIRM_TYPE", ORDER_CONFIRM_TYPE);
                intent.putExtra("DATA_BUNDLE", mBundle);
                startActivityForResult(intent, Constants.ACT_RES_CONFIRM_BACK_REQ);
			}
		});
	}
	
	TextWatcher searchWatcher = new TextWatcher() {
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
			if(hint.length()!=0){
                //"'",英文的单引号会报错,需要在此处过滤
                String last=hint.substring(hint.length()-1,hint.length());
                KLog.i("last:"+last);
                if(last.equals("'")){
                    edit_searchInput.setText("");
                    showShortTip("无法输入符号搜索,请重新输入!");
                }else{
                    setSearchDescValue(hint);
                    updateSearchedDishes(hint);
                }
			}else{
                setSearchDescValue("");
                mDishesList.clear();
                mSearchDishesAdapter.onRefreshData(mDishesList);
            }
		}
	};
	
	private void setSearchDescValue(String searchValue){
		tv_searchDesc.setText(Html.fromHtml(String.format(mRes.getString(R.string.search_dishes_hint), searchValue)));
	}
	
	private void updateSearchedDishes(String searchInfo){
		mDishesList = mMerchantDishesEntityService.queryAllMerchantDishesByInfo(searchInfo);
		mSearchDishesAdapter.onRefreshData(mDishesList);
        KLog.i("刷新");
	}
	
	/**
	 * 所有菜点击事件
	 */
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(View view, int position) {
			mSearchDishesAdapter.setSelectedPos(position);
		}
	};
	
	private OnSearchDishesCompUpdateListener mOnSearchDishesCompUpdateListener = new OnSearchDishesCompUpdateListener() {
		@Override
		public void onDishesCompUpdate(String dishesTypeCode, String dishesId,
				String dishesName, String dishesPrice) {
            getDishesCompDetail(dishesTypeCode, dishesId, dishesName, dishesPrice);
		}
	};
	
	/**
	 * 点菜下单， 点击改变点菜数量的按钮监听器
	 */
	private OnChangeDishCountListener mOnChangeDishCountListener = new OnChangeDishCountListener() {
		@Override
		public void onChangeCount(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice) {
			updateOrderGoodsListData(dishesItem, selectedCount, mDishesPropertyChoice);
		}
	};

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            EventMain eventOrder =new EventMain();
            OrderListEntity orderListEntity=new OrderListEntity();
            orderListEntity.setmDishesCompDataList(mDishesCompDataList);
            orderListEntity.setmNormalDishDataList(mNormalDishDataList);
            eventOrder.setData(orderListEntity);
            eventOrder.setType(EventMain.TYPE_THREE);
            eventOrder.setDescribe("搜索点餐页面返回键触发的时候，需要将订单修改带回去刷新");
            eventOrder.setName(MakeOrderActivity.class.getName());
            EventBus.getDefault().post(eventOrder);
        }
        return  super.onKeyDown(keyCode, event);
    }

    /**
     * 根据当前点的菜，更新订单所点菜品信息
     * @param dishesItem
     * @param selectedCount
     */
    private void updateOrderGoodsListData(MerchantDishes dishesItem, int selectedCount, List<PropertySelectEntity> mDishesPropertyChoice){
        Boolean isExist = false;
        //(1)先判断修改,没有属性的菜,直接增加数量
        for(int i=0; i<mNormalDishDataList.size(); i++){
            OrderGoodsItem goodsItem = mNormalDishDataList.get(i);
            String gitc = goodsItem.getDishesTypeCode();
            String ditc = dishesItem.getDishesTypeCode();
            String gidid = goodsItem.getSalesId();
            String didid = dishesItem.getDishesId();

            boolean hasProp = true;
            if(goodsItem.getRemark()==null || goodsItem.getRemark().size()==0){
                hasProp = false;
                //没有属性细项标签
            }
            /**
             * 如果该菜在所选菜中已经存在，并且没有属性，则直接设置数量
             */
            if(gitc!=null && ditc!=null && gitc.equals(ditc)
                    && gidid!=null && didid!=null && gidid.equals(didid) && !hasProp){
                int sum=selectedCount+goodsItem.getSalesNum();
                goodsItem.setSalesNum(sum);
                goodsItem.setSalesPrice(""+ Arith.d2str(sum* StringUtils.str2Double(goodsItem.getDishesPrice())));
                List<String> remarkList = updateOrderGoodsRemarkTypeObj(mDishesPropertyChoice);
                goodsItem.setRemark(remarkList);
                mNormalDishDataList.set(i, goodsItem);
                isExist = true;
                break;
            }
        }

        /**
         * (2)
         * 如果该菜没有选过，或者选过，但是有属性，则重新添加一份,数量根据传入的而定
         * isExist为true,表示已经处理过了,需要忽略
         */
        if(!isExist){
            OrderGoodsItem goodsItem = new OrderGoodsItem();
            goodsItem.setCompId("0");  //非套餐
            goodsItem.setTradeStaffId(mOrderSubmit.getTradeStsffId());
            goodsItem.setDeskId(mCurDesk.getDeskId());
            goodsItem.setDishesPrice(dishesItem.getDishesPrice());
            goodsItem.setDishesTypeCode(dishesItem.getDishesTypeCode());
            goodsItem.setExportId(dishesItem.getExportId());
            goodsItem.setInstanceId(""+System.currentTimeMillis());
            goodsItem.setInterferePrice("0");
            goodsItem.setOrderId("");

            List<String> remarkList = updateOrderGoodsRemarkTypeObj(mDishesPropertyChoice);
            goodsItem.setRemark(remarkList);
            goodsItem.setSalesId(dishesItem.getDishesId());
            goodsItem.setSalesName(dishesItem.getDishesName());
            goodsItem.setSalesNum(selectedCount);
            goodsItem.setSalesPrice(""+Arith.d2str(selectedCount*StringUtils.str2Double(dishesItem.getDishesPrice())));
            goodsItem.setSalesState("1");  //0稍后下单  1立即下单
            Boolean isComp = false;
            if(dishesItem.getIsComp()==1){ isComp=true; }
            goodsItem.setIsCompDish(""+isComp);
            goodsItem.setAction("1"); //订单操作类型，增删改
            goodsItem.setIsZdzk(dishesItem.getIsZdzk()); //是否参与整单折扣
            goodsItem.setMemberPrice(dishesItem.getMemberPrice()); //会员价

            mNormalDishDataList.add(goodsItem);
        }
        updateViewWithNumAndTotal();
        clearSearchContext();
    }

    /**
     * 将属性一属性对象的json字符串形式保存
     * @param mDishesPropertyChoice
     * @return
     */
    private List<String> updateOrderGoodsRemarkTypeObj(List<PropertySelectEntity> mDishesPropertyChoice){
        List<String> remarkList = new ArrayList<String>();
        for(int m=0; m<mDishesPropertyChoice.size(); m++){
            PropertySelectEntity  psEntity= mDishesPropertyChoice.get(m);
            String propertyItem = gson.toJson(psEntity);
            remarkList.add(propertyItem);
        }

        return remarkList;
    }

    /**
     * 获取套餐相关选项
     * @param dishesTypeCode
     * @param dishesId
     * @param dishesName
     * @param dishesPrice
     */
    private void getDishesCompDetail(String dishesTypeCode, String dishesId, String dishesName, String dishesPrice){
        Intent intent = new Intent(SearchDishesActivity.this, DishSetCompActivity.class);
        intent.putExtra("DISHES_TYPE_CODE", dishesTypeCode);
        intent.putExtra("DISHES_ID", dishesId);
        intent.putExtra("DISHES_NAME", dishesName);
        intent.putExtra("CUR_DESK", mCurDesk);
        intent.putExtra("DISHES_PRICE", dishesPrice);
        intent.putExtra("CHIDLMERCHANTID",merchantRegister.getChildMerchantId());
        startActivityForResult(intent, Constants.ACT_RES_CHOOSE_COMPS_REQ);
    }

    @Override
    protected void onActivityResult(int req, int resp, Intent intent) {
        super.onActivityResult(req, resp, intent);
        //选择套餐组合后返回
        if(req==Constants.ACT_RES_CHOOSE_COMPS_REQ && resp==Constants.ACT_RES_CHOOSE_COMPS_RESP){
            String curGoodsListJson = intent.getStringExtra("SELECTED_COMP_DISHES");
            Log.d(TAG, "curGoodsListJson: " + curGoodsListJson);
            if(curGoodsListJson!=null){
                DishesCompSelectionEntity mDishesCompSelectionEntity = gson.fromJson(curGoodsListJson,
                        DishesCompSelectionEntity.class);
                mDishesCompDataList.add(mDishesCompSelectionEntity);
                updateViewWithNumAndTotal();
                clearSearchContext();
            }
            InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

        //确认订单页面返回
        if(req==Constants.ACT_RES_CONFIRM_BACK_REQ && resp==Constants.ACT_RES_CONFIRM_BACK_RESP){
            String curCommonDishesJson = intent.getStringExtra("RETURNED_NORMAL_DISHES");
            String curCompDishesJson = intent.getStringExtra("RETURNED_COMP_DISHES");
            mNormalDishDataList = gson.fromJson(curCommonDishesJson, new TypeToken<List<OrderGoodsItem>>(){}.getType());
            mDishesCompDataList = gson.fromJson(curCompDishesJson, new TypeToken<List<DishesCompSelectionEntity>>(){}.getType());
            updateViewWithNumAndTotal();
            clearSearchContext();
            InputMethodManager imm = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 计算订单的商品总价格
     * @return
     */
    private String getOrderSubmitOriginalPrice(){
        //已有订单，在删菜后在加菜过程中，价格计算有误
        Double sumPrice=0.0;
        //计算普通菜价格
        for(int i=0; i<mNormalDishDataList.size(); i++){
            OrderGoodsItem goodsItem1 = mNormalDishDataList.get(i);
            sumPrice = sumPrice + StringUtils.str2Double(goodsItem1.getSalesPrice());
        }
        //计算套餐菜价格
        if(mDishesCompDataList!=null && mDishesCompDataList.size()>0){
            for(int j=0; j<mDishesCompDataList.size(); j++){
                DishesCompSelectionEntity dishesCompItem = mDishesCompDataList.get(j);
                OrderGoodsItem goodsItem2 = dishesCompItem.getmCompMainDishes();
                sumPrice = sumPrice + StringUtils.str2Double(goodsItem2.getSalesPrice());
            }
        }
        return Arith.d2str(sumPrice);
    }

    /**
     * 计算订单的商品总数
     * @return
     */
    private int getOrderSubmitAllGoodsNum(){
        int num = 0;
        for(int i=0; i<mNormalDishDataList.size(); i++){
            OrderGoodsItem goodsItem = mNormalDishDataList.get(i);
            num = num + goodsItem.getSalesNum();
        }
        if(mDishesCompDataList!=null && mDishesCompDataList.size()>0){
            num = num + mDishesCompDataList.size();
        }
        return num;
    }

    /**
     * 将数据变化更新视图
     */
    private void updateViewWithNumAndTotal(){
        order_num.setText(getOrderSubmitAllGoodsNum()+"");
        order_total.setText(getOrderSubmitOriginalPrice()+"元");
    }

    /**
     * 清空下搜索内容
     */
    private void clearSearchContext(){
        edit_searchInput.setText("");
        setSearchDescValue("");
        mDishesList.clear();
        mSearchDishesAdapter.onRefreshData(mDishesList);
        edit_searchInput.setFocusable(true);
        edit_searchInput.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(edit_searchInput,InputMethodManager.SHOW_IMPLICIT);
    }

}
