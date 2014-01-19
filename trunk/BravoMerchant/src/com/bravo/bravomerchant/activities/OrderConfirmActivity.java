package com.bravo.bravomerchant.activities;

import java.util.ArrayList;
import java.util.List;

import com.bravo.bravomerchant.R;
import com.bravo.bravomerchant.bean.Order;

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
    private List<String> itemInfoList;
    private Intent getIntentSource;
    private Order order;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        
        getIntentSource = getIntent();
        
        String cardId = getIntentSource.getStringExtra("cardId");
        String productsCode = getIntentSource.getStringExtra("productsCode");
        
        order = new Order();
        order.setCardId(cardId);
        
        if(productsCode != null
        		&& !"".equals(productsCode)){
        	
        	String[] productsCodeArray = productsCode.split(",");
        	for (int i = 0; i < productsCodeArray.length; i++) {
				
        		if("".equals(productsCodeArray[i])) continue;
        		
        		order.addItem(productsCodeArray[i]);
			}
        }

        itemInfoList = order.getItemInfoList();
          
        //设置一个Adapter,使用自定义的Adapter
        setListAdapter(new OrderListAdapter(this));
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
			return itemInfoList.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
		//用以生成在ListView中展示的一个个元素View
		public View getView(int position, View convertView, ViewGroup parent) {
			//优化ListView
			if(convertView==null){
				convertView=LayoutInflater.from(mContext).inflate(R.layout.order_item, null);
				ItemViewCache viewCache=new ItemViewCache();
				viewCache.mTextView=(TextView)convertView.findViewById(R.id.order_item_name);
				viewCache.mImageView=(ImageView)convertView.findViewById(R.id.order_item_remove_button);
				convertView.setTag(viewCache);
			}
			ItemViewCache cache=(ItemViewCache)convertView.getTag();
			//设置文本和图片，然后返回这个View，用于ListView的Item的展示
			cache.mTextView.setText(itemInfoList.get(position));
			cache.mImageView.setImageResource(R.drawable.pay);
			return convertView;
		}
    }
    //元素的缓冲类,用于优化ListView
    private static class ItemViewCache{
		public TextView mTextView;
		public ImageView mImageView;
	}
}
