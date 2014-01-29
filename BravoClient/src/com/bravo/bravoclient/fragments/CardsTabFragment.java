package com.bravo.bravoclient.fragments;
 
import java.util.logging.Level;
import java.util.logging.Logger;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
 
import com.actionbarsherlock.app.SherlockFragment;
import com.bravo.bravoclient.R;
import com.bravo.bravoclient.activities.CardsListActivity;
import com.bravo.bravoclient.activities.LoginActivity;
import com.bravo.bravoclient.activities.MainActivity;
import com.bravo.bravoclient.async.AsyncGetCardsList;
import com.bravo.bravoclient.async.AsyncLogin;
import com.bravo.bravoclient.async.AsyncRegister;
import com.bravo.bravoclient.dialogs.BravoPaymentDialog;
import com.bravo.bravoclient.model.Card;
import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.bravoclient.util.Encryption;

/**
 * The class is for cards tab
 * @author Jia Wenlong
 * @author Daniel danniel1205@gmail.com
 */
public class CardsTabFragment extends SherlockFragment{
	/** Declare the button icons used in Card Tab */
	private final SparseIntArray imageViewBg = new SparseIntArray();
	/** Declare the pressed button icons used in Card Tab */
	private final static SparseIntArray imageViewPressedBg = new SparseIntArray();
	private Logger logger = Logger.getLogger(CardsTabFragment.class.getName());
	
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
        
        // Get the device screen dimension and calculate the perfect image size
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		Point windowSize = new Point();
		wm.getDefaultDisplay().getSize(windowSize);
		
    	ImageView cardView = (ImageView) getView().findViewById(R.id.card);
    	cardView.getLayoutParams().height = (int) (windowSize.y * 1.2/4);
    	cardView.getLayoutParams().width = (int) (windowSize.x * 3/4);
        
        // The seven buttons in  Card Tab,six for menu,one for refresh
        final ImageView cardPayButton = (ImageView) getView().findViewById(R.id.cardPayButton);
        final ImageView cardReloadButton = (ImageView) getView().findViewById(R.id.cardReloadButton);
        final ImageView cardSelfCheckoutButton = (ImageView) getView().findViewById(R.id.cardSelfCheckoutButton);
        final ImageView cardSendGiftButton = (ImageView) getView().findViewById(R.id.cardSendGiftButton);
        final ImageView cardTransactionHistoryButton = (ImageView) getView().findViewById(R.id.cardTransactionHistoryButton);
        final ImageView cardCardsListButton = (ImageView) getView().findViewById(R.id.cardCardsListButton);
        final ImageView cardRefreshButton = (ImageView) getView().findViewById(R.id.cardRefreshButton);
        
        OnClickListener buttonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText cardBalanceEditText = (EditText)getView().findViewById(R.id.cardBalanceEditText);
				cardBalanceEditText.setHint("noButton Clicked!");
				if (isAuthenticated() == false) return;
				if(v.equals(cardPayButton)) {
					// TODO: when pay button be pressed
					payImpl();
				} else if (v.equals(cardReloadButton)){
					// TODO: when reload button be pressed
				} else if (v.equals(cardSelfCheckoutButton)){
					// TODO: when selfCheckout button be pressed
				} else if (v.equals(cardSendGiftButton)){
					// TODO: when send gift button be pressed
				} else if (v.equals(cardTransactionHistoryButton)){
					// TODO: when transaction history button be pressed
				} else if (v.equals(cardCardsListButton)){
					// TODO: when cards list button be pressed
					cardsListImpl();
				} else if (v.equals(cardRefreshButton)){
					// TODO: when refresh button be pressed
		        	new AsyncGetCardsList(getActivity()).execute(getString(R.string.IP_Address));
				}
			}
        };
        // add the onClick listener for buttons
        cardPayButton.setOnClickListener(buttonListener);
        cardReloadButton.setOnClickListener(buttonListener);
        cardSelfCheckoutButton.setOnClickListener(buttonListener);
        cardSendGiftButton.setOnClickListener(buttonListener);
        cardTransactionHistoryButton.setOnClickListener(buttonListener);
        cardCardsListButton.setOnClickListener(buttonListener);
        cardRefreshButton.setOnClickListener(buttonListener);
        
        initImageViewBgMap(cardPayButton, cardReloadButton, cardSelfCheckoutButton, cardSendGiftButton, cardTransactionHistoryButton, cardCardsListButton);
        
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
		cardCardsListButton.setOnTouchListener(buttonTouchListener);
        
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
	
	private Card getDefaultCard() {
		CardListDAO cardListDAO = new CardListDAO(getActivity());
		cardListDAO.openDB();
		Card card = cardListDAO.getCard(0);
		cardListDAO.closeDB();	
		return card;
	}
	
	private void showLoginActivity() {
		Intent toLoginActivity = new Intent(getActivity(), LoginActivity.class);
		getActivity().startActivity(toLoginActivity);
		getActivity().overridePendingTransition(R.anim.login_enter, R.anim.login_out);
	}
	
	private void payImpl() {
		// TODO: This should be changed once the listView has been implemented
		Card defaultCard = getDefaultCard();
		logger.log(Level.INFO, "Default cardID is: " + defaultCard.getCardId());
		
		// Generate the encrypted data, the public key has already gotten when login
		Encryption encryptionObj = AsyncLogin.EncryptionObj == null ? AsyncRegister.EncryptionObj : AsyncLogin.EncryptionObj;
		String encryptedData = encryptionObj.generateEncryptedData(defaultCard.getCardId());
		
		// Generate QRCode for encrypted data
		BravoPaymentDialog paymentDialog = new BravoPaymentDialog(getActivity());
		paymentDialog.generateQRCode(encryptedData);
	}
	
	private void cardsListImpl() {
		Intent toCardsListActivity = new Intent(getActivity(), CardsListActivity.class);
		getActivity().startActivity(toCardsListActivity);
		getActivity().overridePendingTransition(R.anim.cards_list_enter, R.anim.cards_list_out);
	}
	
	private boolean isAuthenticated() {
		if (MainActivity.getLoginStatus() == false) {
			showLoginActivity();
			return false;
		} else {
			return true;
		}
	}
	
}