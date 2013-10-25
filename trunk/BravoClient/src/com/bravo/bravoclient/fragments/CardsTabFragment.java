package com.bravo.bravoclient.fragments;
 
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.SparseIntArray;
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
import com.bravo.bravoclient.dialogs.BravoPaymentDialog;

/**
 * The class is for cards tab
 * @author Jia Wenlong
 * @author Daniel
 */
public class CardsTabFragment extends SherlockFragment{
	/** Declare the button icons used in Card Tab */
	private final SparseIntArray imageViewBg = new SparseIntArray();
	/** Declare the pressed button icons used in Card Tab */
	private final static SparseIntArray imageViewPressedBg = new SparseIntArray();
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		/** Create the customized view for Card Tab*/
		View v = inflater.inflate(R.layout.activity_card, null);
	    return v;
    }
    
    @SuppressLint("NewApi")
	@Override
    public void onStart() {
        super.onStart();
        // Setting the background when view is starting
        Drawable myDrawable = getResources().getDrawable(R.drawable.solid_background);
        getView().setBackground(myDrawable);
        
        // The seven buttons in  Card Tab,six for menu,one for refresh
        final ImageView cardPayButton = (ImageView) getView().findViewById(R.id.cardPayButton);
        final ImageView cardReloadButton = (ImageView) getView().findViewById(R.id.cardReloadButton);
        final ImageView cardSelfCheckoutButton = (ImageView) getView().findViewById(R.id.cardSelfCheckoutButton);
        final ImageView cardSendGiftButton = (ImageView) getView().findViewById(R.id.cardSendGiftButton);
        final ImageView cardTransactionHistoryButton = (ImageView) getView().findViewById(R.id.cardTransactionHistoryButton);
        final ImageView cardReceiveMoneyButton = (ImageView) getView().findViewById(R.id.cardReceiveMoneyButton);
        final ImageView cardRefreshButton = (ImageView) getView().findViewById(R.id.cardRefreshButton);
        
        OnClickListener buttonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText cardBalanceEditText = (EditText)getView().findViewById(R.id.cardBalanceEditText);
				cardBalanceEditText.setHint("noButton Clicked!");
				if(v.equals(cardPayButton)) {
					// TODO: when pay button be pressed
					BravoPaymentDialog paymentDialog = new BravoPaymentDialog(getActivity());
					paymentDialog.generateQRCode("bravo, it is working now!!!!");
				} else if (v.equals(cardReloadButton)){
					// TODO: when reload button be pressed
				} else if (v.equals(cardSelfCheckoutButton)){
					// TODO: when selfCheckout button be pressed
				} else if (v.equals(cardSendGiftButton)){
					// TODO: when send gift button be pressed
				} else if (v.equals(cardTransactionHistoryButton)){
					// TODO: when transaction history button be pressed
				} else if (v.equals(cardReceiveMoneyButton)){
					// TODO: when receive money button be pressed
				} else if (v.equals(cardRefreshButton)){
					// TODO: when refresh button be pressed
				}
			}
        };
        // add the onClick listener for buttons
        cardPayButton.setOnClickListener(buttonListener);
        cardReloadButton.setOnClickListener(buttonListener);
        cardSelfCheckoutButton.setOnClickListener(buttonListener);
        cardSendGiftButton.setOnClickListener(buttonListener);
        cardTransactionHistoryButton.setOnClickListener(buttonListener);
        cardReceiveMoneyButton.setOnClickListener(buttonListener);
        cardRefreshButton.setOnClickListener(buttonListener);
        
        initImageViewBgMap(cardPayButton, cardReloadButton, cardSelfCheckoutButton, cardSendGiftButton, cardTransactionHistoryButton, cardReceiveMoneyButton);
        
        OnTouchListener buttonTouchListener = new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				ImageView iv = (ImageView)v;
				if(event.getAction()==MotionEvent.ACTION_DOWN){
					// when pressed, use the pressed button icon
					if(imageViewPressedBg.indexOfKey(iv.getId()) >= 0){
						iv.setImageResource(imageViewPressedBg.get(iv.getId()));
					}
				}else if(event.getAction()==MotionEvent.ACTION_UP){
					// when released, use the normal button icon
					if(imageViewBg.indexOfKey(iv.getId()) >= 0){
						iv.setImageResource(imageViewBg.get(iv.getId()));
					}
				}
				return false;
			}
		};
		// add the touch listener for buttons
		cardPayButton.setOnTouchListener(buttonTouchListener);
		cardReloadButton.setOnTouchListener(buttonTouchListener);
		cardSelfCheckoutButton.setOnTouchListener(buttonTouchListener);
		cardSendGiftButton.setOnTouchListener(buttonTouchListener);
		cardTransactionHistoryButton.setOnTouchListener(buttonTouchListener);
		cardReceiveMoneyButton.setOnTouchListener(buttonTouchListener);
        
    }

    /**
     * initialize imageViewBg and imageViewPressedBg
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