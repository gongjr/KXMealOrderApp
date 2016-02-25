package com.asiainfo.mealorder.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.asiainfo.mealorder.R;
import com.asiainfo.mealorder.adapter.DishPropertyValuesAdapter;
import com.asiainfo.mealorder.entity.DishesProperty;
import com.asiainfo.mealorder.entity.DishesPropertyItem;
import com.asiainfo.mealorder.listener.OnEnsureCheckedPropertyItemsListener;
import com.asiainfo.mealorder.listener.OnItemClickListener;
import com.asiainfo.mealorder.listener.OnPropertyCheckedChangeListener;
import com.asiainfo.mealorder.ui.base.DialogFragmentBase;
import com.asiainfo.mealorder.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gjr
 *
 * 2015年6月25日
 *
 * 选择套餐属性值的弹出框
 */
public class ChoosePropertyValueDF extends DialogFragmentBase{
	private static final String TAG = ChoosePropertyValueDF.class.getName();
	
	private int CHOOSE_PROPERTY_DIALOG_TYPE;
	private View mView;
	private TextView tv_propertyName/*头部标题*/;
	private ImageView img_close;
	private DishPropertyValuesAdapter mDishPropertyValuesAdapter;
  	private ListView lv_properties; 
	private List<DishesPropertyItem> mDishPropertyItemsDataList;
	private View tv_propertyValue;
	private int curCount; //本菜当前所选数量
    private int position=0;
	private OnEnsureCheckedPropertyItemsListener mOnEnsureCheckedPropertyItemsListener;
	private Button btn_ensure;
	private DishesProperty mDishesProperty;
	private List<String> curSelectedPropertyList; //当前选择的属性值 
	
	
	public static ChoosePropertyValueDF propertyDF;
	public static ChoosePropertyValueDF newInstance(int dialogType, DishesProperty mDishesProperty) {
		if(propertyDF==null){
			propertyDF = new ChoosePropertyValueDF();
		}
	    Bundle args = new Bundle();
	    args.putInt("CHOOSE_PROPERTY_DIALOG_TYPE", dialogType);
	    args.putSerializable("DISHES_PROPERTY", mDishesProperty);
	    propertyDF.setArguments(args);
	    return propertyDF;
	}
	
	/**
	 * 设置当前选中的属性值
	 */
	public void setCurPropertyList(List<String> curSelectedPropertyList){
		this.curSelectedPropertyList = curSelectedPropertyList;
	}
	
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		CHOOSE_PROPERTY_DIALOG_TYPE = getArguments().getInt("CHOOSE_PROPERTY_DIALOG_TYPE");
		mDishesProperty = (DishesProperty) getArguments().getSerializable("DISHES_PROPERTY");
		Log.d(TAG, "CHOOSE_PROPERTY_DIALOG_TYPE:" + CHOOSE_PROPERTY_DIALOG_TYPE);
		Log.d(TAG, "mDishesProperty Name:" + mDishesProperty.getItemTypeName());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		//getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4D000000")));
		mView = inflater.inflate(R.layout.df_choose_property_value, null);
		return mView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setCancelable(true);
		initView();
        initData();
        initListener();
	}
	
	public void initView(){
		tv_propertyName = (TextView)mView.findViewById(R.id.tv_properties_name);
		img_close = (ImageView)mView.findViewById(R.id.img_close);
		lv_properties = (ListView)mView.findViewById(R.id.lv_property);
		btn_ensure = (Button)mView.findViewById(R.id.btn_ensure);
	}
	
	public void initData(){
		tv_propertyName.setText(mDishesProperty.getItemTypeName()); //套餐属性
		//mDishPropertyItemsDataList = new ArrayList<DishesPropertyItem>();
		mDishPropertyItemsDataList = mDishesProperty.getItemlist();
		Log.d(TAG, "mDishPropertyItemsDataList size:" + mDishPropertyItemsDataList.size());
        Log.i("tag","003");
		mDishPropertyValuesAdapter = new DishPropertyValuesAdapter(mActivity, mDishPropertyItemsDataList, -1, mOnItemClickListener, curSelectedPropertyList);
		mDishPropertyValuesAdapter.setOnPropertyCheckedChangeListener(mOnPropertyCheckedChangeListener);
		lv_properties.setAdapter(mDishPropertyValuesAdapter);
	}
	
	public OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(View view, int position) {
			
		}
	};

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
	public void setOnEnsureCheckedPropertyItemsListener(int curCount, View v, OnEnsureCheckedPropertyItemsListener mOnEnsureCheckedPropertyItemsListener,int position){
		this.curCount = curCount;
		this.tv_propertyValue = v;
        this.position=position;
		this.mOnEnsureCheckedPropertyItemsListener = mOnEnsureCheckedPropertyItemsListener;
	}
	
	public void initListener(){
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
				List<DishesPropertyItem> checkedItemsList = getSelectedPropertyItems();
				Log.d(TAG, "checkedItemsList size: " + checkedItemsList.size());
				if(mOnEnsureCheckedPropertyItemsListener!=null){
					if(checkedItemsList.size()>0){
						mOnEnsureCheckedPropertyItemsListener.returnCheckedItems(curCount, tv_propertyValue, checkedItemsList.get(0).getItemType(), curSelectedPropertyList, checkedItemsList,position);
					}
				}
			}
		});
	}
	
	private List<DishesPropertyItem> getSelectedPropertyItems(){
		List<DishesPropertyItem> itemValuesList = new ArrayList<DishesPropertyItem>();
		
		SparseBooleanArray mCheckedStateMap = mDishPropertyValuesAdapter.getCheckedStateMap();
		for(int i=0; i<mDishPropertyItemsDataList.size(); i++){
			DishesPropertyItem dpi = mDishPropertyItemsDataList.get(i);
			Boolean isChecked = mCheckedStateMap.get(StringUtils.str2Int(dpi.getItemId()), false);
		    if(isChecked){
		    	itemValuesList.add(dpi);
		    }
		}
		
		return itemValuesList; 
	}
}
