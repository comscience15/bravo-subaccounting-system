package com.bravo.bravomerchant.activities;

import java.util.List;

import org.json.JSONException;

import com.bravo.bravomerchant.R;
import com.bravo.bravomerchant.bean.Order;
import com.bravo.bravomerchant.bean.OrderItem;
import com.bravo.bravomerchant.util.JsonUtil;

import android.os.Bundle;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Merchant main activity
 * @author wenlong_jia
 *
 */
public class OrderConfirmActivity extends ListActivity {
    //展示的文字
	List<OrderItem> orderItemList;
    private Intent getIntentSource;
    private Order order;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        
        getIntentSource = getIntent();
        String productsCode = getIntentSource.getStringExtra("productsCode");
        order = new Order();
        
        if(productsCode != null
        		&& !"".equals(productsCode)){
        	
        	String[] productsCodeArray = productsCode.split(",");
        	for (int i = 0; i < productsCodeArray.length; i++) {
				
        		if("".equals(productsCodeArray[i])) continue;
        		
        		order.addItem(productsCodeArray[i]);
			}
        }

        orderItemList = order.getItemInfoList();
        
		TextView totalPriceTextView = (TextView)findViewById(R.id.order_total_price);
        totalPriceTextView.setText(String.valueOf(getTotalPrice()));
        
        ImageView payOrderImageView = (ImageView)findViewById(R.id.pay_order);
        payOrderImageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {

				Intent toCardScanIntent = new Intent();
				toCardScanIntent.setClass(OrderConfirmActivity.this, ScannerActivity.class);
				try {
					toCardScanIntent.putExtra("orderItemListJsonStr", JsonUtil.getJsonStr(orderItemList));
					startActivity(toCardScanIntent);
				} catch (JSONException e) {
					//TODO show error msg
					e.printStackTrace();
				}
			}
		});
        
        //设置一个Adapter,使用自定义的Adapter
        OrderListAdapter orderListAdapter = new OrderListAdapter(this);
        setListAdapter(orderListAdapter);
    }
    
    private Double getTotalPrice() {
    	
    	Double res = 0d;
    	for (int i = 0; i < orderItemList.size(); i++) {
			
    		res += orderItemList.get(i).getTotalPrice();
		}
		return res;
	}

	private class OrderListAdapter extends BaseAdapter{
        private Context mContext;
        
    	public OrderListAdapter(Context context) {
			this.mContext=context;
		}
        /**
         * 元素的个数
         */
		public int getCount() {
			return orderItemList.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
		//用以生成在ListView中展示的一个个元素View
		public View getView(int position, View convertView, ViewGroup parent) {
//			@+id/order_item_name @+id/order_item_unit @+id/order_item_tax @+id/order_item_price @+id/order_item_remove_button
			//优化ListView
			if(convertView==null){
				convertView=LayoutInflater.from(mContext).inflate(R.layout.order_item, null);
				ItemViewCache viewCache=new ItemViewCache();
				viewCache.orderItemNameTextView=(TextView)convertView.findViewById(R.id.order_item_name);
				viewCache.orderItemUnitTextView=(TextView)convertView.findViewById(R.id.order_item_unit);
				viewCache.orderItemTaxTextView=(TextView)convertView.findViewById(R.id.order_item_tax);
				viewCache.orderItemPriceTextView=(TextView)convertView.findViewById(R.id.order_item_price);
				viewCache.removeButtonImageView=(ImageView)convertView.findViewById(R.id.order_item_remove_button);
				convertView.setTag(viewCache);
			}
			ItemViewCache cache=(ItemViewCache)convertView.getTag();
			//设置文本和图片，然后返回这个View，用于ListView的Item的展示
			OrderItem orderItem = orderItemList.get(position);
			cache.orderItemNameTextView.setText("NAME:"+orderItem.getName()+"("+orderItem.getBarCode()+")");
			cache.orderItemUnitTextView.setText("UNIT:"+String.valueOf(orderItem.getUnit()));
			cache.orderItemTaxTextView.setText("TAX:"+String.valueOf(orderItem.getTax()));
			cache.orderItemPriceTextView.setText("PRICE:"+String.valueOf(orderItem.getTotalPrice()));
			cache.removeButtonImageView.setContentDescription(String.valueOf(position));
			cache.removeButtonImageView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CharSequence position = v.getContentDescription();
					orderItemList.remove(Integer.parseInt(position.toString()));
					if(orderItemList.size() > 0){

						TextView totalPriceTextView = (TextView)findViewById(R.id.order_total_price);
				        totalPriceTextView.setText(String.valueOf(getTotalPrice()));
						notifyDataSetChanged();
					}else{

						Intent backToMainIntent = new Intent();
						backToMainIntent.setClass(OrderConfirmActivity.this, MainActivity.class);
						startActivity(backToMainIntent);
						OrderConfirmActivity.this.finish();
					}
				}
			});
			return convertView;
		}
    }
    //元素的缓冲类,用于优化ListView
    private static class ItemViewCache{
		public TextView orderItemNameTextView;
		public TextView orderItemUnitTextView;
		public TextView orderItemTaxTextView;
		public TextView orderItemPriceTextView;
		public ImageView removeButtonImageView;
	}
}
