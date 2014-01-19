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
    //չʾ������
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
          
        //����һ��Adapter,ʹ���Զ����Adapter
        setListAdapter(new OrderListAdapter(this));
    }
    
    private class OrderListAdapter extends BaseAdapter{
        private Context mContext;
    	public OrderListAdapter(Context context) {
			this.mContext=context;
		}
        /**
         * Ԫ�صĸ���
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
		//����������ListView��չʾ��һ����Ԫ��View
		public View getView(int position, View convertView, ViewGroup parent) {
			//�Ż�ListView
			if(convertView==null){
				convertView=LayoutInflater.from(mContext).inflate(R.layout.order_item, null);
				ItemViewCache viewCache=new ItemViewCache();
				viewCache.mTextView=(TextView)convertView.findViewById(R.id.order_item_name);
				viewCache.mImageView=(ImageView)convertView.findViewById(R.id.order_item_remove_button);
				convertView.setTag(viewCache);
			}
			ItemViewCache cache=(ItemViewCache)convertView.getTag();
			//�����ı���ͼƬ��Ȼ�󷵻����View������ListView��Item��չʾ
			cache.mTextView.setText(itemInfoList.get(position));
			cache.mImageView.setImageResource(R.drawable.pay);
			return convertView;
		}
    }
    //Ԫ�صĻ�����,�����Ż�ListView
    private static class ItemViewCache{
		public TextView mTextView;
		public ImageView mImageView;
	}
}
