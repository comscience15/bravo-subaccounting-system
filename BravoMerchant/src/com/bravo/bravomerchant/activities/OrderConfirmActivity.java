package com.bravo.bravomerchant.activities;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.bravo.bravomerchant.R;
import com.bravo.bravomerchant.bean.Order;
import com.bravo.bravomerchant.bean.OrderItem;
import com.bravo.bravomerchant.util.ArithUtil;
import com.bravo.bravomerchant.util.JsonUtil;
import com.bravo.bravomerchant.util.NFCHandler;

import android.os.Bundle;
import android.os.Handler;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Merchant main activity
 * @author wenlong_jia
 *
 */
public class OrderConfirmActivity extends ListActivity {

	//product data list
	private List<OrderItem> orderItemList;
    private Intent getIntentSource;
    //order data
    private Order order;
    //product barcode list
    private ArrayList<String> productBarCodesList;
	private boolean doublePressBackButton = false;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		/** Removing title bar */
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);
        
        getIntentSource = getIntent();
        //get the scanned bar code and use it to init the order object
        productBarCodesList = getIntentSource.getStringArrayListExtra("productBarCodesList");
        order = new Order(productBarCodesList);
        //init the product-data-list
        orderItemList = order.getItemInfoList();
        
        //show the order total price
		TextView totalPriceTextView = (TextView)findViewById(R.id.order_total_price);
        totalPriceTextView.setText(String.valueOf(getTotalPrice()));
        
        //set the btn-listener for the pay button
        Button payOrderButton = (Button)findViewById(R.id.pay_order);
        payOrderButton.setOnClickListener(new View.OnClickListener() {
			
        	//turn to the ScannerActivity, ready to scan the qr code of the customer card
			@Override
			public void onClick(View v) {
				// Check availability of NFC
				NFCHandler.checkNFCAvailability(OrderConfirmActivity.this);

				Intent toCardScanIntent = new Intent();
				toCardScanIntent.setClass(OrderConfirmActivity.this, ScannerActivity.class);
				
				try {
					
					//with the products info
					toCardScanIntent.putExtra("orderItemListJsonStr", JsonUtil.getJsonStr(orderItemList));
					toCardScanIntent.putStringArrayListExtra("productBarCodesList", productBarCodesList);
					startActivity(toCardScanIntent);
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		});

        //set the btn-listener for the go-on-scanning-products button
        Button scanProductButton = (Button)findViewById(R.id.scan_product);
        scanProductButton.setOnClickListener(new View.OnClickListener() {
			
        	//turn to the ScannerActivity, ready to scan another product
			@Override
			public void onClick(View v) {

				Intent toScanProductIntent = new Intent();
				toScanProductIntent.setClass(OrderConfirmActivity.this, ScannerActivity.class);
				toScanProductIntent.putStringArrayListExtra("productBarCodesList", productBarCodesList);
				startActivity(toScanProductIntent);
			}
		});
        
        //create a listAdapter to show the scanned product info
        OrderListAdapter orderListAdapter = new OrderListAdapter(this);
        setListAdapter(orderListAdapter);
    }
    
    //when double press back button, turn to the MainActivity, and all the products data and order data will be lost
    @Override
    public void onBackPressed() {
    	
    	if (doublePressBackButton) {
    		
			Intent toMainPageIntent = new Intent(this, MainActivity.class);
			startActivity(toMainPageIntent);
    	}
    	this.doublePressBackButton = true;
    	
    	/** Setting the toast*/
    	Context context = getApplicationContext();
    	CharSequence text = getString(R.string.back_to_main_toast);
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, text, duration);
    	toast.show();
    	
    	/** Setting boolean variable to false after 3sec*/
    	new Handler().postDelayed(new Runnable() {
    		@Override
    		public void run() {
    			doublePressBackButton = false;
    		}
    	}, 3000);
    }
    
    
    /**
     * calculate the total price of this order
     * @return
     */
    private Double getTotalPrice() {
    	
    	Double res = 0d;
    	for (int i = 0; i < orderItemList.size(); i++) {
			

    		res = ArithUtil.add(res, orderItemList.get(i).getTotalPrice());
		}
		return res;
	}

    /**
     * the listAdapter for showing the scanned product info
     * @author jiawl
     *
     */
	private class OrderListAdapter extends BaseAdapter{
		
        private Context mContext;
        
    	public OrderListAdapter(Context context) {
			this.mContext=context;
		}


		public int getCount() {
			return orderItemList.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
		
		/**
		 * return a new product view.Every product info in the page is made by this.
		 */
		public View getView(int position, View convertView, ViewGroup parent) {
//			@+id/order_item_name @+id/order_item_unit @+id/order_item_tax @+id/order_item_price @+id/order_item_remove_button

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
			OrderItem orderItem = orderItemList.get(position);
			
			cache.orderItemNameTextView.setText("NAME:"+orderItem.getName()+"("+orderItem.getBarCode()+")");
			cache.orderItemUnitTextView.setText("UNIT:"+String.valueOf(orderItem.getUnit()));
			cache.orderItemTaxTextView.setText("TAX:"+String.valueOf(orderItem.getTax()));
			cache.orderItemPriceTextView.setText("PRICE:"+String.valueOf(orderItem.getTotalPrice()));
			cache.removeButtonImageView.setContentDescription(String.valueOf(position));

			//when click the remove btn, remove the product
			cache.removeButtonImageView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					CharSequence position = v.getContentDescription();
					//remove the product 
					productBarCodesList.remove(orderItemList.remove(Integer.parseInt(position.toString())).getBarCode());
					order = new Order(productBarCodesList);
					
					if(orderItemList.size() > 0){//when have other products, reCalculate the total price, and refresh the list view.

						TextView totalPriceTextView = (TextView)findViewById(R.id.order_total_price);
				        totalPriceTextView.setText(String.valueOf(getTotalPrice()));
						notifyDataSetChanged();
					}else{//when no products left, turn to the main page

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

	private static class ItemViewCache{
		public TextView orderItemNameTextView;
		public TextView orderItemUnitTextView;
		public TextView orderItemTaxTextView;
		public TextView orderItemPriceTextView;
		public ImageView removeButtonImageView;
	}
}
