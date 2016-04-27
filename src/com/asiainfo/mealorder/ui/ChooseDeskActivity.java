package com.asiainfo.mealorder.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.asiainfo.mealorder.AppApplication;
import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.ChooseDeskAdapter;
import com.asiainfo.mealorder.config.Constants;
import com.asiainfo.mealorder.config.SystemPrefData;
import com.asiainfo.mealorder.entity.DeskOrder;
import com.asiainfo.mealorder.entity.DeskOrderGoodsItem;
import com.asiainfo.mealorder.entity.DishesComp;
import com.asiainfo.mealorder.entity.DishesCompItem;
import com.asiainfo.mealorder.entity.DishesProperty;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.entity.MerchantDesk;
import com.asiainfo.mealorder.entity.MerchantDeskLocation;
import com.asiainfo.mealorder.entity.MerchantDishes;
import com.asiainfo.mealorder.entity.MerchantDishesType;
import com.asiainfo.mealorder.entity.MerchantRegister;
import com.asiainfo.mealorder.entity.eventbus.EventBackground;
import com.asiainfo.mealorder.entity.eventbus.EventMain;
import com.asiainfo.mealorder.entity.eventbus.post.DishesListEntity;
import com.asiainfo.mealorder.entity.http.PublicDishesItem;
import com.asiainfo.mealorder.entity.http.QueryAppMerchantPublicAttr;
import com.asiainfo.mealorder.http.HttpController;
import com.asiainfo.mealorder.http.VolleyErrorHelper;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.asiainfo.mealorder.ui.ChooseDeskOrderDF.OnFinishChooseDeskOrderListener;
import com.asiainfo.mealorder.ui.InputOrderPersonNumDF.OnFinishInputNumListener;
import com.asiainfo.mealorder.ui.base.ChooseDeskActivityBase;
import com.asiainfo.mealorder.ui.base.EnsureDialogFragmentBase;
import com.asiainfo.mealorder.utils.KLog;
import com.asiainfo.mealorder.utils.StringUtils;
import com.asiainfo.mealorder.widget.ActionItem;
import com.asiainfo.mealorder.widget.EmptyLayout;
import com.asiainfo.mealorder.widget.QuickAction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.asiainfo.mealorder.widget.handmark.pulltorefresh.PullToRefreshBase;
import com.asiainfo.mealorder.widget.handmark.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.asiainfo.mealorder.widget.handmark.pulltorefresh.PullToRefreshGridView;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import roboguice.inject.InjectResource;

/**
 * 选桌台根页面
 * 单栈共享singleTask，在栈低，返回的时候，弹出销毁上面的activity
 * @author gjr
 *
 * 2015年6月27日
 */
public class ChooseDeskActivity extends ChooseDeskActivityBase{
	private TextView tv_waiter_info;
	private Button btn_exit;
	private RadioGroup grp_deskLoc;
    private HorizontalScrollView deskTypeLsitView;
	private PullToRefreshGridView mPullRefreshGridView;
	private GridView grid_deskItem;
	private ChooseDeskAdapter mChooseDeskAdapter;
    private EmptyLayout mEmptyLayout; 
    
    //全局逻辑变量
    private String staffId = null;
    private String staffName = null;
    private String childMerchantId = null;
    private MerchantDeskLocation mCurDeskLoc;  //暂存当前选中的桌子区域
    private int curIndex=0;
    private MerchantDesk mCurDesk;  //暂存当前选中的桌子
    private List<MerchantDeskLocation> mMerchantDeskLocList; //从服务器获取到的桌子数据
    private List<MerchantDesk> mDeskList;
    private SystemPrefData mSystemPrefData;
    private ArrayList<DeskOrder> mDeskOrderList;
    private AppApplication BaseApp;
    private QuickAction quickAction;
    private static final int ID_refresh     = 1;
    private static final int ID_localOrder   = 2;
    private static final int ID_exit   = 3;
    private List<MerchantDishesType> mDishTypeDataList;
    private List<MerchantDishes> mAllDishesDataList;
    @InjectResource(R.drawable.ic_actionitem_refresh)
    private Drawable ic_actionitem_refresh;
    @InjectResource(R.drawable.ic_actionitem_exit)
    private Drawable ic_actionitem_exit;
    @InjectResource(R.drawable.ic_actionitem_order)
    private Drawable ic_actionitem_order;
    private MerchantRegister merchantRegister;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
	    setContentView(R.layout.activity_choose_desk);
        EventBus.getDefault().register(this);
	    Bundle mBundle = getIntent().getExtras();
	    staffId = mBundle.getString("STAFF_ID");
	    staffName = mBundle.getString("STAFF_NAME");
	    childMerchantId = mBundle.getString("CHILD_MERCHANT_ID");
        BaseApp=(AppApplication)getApplication();
		initView();
		initData();
		initListener();
	}
	
	public void initView(){
		tv_waiter_info = (TextView)findViewById(R.id.tv_waiter_info);
		grp_deskLoc = (RadioGroup)findViewById(R.id.grp_desk_loc);
        deskTypeLsitView = (HorizontalScrollView)findViewById(R.id.hzv_desk_loc);
		mPullRefreshGridView = (PullToRefreshGridView) findViewById(R.id.grid_desks);
		grid_deskItem = mPullRefreshGridView.getRefreshableView();
		//grid_deskItem = (GridView)findViewById(R.id.grid_desks);
		btn_exit = (Button)findViewById(R.id.btn_exit);
		mEmptyLayout = new EmptyLayout(this, grid_deskItem);
	}

	public void initData(){
        merchantRegister=(MerchantRegister)BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);
        quickAction=new QuickAction(this, QuickAction.VERTICAL);

        ActionItem refreshDishesItem 	= new ActionItem(ID_refresh, "刷新菜单",ic_actionitem_refresh);
        ActionItem exitItem 	= new ActionItem(ID_exit, "退出登录", ic_actionitem_exit);
        ActionItem localOrder 	= new ActionItem(ID_localOrder, "本地订单", ic_actionitem_order);
        localOrder.setSticky(true);
        refreshDishesItem.setSticky(true);
        exitItem.setSticky(true);
        quickAction.addActionItem(refreshDishesItem);
        quickAction.addActionItem(localOrder);
        quickAction.addActionItem(exitItem);
        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override
            public void onItemClick(QuickAction source, int pos, int actionId) {
                ActionItem actionItem = quickAction.getActionItem(pos);
                if (actionId == ID_refresh) {
                    source.dismiss();
                    MerchantRegister merchantRegister=(MerchantRegister)BaseApp.gainData(BaseApp.KEY_GLOABLE_LOGININFO);
                    httpGetMerchantDishes2(childMerchantId,merchantRegister.getMerchantId());
                } else if (actionId == ID_exit) {
                    source.dismiss();
                    finish();
                }else if(actionId == ID_localOrder){
                    source.dismiss();
                    getOperation().forward(LocalOrderActivity.class);
                }
            }
        });

        quickAction.setOnDismissListener(new QuickAction.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });

        mSystemPrefData = new SystemPrefData(ChooseDeskActivity.this);
		tv_waiter_info.setText(Html.fromHtml(String.format(mRes.getString(R.string.waiter_info), staffId, staffName)));
		
		mPullRefreshGridView.setOnRefreshListener(new OnRefreshListener2<GridView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
				Log.d(TAG, "下拉刷新");
				httpGetLocDeskData();
			}
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
				Log.d(TAG, "上拉刷新");
				httpGetLocDeskData();
			}
        });
		mDeskList = new ArrayList<MerchantDesk>();
		mChooseDeskAdapter = new ChooseDeskAdapter(ChooseDeskActivity.this, mDeskList, -1, onDeskItemClickListener);
		grid_deskItem.setAdapter(mChooseDeskAdapter);
        initLocDeskData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
        if(mCurDeskLoc==null)
		mCurDeskLoc = new MerchantDeskLocation();
        if(mCurDesk==null)
		mCurDesk = new MerchantDesk();
        if(mMerchantDeskLocList==null)
		mMerchantDeskLocList = new ArrayList<MerchantDeskLocation>();
		mPullRefreshGridView.setRefreshing(true);
        //熄屏,home键后返回onRestart()->onStart()->onResume()
	}

    @Override
    protected void onRestart() {
        super.onRestart();
        KLog.i("onRestart  curIndex:"+curIndex);
        KLog.i("mCurDesk.getLocationCode():"+mCurDeskLoc.getLocationCode());
    }


    /**
     * 获取桌子区域和桌子数据
     */
    private void initLocDeskData(){
        onShowLoading();
        String url = "/appController/queryDeskLocation.do?childMerchantId="+childMerchantId;
        Log.d(TAG, "initLocDeskData: " + HttpController.HOST+url);
        JsonObjectRequest httpGetLocDeskData = new JsonObjectRequest(
                HttpController.HOST+url, null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        Log.d(TAG, "data: " + data);
                        try {
                            if(data.getString("msg").equals("ok")){
                                String info = data.getJSONObject("data").getString("info");
                                Gson gson = new Gson();
                                mMerchantDeskLocList = gson.fromJson(info,
                                        new TypeToken<List<MerchantDeskLocation>>(){}.getType());
                                initDeskLocData();
                            }else{
                                onShowEmpty();
                                showShortTip("桌子数据有误!");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mPullRefreshGridView.onRefreshComplete();
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mPullRefreshGridView.onRefreshComplete();
                        onShowError();
                        Log.e(TAG, "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                    }
                });
        executeRequest(httpGetLocDeskData);
    }

	/**
	 * 获取桌子区域和桌子数据
	 */
	private void httpGetLocDeskData(){
		String url = "/appController/queryDeskLocation.do?childMerchantId="+childMerchantId;
		Log.d(TAG, "httpGetLocDeskData: " + HttpController.HOST+url);
		JsonObjectRequest httpGetLocDeskData = new JsonObjectRequest(
				HttpController.HOST+url, null,
				new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject data) {
						Log.d(TAG, "data: " + data);
						try {
							if(data.getString("msg").equals("ok")){
								String info = data.getJSONObject("data").getString("info");
							    Gson gson = new Gson();
							    mMerchantDeskLocList = gson.fromJson(info, 
							    		new TypeToken<List<MerchantDeskLocation>>(){}.getType());
//							    initDeskLocData();
                                refreshDeskLocData();
							}else{
								showShortTip("桌子数据有误!");
							}
						} catch (JSONException e) {
                            showShortTip("桌子数据有误!");
							e.printStackTrace();
						}
						mPullRefreshGridView.onRefreshComplete();
					}
				}, 
				new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						mPullRefreshGridView.onRefreshComplete();
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
					}
				});
		executeRequest(httpGetLocDeskData);
	}
	
	/**
	 * 初始化桌子区域数据
	 */
	private void initDeskLocData(){
		if(mMerchantDeskLocList==null || mMerchantDeskLocList.size()<=0){
			showShortTip("该商户没有桌子数据!");
			return;
		}

		grp_deskLoc.removeAllViews();
		for(int i=0; i<mMerchantDeskLocList.size(); i++){
			MerchantDeskLocation mdl = mMerchantDeskLocList.get(i);
			LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			RadioButton rdo_deskLocItem = (RadioButton)mInflater.inflate(R.layout.desk_location_item, null);
			rdo_deskLocItem.setId(i);
			rdo_deskLocItem.setTag(mdl.getLocationCode()+"");
			rdo_deskLocItem.setText(mdl.getLocationName()+"");
            rdo_deskLocItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if(isChecked){
                        String mdl_code = (String)buttonView.getTag();
                        curIndex=buttonView.getId();
                        if(mMerchantDeskLocList!=null&&mMerchantDeskLocList.size()>=curIndex){
                        mCurDeskLoc=mMerchantDeskLocList.get(curIndex);
                        updateLocationDesks(mCurDeskLoc.getLocationCode());
                        }else{
                            KLog.i("mMerchantDeskLocList有问题");
                        }
                    }
                }
            });
            if(i==0){
                curIndex=0;
                rdo_deskLocItem.setChecked(true);
            }else{
                rdo_deskLocItem.setChecked(false);
            }
			grp_deskLoc.addView(rdo_deskLocItem, lp);
		}
    }

    /**
     * 刷新桌子区域数据
     */
    private void refreshDeskLocData(){
        if(mMerchantDeskLocList==null || mMerchantDeskLocList.size()<=0){
            showShortTip("该商户没有桌子数据!");
            return;
        }
        //先将上次的位置置换为此次的位置,没有的话就置为0
        for(int i=0; i<mMerchantDeskLocList.size(); i++){
            MerchantDeskLocation mdl = mMerchantDeskLocList.get(i);
            if(mdl.getLocationCode().equals(mCurDeskLoc.getLocationCode())){
                curIndex=i;//将旧的选中index置为新的位置
                break;
            }else{
                curIndex=0;//倘若不存在,新的选项中位置置0
            }
        }
        //重新构建视图
        grp_deskLoc.removeAllViews();
        for(int i=0; i<mMerchantDeskLocList.size(); i++){
            MerchantDeskLocation mdl = mMerchantDeskLocList.get(i);
            LayoutInflater mInflater = LayoutInflater.from(getApplicationContext());
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            RadioButton rdo_deskLocItem = (RadioButton)mInflater.inflate(R.layout.desk_location_item, null);
            rdo_deskLocItem.setId(i);
            rdo_deskLocItem.setTag(mdl.getLocationCode()+"");
            rdo_deskLocItem.setText(mdl.getLocationName()+"");
            rdo_deskLocItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView,
                                             boolean isChecked) {
                    if(isChecked){
                        String mdl_code = (String)buttonView.getTag();
                        curIndex=buttonView.getId();
                        if(mMerchantDeskLocList!=null&&mMerchantDeskLocList.size()>=curIndex){
                            mCurDeskLoc=mMerchantDeskLocList.get(curIndex);
                            updateLocationDesks(mCurDeskLoc.getLocationCode());
                        }else{
                            KLog.i("mMerchantDeskLocList有问题");
                        }
                    }
                }
            });
            if(curIndex==0&&i==0){
                rdo_deskLocItem.setChecked(true);
            }else if(curIndex==i){
                rdo_deskLocItem.setChecked(true);
            }else{
                rdo_deskLocItem.setChecked(false);
            }
            grp_deskLoc.addView(rdo_deskLocItem, lp);
        }

        if (curIndex!=0){
            new Handler().postDelayed((new Runnable() {
                    @Override public void run() {
                        if(grp_deskLoc!=null&&grp_deskLoc.getChildCount()>=curIndex)
                        deskTypeLsitView.scrollTo(((RadioButton)grp_deskLoc.getChildAt(curIndex-1)).getLeft()-80,0);
                        else KLog.i("桌子类型视图有问题");
                    }
                }),5);
        }
    }
	
	/**
	 * 更新桌子显示
	 */
	private void updateLocationDesks(String mdl_code){
		synchronized(mMerchantDeskLocList){
			for(int i=0; i<mMerchantDeskLocList.size(); i++){
				MerchantDeskLocation mdl = mMerchantDeskLocList.get(i);
				if(mdl.getLocationCode().equals(mdl_code)){
					mDeskList = mdl.getMerchantDeskList();
					Log.d(TAG, "mChooseDeskAdapter: " + mChooseDeskAdapter);
					mChooseDeskAdapter.onRefresh(mDeskList);
					if(mDeskList==null || mDeskList.size()==0){
						onShowEmpty();
					}
				}
			}
		} 
	}
	
	/**
	 * 点击桌子,进入点菜
	 */
	private OnItemClickListener onDeskItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(View view, int position) {
            try {
                if(mChooseDeskAdapter.getCount()>=position){
                    mChooseDeskAdapter.setSelectedPos(position);
                    mCurDesk = (MerchantDesk)mChooseDeskAdapter.getItem(position);
                    httpGetDeskOrderByDeskId(); //获取桌子订单，根据情况进行后续操作
                }
            }catch (Exception e){
                e.printStackTrace();
            }

		}
	};
	
	/**
	 * 新开桌子下单，输入用餐人数
	 */
	private void openNewDeskInputPersonNumDialog(){
        try {
        InputOrderPersonNumDF mInputOrderPersonNumDF = InputOrderPersonNumDF.newInstance();
		mInputOrderPersonNumDF.setOnFinishInputNumListener(new OnFinishInputNumListener() {
			@Override
			public void onInputNumCallBack(int personNum) {
				Intent intent = new Intent(ChooseDeskActivity.this, MakeOrderActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("CHILD_MERCHANT_ID", childMerchantId);
				bundle.putSerializable("SELECTED_MERCHANT_DESK", mCurDesk);
				bundle.putInt("ORDER_PERSON_NUM", personNum);
				intent.putExtra("BUNDLE", bundle);
				startActivity(intent);	
			}
		});
		mInputOrderPersonNumDF.show(getSupportFragmentManager(), "fragment_input_order_person_num");
        }catch (Exception e){
            e.printStackTrace();
        }
	}
	
	/**
	 * 桌子已有订单，进入选择订单
	 * salesstate=0 稍后下单
	 * salesstate=1 立即下单
	 */
	private void openDeskOrderSelectDialog(){
		Boolean isDirectToDishesPage = true;
		Boolean isHandled = false;
        //只有一张订单，并且非保留订单,直接操作,不需要跳出订单列表
		if(mDeskOrderList.size()==1){
			DeskOrder deskOrder = mDeskOrderList.get(0);
			if(deskOrder!=null){
                if(deskOrder.getOrderState().equals("1"))
                {
                    showShortTip(deskOrder.getOrderId()+"-本单已挂单,无法操作!");
                    return;
                }else if(deskOrder.getOrderState().equals("8")){
                    showShortTip(deskOrder.getOrderId()+"-本单扫码支付中,无法操作!");
                    return;
                }
				List<DeskOrderGoodsItem> orderGoods = deskOrder.getOrderGoods();
				if(orderGoods!=null){
					for(int i=0; i<orderGoods.size(); i++){
						DeskOrderGoodsItem mDeskOrderGoodsItem = orderGoods.get(i);
						if(StringUtils.str2Int(mDeskOrderGoodsItem.getSalesState())==0){
							isDirectToDishesPage = false;
							break;
						}
					}
				}
			}
			if(isDirectToDishesPage){
				//httpLockDeskOrder(staffId, deskOrder);
				isHandled = true;
				Gson gson = new Gson();
				String deskOrderJsonStr = gson.toJson(deskOrder);
				Intent intent = new Intent(ChooseDeskActivity.this, MakeOrderActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("CHILD_MERCHANT_ID", childMerchantId);
				bundle.putSerializable("SELECTED_MERCHANT_DESK", mCurDesk);
				bundle.putInt("ORDER_PERSON_NUM", StringUtils.str2Int(deskOrder.getPersonNum()));
				bundle.putString("CURRENT_SELECTED_ORDER", deskOrderJsonStr);
				intent.putExtra("BUNDLE", bundle);
				startActivity(intent);
			}
		}
		if(!isHandled){
			ChooseDeskOrderDF mChooseDeskOrderDF = ChooseDeskOrderDF.newInstance(mDeskOrderList);
			mChooseDeskOrderDF.setOnFinishChooseDeskOrderListener(new OnFinishChooseDeskOrderListener() {
				@Override
				public void onFinishChooseCallBack(int actionType, DeskOrder deskOrder) {
					if(actionType == Constants.CHOOSE_DESK_DESK_ORDER_ADD_DISHES){
						//加菜
						Gson gson = new Gson();
						String deskOrderJsonStr = gson.toJson(deskOrder);
						Intent intent = new Intent(ChooseDeskActivity.this, MakeOrderActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("CHILD_MERCHANT_ID", childMerchantId);
						bundle.putSerializable("SELECTED_MERCHANT_DESK", mCurDesk);
						bundle.putInt("ORDER_PERSON_NUM", StringUtils.str2Int(deskOrder.getPersonNum()));
						bundle.putString("CURRENT_SELECTED_ORDER", deskOrderJsonStr);
						bundle.putBoolean("DESK_ORDER_NOTIFY_KITCHEN", false);
						intent.putExtra("BUNDLE", bundle);
						startActivity(intent);
					}else{
						//通知后厨
						Gson gson = new Gson();
						String deskOrderJsonStr = gson.toJson(deskOrder);
						Intent intent = new Intent(ChooseDeskActivity.this, MakeOrderActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("CHILD_MERCHANT_ID", childMerchantId);
						bundle.putSerializable("SELECTED_MERCHANT_DESK", mCurDesk);
						bundle.putInt("ORDER_PERSON_NUM", StringUtils.str2Int(deskOrder.getPersonNum()));
						bundle.putString("CURRENT_SELECTED_ORDER", deskOrderJsonStr);
						bundle.putBoolean("DESK_ORDER_NOTIFY_KITCHEN", true);
						intent.putExtra("BUNDLE", bundle);
						startActivity(intent);
					}
				}
			});
			mChooseDeskOrderDF.show(getSupportFragmentManager(), "fragment_choose_desk_order");
		}
	}

	public void initListener(){
		btn_exit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
                quickAction.show(v);
                backgroundAlpha(0.5f);
			}
		});
        setmEnsureDialogListener(new EnsureDialogFragmentBase.CallBackListener() {
            @Override
            public void onLeftBtnFinish() {
                dismissEnsureDialog();
            }

            @Override
            public void onRightBtnFinish() {
                finish();
            }
        },"","退出登录吗？");
	}
	
	/**
	 * 同步桌子数据和桌子区域数据
	 */
	public void dbSynchMerchantDeskData(){
		//先删除所有的桌子区域和桌子数据
		DataSupport.deleteAll(MerchantDeskLocation.class);
		DataSupport.deleteAll(MerchantDesk.class);
		if(mMerchantDeskLocList!=null && mMerchantDeskLocList.size()>0){
			DataSupport.saveAll(mMerchantDeskLocList);
			for(int i=0; i<mMerchantDeskLocList.size(); i++){
				MerchantDeskLocation mdl = mMerchantDeskLocList.get(i);
				List<MerchantDesk> mdList = mdl.getMerchantDeskList();
				if(mdList!=null && mdList.size()>0){
					for(int m=0; m<mdList.size(); m++){
						MerchantDesk desk = mdList.get(m);
						desk.setDeskState("0");
						mdList.set(m, desk);
					}
					DataSupport.saveAll(mdList);
				}
			}
		}
		Log.d(TAG, "同步桌子数据");
		String curDate = StringUtils.date2Str(new Date(), StringUtils.DATE_FORMAT);
		mSystemPrefData.saveLastMerchantDeskDataAsychDate(curDate);
	}
	
	/**
	 * 根据桌子id，获取该桌子对应的订单
	 * @param
	 */
	private void httpGetDeskOrderByDeskId(){

		showCommonDialog("正在获取订单...");
		String url = "/appController/queryUnfinishedOrder.do?childMerchantId="+childMerchantId+"&deskId="+mCurDesk.getDeskId();
	    Log.d(TAG, "httpGetDeskOrderBydeskId:" + HttpController.HOST + url);
		JsonObjectRequest httpGetDeskOrderByDeskId = new JsonObjectRequest(
	    		HttpController.HOST+url, null,
	    		new Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject data) {
						Log.d(TAG, "httpGetDeskOrderByDeskId: "+data.toString());
						dismissCommonDialog();
						try {
							if(data.getString("msg").equals("ok")){
								String info = data.getJSONObject("data").getString("info");
								Gson gson = new Gson();
								mDeskOrderList = gson.fromJson(info, new TypeToken<List<DeskOrder>>(){}.getType());
                                Log.d(TAG, "mDeskOrderList.size(): " + mDeskOrderList.size());
							    if(mDeskOrderList==null || mDeskOrderList.size()==0){
							    	openNewDeskInputPersonNumDialog();
							    }else{
                                    List<DeskOrder> abandonedOrder =new ArrayList<DeskOrder>();
                                    for (DeskOrder mDeskOrder:mDeskOrderList){
                                        if(mDeskOrder.getOrderState().equals("11")){
                                            abandonedOrder.add(mDeskOrder);
                                        }
                                    }
                                    if(abandonedOrder.size()>0&&mDeskOrderList.size()>=abandonedOrder.size())
                                        mDeskOrderList.removeAll(abandonedOrder);
                                    if(mDeskOrderList.size()==0)openNewDeskInputPersonNumDialog();
                                    else openDeskOrderSelectDialog();
							    }
							}else{
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, 
	    		new ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						dismissCommonDialog();
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
					}
				});
	    executeRequest(httpGetDeskOrderByDeskId);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		mMerchantDeskLocList = null;
//		mCurDeskLoc = null;保持当前桌子类型
//		mCurDesk = null;保持当前桌子不置空
		mDeskOrderList = null;
	}
	
	@Override
	protected void onDestroy() {
        EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
    public void onShowEmpty() {
        mChooseDeskAdapter.clear();
        mEmptyLayout.showEmpty();
    }

    public void onShowLoading() {
        mChooseDeskAdapter.clear();
        mEmptyLayout.showLoading();
    }

    public void onShowError() {
        mChooseDeskAdapter.clear();
        mEmptyLayout.showError();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            showEnsureDialog("ChooseDesk");
            return  true;
        }
        return  super.onKeyDown(keyCode, event);
    }

    private void httpGetMerchantDishes(String childMerchantId,String MerchantId) {
        showCommonDialog("正在更新菜单...");
        String dishesWithAttrs = "/appController/queryDishesInfoNoComp.do?childMerchantId=" + childMerchantId+"&merchantId="+MerchantId;
        String url = "/appController/queryAllDishesInfoByMerchantId.do?childMerchantId=" + childMerchantId;
        Log.d(TAG, "httpGetMerchantDishes: " + HttpController.HOST + dishesWithAttrs);
        JsonObjectRequest httpGetMerchantDishes = new JsonObjectRequest(
                HttpController.HOST + dishesWithAttrs, null,
                new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        try {
                            if (data.getString("msg").equals("ok")) {
                                Gson gson = new Gson();
                                JSONObject datainfo=data.getJSONObject("data");
                                mDishTypeDataList = gson.fromJson(datainfo.getString("types"), new TypeToken<List<MerchantDishesType>>() {
                                }.getType());
                                Log.d(TAG, "Dishes Type Count: " + mDishTypeDataList.size());
                                mAllDishesDataList = gson.fromJson(datainfo.getString("dishes"), new TypeToken<List<MerchantDishes>>() {
                                }.getType());
                                Log.d(TAG, "All Dishes Count: " + mAllDishesDataList.size());

                                if(datainfo.has("attrs")){
                                    QueryAppMerchantPublicAttr attr=new QueryAppMerchantPublicAttr();
                                    ArrayList<PublicDishesItem> attrInfos=gson.fromJson(datainfo.getString("attrs"), new TypeToken<ArrayList<PublicDishesItem>>() {
                                    }.getType());
                                    attr.setInfo(attrInfos);
                                    baseApp.assignData(baseApp.KEY_GLOABLE_PUBLICATTR,attr);
//                                    baseApp.setPublicAttr(attr);
                                }
//                                else   baseApp.setPublicAttr(null);
                                EventBackground event = new EventBackground();
                                DishesListEntity DishesListEntity = new DishesListEntity();
                                DishesListEntity.setmDishTypeDataList(mDishTypeDataList);
                                DishesListEntity.setmAllDishesDataList(mAllDishesDataList);
                                event.setData(DishesListEntity);
                                event.setName(ChooseDeskActivity.class.getName());
                                event.setType(EventBackground.TYPE_FIRST);
                                event.setDescribe("菜单数据传入后台线程存入数据库");
                                EventBus.getDefault().post(event);
                                dismissCommonDialog();
                            } else {
                                dismissCommonDialog();
                                showShortTip("菜品更新失败! " + data.getString("msg"));
                                Log.d(TAG, "菜品更新失败,请确认菜单!");
                            }
                        } catch (JSONException e) {
                            dismissCommonDialog();
                            showShortTip("菜品更新失败,请确认菜单! ");
                            e.printStackTrace();
                        }
                    }
                },
                new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissCommonDialog();
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                    }
                });
        executeRequest(httpGetMerchantDishes);
    }

    private void httpGetMerchantDishes2(String childMerchantId,String MerchantId) {
        showCommonDialog("正在更新菜单...");
        HttpController.getInstance().getMerchantDishes(childMerchantId,MerchantId,new Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject data) {
                        try {
                            if (data.getString("msg").equals("ok")) {
                                Gson gson = new Gson();
                                JSONObject datainfo=data.getJSONObject("data");
                                mDishTypeDataList = gson.fromJson(datainfo.getString("types"), new TypeToken<List<MerchantDishesType>>() {
                                }.getType());
                                Log.d(TAG, "Dishes Type Count: " + mDishTypeDataList.size());
                                mAllDishesDataList = gson.fromJson(datainfo.getString("dishes"), new TypeToken<List<MerchantDishes>>() {
                                }.getType());
                                Log.d(TAG, "All Dishes Count: " + mAllDishesDataList.size());

                                if(datainfo.has("attrs")){
                                    QueryAppMerchantPublicAttr attr=new QueryAppMerchantPublicAttr();
                                    ArrayList<PublicDishesItem> attrInfos=gson.fromJson(datainfo.getString("attrs"), new TypeToken<ArrayList<PublicDishesItem>>() {
                                    }.getType());
                                    attr.setInfo(attrInfos);
                                    baseApp.assignData(baseApp.KEY_GLOABLE_PUBLICATTR,attr);
//                                    baseApp.setPublicAttr(attr);
                                }
//                                else   baseApp.setPublicAttr(null);
                                EventBackground event = new EventBackground();
                                DishesListEntity DishesListEntity = new DishesListEntity();
                                DishesListEntity.setmDishTypeDataList(mDishTypeDataList);
                                DishesListEntity.setmAllDishesDataList(mAllDishesDataList);
                                event.setData(DishesListEntity);
                                event.setName(ChooseDeskActivity.class.getName());
                                event.setType(EventBackground.TYPE_FIRST);
                                event.setDescribe("菜单数据传入后台线程存入数据库");
                                EventBus.getDefault().post(event);
                                dismissCommonDialog();
                            } else {
                                dismissCommonDialog();
                                showShortTip("菜品更新失败! " + data.getString("msg"));
                                Log.d(TAG, "菜品更新失败,请确认菜单!");
                            }
                        } catch (JSONException e) {
                            dismissCommonDialog();
                            showShortTip("菜品更新失败,请确认菜单! ");
                            e.printStackTrace();
                        }
                    }
                },new ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissCommonDialog();
                        Log.e("VolleyLogTag", "VolleyError:" + error.getMessage(), error);
                        showShortTip(VolleyErrorHelper.getMessage(error, mActivity));
                    }
                });
    }

    @Override
    public boolean onEventMainThread(EventMain event) {
        boolean isRun=super.onEventMainThread(event);
        if (isRun) {
            switch (event.getType()) {
                case EventMain.TYPE_FIRST:
                    showShortTip("菜单更新成功!");
                    break;
                default:
                    break;
            }
        }
        return isRun;
    }

    @Override
    public boolean onEventBackgroundThread(EventBackground event) {
        boolean isRun=super.onEventBackgroundThread(event);
        if (isRun) {
            switch (event.getType()) {
                case EventBackground.TYPE_FIRST:
                    DishesListEntity DishesListEntity = (DishesListEntity) event.getData();
                    mDishTypeDataList = DishesListEntity.getmDishTypeDataList();
                    if (mDishTypeDataList != null && mDishTypeDataList.size() > 0) {
                        DataSupport.deleteAll(MerchantDishesType.class);//清空菜品类型缓存表
                        DataSupport.saveAll(mDishTypeDataList);
                    }
                    if (mAllDishesDataList != null && mAllDishesDataList.size() > 0) {
                        DataSupport.deleteAll(MerchantDishes.class);//清空菜品缓存
                        DataSupport.deleteAll(DishesProperty.class);//清空菜品属性类型缓存
                        DataSupport.deleteAll(DishesPropertyItem.class);//清空菜品属性值缓存
                        DataSupport.saveAll(mAllDishesDataList);
                        for (int i = 0; i < mAllDishesDataList.size(); i++) {
                            MerchantDishes md = mAllDishesDataList.get(i);
                            List<DishesProperty> dpList = md.getDishesItemTypelist();
                            if (dpList != null && dpList.size() > 0) {
                                DataSupport.saveAll(dpList); //缓存菜品属性类型数据
                                for (int j = 0; j < dpList.size(); j++) {
                                    DishesProperty dpItem = dpList.get(j);
                                    List<DishesPropertyItem> dpiList = dpItem.getItemlist();
                                    DataSupport.saveAll(dpiList); //缓存菜品属性值数据
                                }
                            }
                        }
                    }
                    //更新时将套餐数据清空，后续缓存更新
                    DataSupport.deleteAll(DishesComp.class);
                    DataSupport.deleteAll(DishesCompItem.class);

                    EventMain eventMain = new EventMain();
                    eventMain.setName(ChooseDeskActivity.class.getName());
                    eventMain.setType(EventMain.TYPE_FIRST);
                    eventMain.setDescribe("菜品更新成功后，通知消息发布到桌台页面");
                    EventBus.getDefault().post(eventMain);
                    break;
                default:
                    break;
            }
        }
        return isRun;
    }
}