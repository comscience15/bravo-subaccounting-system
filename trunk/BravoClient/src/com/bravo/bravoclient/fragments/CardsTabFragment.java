package com.bravo.bravoclient.fragments;
 
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
 
import com.actionbarsherlock.app.SherlockFragment;
import com.bravo.bravoclient.R;
 
public class CardsTabFragment extends SherlockFragment{

	private Context cxt = null;
	/** cards页面菜单按钮所使用的图片 */
	private final static Map<Integer,Integer> imageViewBg = new HashMap<Integer,Integer>();
	/** cards页面菜单按钮被按下时所使用的图片 */
	private final static Map<Integer,Integer> imageViewPressedBg = new HashMap<Integer,Integer>();
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		/** Create the customized view for Card Tab*/
		View v = inflater.inflate(R.layout.activity_card, null);
		cxt = getActivity().getApplicationContext();
	    return v;
    }
    
    @SuppressLint("NewApi")
	@Override
    public void onStart() {
        super.onStart();
        // Setting the background when view is starting
        Drawable myDrawable = getResources().getDrawable(R.drawable.solid_background);
        getView().setBackground(myDrawable);
        
        // cards页面的六个按钮。五个菜单按钮，一个查询按钮。按钮都用ImageView实现 
        final ImageView cardPayButton = (ImageView) getView().findViewById(R.id.cardPayButton);
        final ImageView cardReloadButton = (ImageView) getView().findViewById(R.id.cardReloadButton);
        final ImageView cardSelfCheckoutButton = (ImageView) getView().findViewById(R.id.cardSelfCheckoutButton);
        final ImageView cardSendGiftButton = (ImageView) getView().findViewById(R.id.cardSendGiftButton);
        final ImageView cardTransactionHistoryButton = (ImageView) getView().findViewById(R.id.cardTransactionHistoryButton);
        final ImageView cardReceiveMoneyButton = (ImageView) getView().findViewById(R.id.cardReceiveMoneyButton);
        final ImageView cardRefreshButton = (ImageView) getView().findViewById(R.id.cardRefreshButton);
        
        // 监听按钮单击事件 
        OnClickListener buttonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText cardBalanceEditText = (EditText)getView().findViewById(R.id.cardBalanceEditText);
				cardBalanceEditText.setHint("noButton Clicked!");
				if(v.equals(cardPayButton)) {
					cardBalanceEditText.setHint("PayButton Clicked!");
				} else if (v.equals(cardReloadButton)){
					cardBalanceEditText.setHint("ReloadButton Clicked!");
				} else if (v.equals(cardSelfCheckoutButton)){
					cardBalanceEditText.setHint("SelfCheckoutButton Clicked!");
				} else if (v.equals(cardSendGiftButton)){
					cardBalanceEditText.setHint("SendGiftButton Clicked!");
				} else if (v.equals(cardTransactionHistoryButton)){
					cardBalanceEditText.setHint("TransactionHistoryButton Clicked!");
				} else if (v.equals(cardReceiveMoneyButton)){
					cardBalanceEditText.setHint("ReceiveMoneyButton Clicked!");
				} else if (v.equals(cardRefreshButton)){
					cardBalanceEditText.setHint("RefreshButton Clicked!");
				}
			}
        };
        // 为页面上的按钮添加单击事件的监听
        cardPayButton.setOnClickListener(buttonListener);
        cardReloadButton.setOnClickListener(buttonListener);
        cardSelfCheckoutButton.setOnClickListener(buttonListener);
        cardSendGiftButton.setOnClickListener(buttonListener);
        cardTransactionHistoryButton.setOnClickListener(buttonListener);
        cardReceiveMoneyButton.setOnClickListener(buttonListener);
        cardRefreshButton.setOnClickListener(buttonListener);
        
        // 初始化 按钮——图片 对应关系的map，用于按钮被按下时更换图标
        initImageViewBgMap(cardPayButton, cardReloadButton, cardSelfCheckoutButton, cardSendGiftButton, cardTransactionHistoryButton, cardReceiveMoneyButton);
        // 监听屏幕触碰事件，仅能处理ImageView的按下及抬起两个事件。
        OnTouchListener buttonTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageView iv = (ImageView)v;
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					//为按钮更换被按下时的图片
					if(imageViewPressedBg.containsKey(iv.getId())){
						iv.setImageResource(imageViewPressedBg.get(iv.getId()));
					}
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					//为按钮恢复正常显示时的图片
					if(imageViewBg.containsKey(iv.getId())){
						iv.setImageResource(imageViewBg.get(iv.getId()));
					}
				}
				return false;
			}
		};
		//为菜单按钮添加触碰事件监听
		cardPayButton.setOnTouchListener(buttonTouchListener);
		cardReloadButton.setOnTouchListener(buttonTouchListener);
		cardSelfCheckoutButton.setOnTouchListener(buttonTouchListener);
		cardSendGiftButton.setOnTouchListener(buttonTouchListener);
		cardTransactionHistoryButton.setOnTouchListener(buttonTouchListener);
		cardReceiveMoneyButton.setOnTouchListener(buttonTouchListener);
        
    }

    /**
     * 初始化imageViewBg与imageViewPressedBg
     * @param cardPayButton
     * @param cardReloadButton
     * @param cardSelfCheckoutButton
     * @param cardSendGiftButton
     * @param cardTransactionHistoryButton
     * @param cardReceiveMoneyButton
     */
	private void initImageViewBgMap(final ImageView cardPayButton,
			final ImageView cardReloadButton,
			final ImageView cardSelfCheckoutButton,
			final ImageView cardSendGiftButton,
			final ImageView cardTransactionHistoryButton,
			final ImageView cardReceiveMoneyButton) {
		if(imageViewBg != null){
        	imageViewBg.put(cardPayButton.getId(),R.drawable.pay);
        	imageViewBg.put(cardReloadButton.getId(),R.drawable.reload);
        	imageViewBg.put(cardSelfCheckoutButton.getId(),R.drawable.self_checkout);
        	imageViewBg.put(cardSendGiftButton.getId(),R.drawable.send_gift);
        	imageViewBg.put(cardTransactionHistoryButton.getId(),R.drawable.transaction_history);
        	imageViewBg.put(cardReceiveMoneyButton.getId(),R.drawable.receive_money);
        }
        
        if(imageViewPressedBg != null){
        	imageViewPressedBg.put(cardPayButton.getId(),R.drawable.pay_pressed);
        	imageViewPressedBg.put(cardReloadButton.getId(),R.drawable.reload_pressed);
        	imageViewPressedBg.put(cardSelfCheckoutButton.getId(),R.drawable.self_checkout_pressed);
        	imageViewPressedBg.put(cardSendGiftButton.getId(),R.drawable.send_gift_pressed);
        	imageViewPressedBg.put(cardTransactionHistoryButton.getId(),R.drawable.transaction_history_pressed);
        	imageViewPressedBg.put(cardReceiveMoneyButton.getId(),R.drawable.receive_money_pressed);
        }
	}
}