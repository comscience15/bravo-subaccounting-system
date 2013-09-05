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
	/** cardsҳ��˵���ť��ʹ�õ�ͼƬ */
	private final static Map<Integer,Integer> imageViewBg = new HashMap<Integer,Integer>();
	/** cardsҳ��˵���ť������ʱ��ʹ�õ�ͼƬ */
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
        
        // cardsҳ���������ť������˵���ť��һ����ѯ��ť����ť����ImageViewʵ�� 
        final ImageView cardPayButton = (ImageView) getView().findViewById(R.id.cardPayButton);
        final ImageView cardReloadButton = (ImageView) getView().findViewById(R.id.cardReloadButton);
        final ImageView cardSelfCheckoutButton = (ImageView) getView().findViewById(R.id.cardSelfCheckoutButton);
        final ImageView cardSendGiftButton = (ImageView) getView().findViewById(R.id.cardSendGiftButton);
        final ImageView cardTransactionHistoryButton = (ImageView) getView().findViewById(R.id.cardTransactionHistoryButton);
        final ImageView cardReceiveMoneyButton = (ImageView) getView().findViewById(R.id.cardReceiveMoneyButton);
        final ImageView cardRefreshButton = (ImageView) getView().findViewById(R.id.cardRefreshButton);
        
        // ������ť�����¼� 
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
        // Ϊҳ���ϵİ�ť��ӵ����¼��ļ���
        cardPayButton.setOnClickListener(buttonListener);
        cardReloadButton.setOnClickListener(buttonListener);
        cardSelfCheckoutButton.setOnClickListener(buttonListener);
        cardSendGiftButton.setOnClickListener(buttonListener);
        cardTransactionHistoryButton.setOnClickListener(buttonListener);
        cardReceiveMoneyButton.setOnClickListener(buttonListener);
        cardRefreshButton.setOnClickListener(buttonListener);
        
        // ��ʼ�� ��ť����ͼƬ ��Ӧ��ϵ��map�����ڰ�ť������ʱ����ͼ��
        initImageViewBgMap(cardPayButton, cardReloadButton, cardSelfCheckoutButton, cardSendGiftButton, cardTransactionHistoryButton, cardReceiveMoneyButton);
        // ������Ļ�����¼������ܴ���ImageView�İ��¼�̧�������¼���
        OnTouchListener buttonTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageView iv = (ImageView)v;
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					//Ϊ��ť����������ʱ��ͼƬ
					if(imageViewPressedBg.containsKey(iv.getId())){
						iv.setImageResource(imageViewPressedBg.get(iv.getId()));
					}
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					//Ϊ��ť�ָ�������ʾʱ��ͼƬ
					if(imageViewBg.containsKey(iv.getId())){
						iv.setImageResource(imageViewBg.get(iv.getId()));
					}
				}
				return false;
			}
		};
		//Ϊ�˵���ť��Ӵ����¼�����
		cardPayButton.setOnTouchListener(buttonTouchListener);
		cardReloadButton.setOnTouchListener(buttonTouchListener);
		cardSelfCheckoutButton.setOnTouchListener(buttonTouchListener);
		cardSendGiftButton.setOnTouchListener(buttonTouchListener);
		cardTransactionHistoryButton.setOnTouchListener(buttonTouchListener);
		cardReceiveMoneyButton.setOnTouchListener(buttonTouchListener);
        
    }

    /**
     * ��ʼ��imageViewBg��imageViewPressedBg
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