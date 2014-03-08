package com.bravo.bravoclient.fragments;
 
import java.util.logging.Level;
import java.util.logging.Logger;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
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
import android.widget.Toast;
 
import com.actionbarsherlock.app.SherlockFragment;
import com.bravo.bravoclient.R;
import com.bravo.bravoclient.activities.CardsListActivity;
import com.bravo.bravoclient.activities.LoginActivity;
import com.bravo.bravoclient.activities.MainActivity;
import com.bravo.bravoclient.activities.ReloadMoneyActivity;
import com.bravo.bravoclient.activities.SendMoneyActivity;
import com.bravo.bravoclient.async.AsyncGetCardsList;
import com.bravo.bravoclient.async.AsyncLogin;
import com.bravo.bravoclient.async.AsyncRegister;
import com.bravo.bravoclient.dialogs.BravoAlertDialog;
import com.bravo.bravoclient.dialogs.BravoPaymentDialog;
import com.bravo.bravoclient.model.Card;
import com.bravo.bravoclient.persistence.CardListDAO;
import com.bravo.bravoclient.util.Encryption;

/**
 * The class is for cards tab
 * @author Jia Wenlong
 * @author Daniel danniel1205@gmail.com
 */
public class CardsTabFragment extends SherlockFragment implements CreateNdefMessageCallback{
	/** Declare the button icons used in Card Tab */
	private final SparseIntArray imageViewBg = new SparseIntArray();
	/** Declare the pressed button icons used in Card Tab */
	private final static SparseIntArray imageViewPressedBg = new SparseIntArray();
	private final static Logger logger = Logger.getLogger(CardsTabFragment.class.getName());
	private static String cardId = "";
	private static String encryptedData = "";
	
	private NfcAdapter nfcAdapter;
	
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
        final ImageView cardMoneyTransfer = (ImageView) getView().findViewById(R.id.cardMoneyTransferButton);
        final ImageView cardTransactionHistoryButton = (ImageView) getView().findViewById(R.id.cardTransactionHistoryButton);
        final ImageView cardCardsListButton = (ImageView) getView().findViewById(R.id.cardCardsListButton);
        
        OnClickListener buttonListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (isAuthenticated() == false) return;
				if(v.equals(cardPayButton)) {
					payImpl();
				} else if (v.equals(cardReloadButton)){
					reloadImpl();
				} else if (v.equals(cardSelfCheckoutButton)){
					// TODO: when selfCheckout button be pressed
				} else if (v.equals(cardMoneyTransfer)){
					// TODO: when send gift button be pressed
					moneyTransferImpl();
				} else if (v.equals(cardTransactionHistoryButton)){
					// TODO: when transaction history button be pressed
				} else if (v.equals(cardCardsListButton)){
					cardsListImpl();
				}
			}
        };
        // add the onClick listener for buttons
        cardPayButton.setOnClickListener(buttonListener);
        cardReloadButton.setOnClickListener(buttonListener);
        cardSelfCheckoutButton.setOnClickListener(buttonListener);
        cardMoneyTransfer.setOnClickListener(buttonListener);
        cardTransactionHistoryButton.setOnClickListener(buttonListener);
        cardCardsListButton.setOnClickListener(buttonListener);
        
        initImageViewBgMap(cardPayButton, cardReloadButton, cardSelfCheckoutButton, cardMoneyTransfer, cardTransactionHistoryButton, cardCardsListButton);
        
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
		cardMoneyTransfer.setOnTouchListener(buttonTouchListener);
		cardTransactionHistoryButton.setOnTouchListener(buttonTouchListener);
		cardCardsListButton.setOnTouchListener(buttonTouchListener);
		
		
		String fromActivity = getActivity().getIntent().getStringExtra("Activity");
		if (fromActivity != null) {
        	if (fromActivity.equals("Login") || fromActivity.equals("Register") || fromActivity.equals("Reload_Money") || fromActivity.equals("Send_Money")) {
        		// Get card list in background once login successfully
        		new AsyncGetCardsList(getActivity(), getView()).execute(getString(R.string.IP_Address));
        		getSelectedCard();
        	} else if (fromActivity.equals("CardsList")) {
        		// Display card balance without network updating
        		displayCardBalance();
        	}
		} else {
			// if not from other intent, just display the balance.
			displayCardBalance();
		}
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
			final ImageView cardMoneyTransferButton,
			final ImageView cardTransactionHistoryButton,
			final ImageView cardReceiveMoneyButton) {
		if(imageViewBg != null){
        	imageViewBg.put(cardPayButton.getId(),R.drawable.pay);
        	imageViewBg.put(cardReloadButton.getId(),R.drawable.reload);
        	imageViewBg.put(cardSelfCheckoutButton.getId(),R.drawable.self_checkout);
        	imageViewBg.put(cardMoneyTransferButton.getId(),R.drawable.money_transfer);
        	imageViewBg.put(cardTransactionHistoryButton.getId(),R.drawable.transaction_history);
        	imageViewBg.put(cardReceiveMoneyButton.getId(),R.drawable.receive_money);
        }
        
        if(imageViewPressedBg != null){
        	imageViewPressedBg.put(cardPayButton.getId(),R.drawable.pay_pressed);
        	imageViewPressedBg.put(cardReloadButton.getId(),R.drawable.reload_pressed);
        	imageViewPressedBg.put(cardSelfCheckoutButton.getId(),R.drawable.self_checkout_pressed);
        	imageViewPressedBg.put(cardMoneyTransferButton.getId(),R.drawable.send_gift_pressed);
        	imageViewPressedBg.put(cardTransactionHistoryButton.getId(),R.drawable.transaction_history_pressed);
        	imageViewPressedBg.put(cardReceiveMoneyButton.getId(),R.drawable.receive_money_pressed);
        }
	}
	
	/**
	 * Get selected card
	 * @return
	 */
	private Card getSelectedCard() {
		CardListDAO cardListDAO = new CardListDAO(getActivity());
		SharedPreferences settings = getActivity().getSharedPreferences(CardsListActivity.CHOOSE_CARD, getActivity().MODE_PRIVATE);
		int selectedCardRowId = settings.getInt("SELECTED_CARD", 0);
		
		cardListDAO.openDB();
		Card card = cardListDAO.getCard(selectedCardRowId);
		cardListDAO.closeDB();
		
		//check if card is null. It is possible that getCardList API does not work, so there is no cards in local db
		cardId = card == null ? "" : card.getCardId();
		return card;
	}
	
	/**
	 * Display the current card balance on edit text field
	 */
	private void displayCardBalance() {
		Card selectedCard = getSelectedCard();
		String balanceText = ""; // Default value if the selected card returns null
		
		// Check if selected card is null. This can because db issue or just registered user has no card
		if (selectedCard != null) {
			balanceText = String.valueOf(selectedCard.getBalance());
			logger.info("Card balance is: " + selectedCard.getBalance());
		} 
		
		EditText cardBalanceEditText = (EditText)getView().findViewById(R.id.cardBalanceEditText);
		if (balanceText.equals("") == false) {
			cardBalanceEditText.setText(getString(R.string.currency) + balanceText);
		}
	}
	
	private void showLoginActivity() {
		Intent toLoginActivity = new Intent(getActivity(), LoginActivity.class);
		getActivity().startActivity(toLoginActivity);
		getActivity().overridePendingTransition(R.anim.login_enter, R.anim.login_out);
	}
	
	private void payImpl() {
		Card defaultCard = getSelectedCard();
		
		// Do not do payment if get selected card returns null
		if (defaultCard == null) {
			Toast.makeText(getActivity(), "You did not add card for payment", Toast.LENGTH_LONG).show();
			return;
		}
		
		logger.log(Level.INFO, "Default cardID is: " + defaultCard.getCardId());
		
		// Checking the NFC availability
		logger.info("Checking the availability of NFC");
		nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
		if (nfcAdapter == null || nfcAdapter.isEnabled() == false) {
			logger.warning("NFC is not enabled");
			Toast.makeText(getActivity(), "NFC is not enabled", Toast.LENGTH_LONG).show();
		} else {
			nfcAdapter.setNdefPushMessageCallback(this, getActivity());
		}
		
		// Generate the encrypted data, the public key has already gotten when login
		Encryption encryptionObj = AsyncLogin.EncryptionObj == null ? AsyncRegister.EncryptionObj : AsyncLogin.EncryptionObj;
		String inputData = "{\"cardID\":\""+ defaultCard.getCardId() +"\", \"customerTimestamp\":\""+ System.currentTimeMillis() +"\"}";
		encryptedData = encryptionObj.generateEncryptedData(inputData);
		
		// Generate QRCode for encrypted data
		if (encryptedData.equals("") == false) {
			BravoPaymentDialog paymentDialog = new BravoPaymentDialog(getActivity());
			paymentDialog.generateQRCode(encryptedData);
		} else {
			logger.warning("Cannot generate encrypted data");
			Toast.makeText(getActivity(), "Cannot generate encrypted data for payment", Toast.LENGTH_LONG).show();
		}
	}
	
	private void cardsListImpl() {
		Intent toCardsListActivity = new Intent(getActivity(), CardsListActivity.class);
		getActivity().startActivity(toCardsListActivity);
		getActivity().overridePendingTransition(R.anim.cards_list_enter, R.anim.cards_list_out);
	}
	
	private void reloadImpl() {
		// Do not do reloading if there is no card has been selected
		if (cardId.equals("") || cardId == null) {
			Toast.makeText(getActivity(), "You did not added card for reloading", Toast.LENGTH_LONG).show();
			return;
		} 
		
		Intent toReloadMoneyActivity = new Intent(getActivity(), ReloadMoneyActivity.class);
		toReloadMoneyActivity.putExtra("cardID", cardId);
		getActivity().startActivity(toReloadMoneyActivity);
		// TODO: we should change the animation in the future
		getActivity().overridePendingTransition(R.anim.cards_list_enter, R.anim.cards_list_out);
	}
	
	private void moneyTransferImpl() {
		AlertDialog.Builder moneyTransferBuilder = new AlertDialog.Builder(getActivity());
		moneyTransferBuilder.setTitle(R.string.title_money_transfer_dialog);
		// TODO: Set the view for this dialog
		moneyTransferBuilder.setItems(R.array.money_transfer_array, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					// Go to SendMoneyActivity
					Intent toSendMoneyActivity = new Intent(getActivity(), SendMoneyActivity.class);
					toSendMoneyActivity.putExtra("cardID", cardId);
					getActivity().startActivity(toSendMoneyActivity);
					// TODO: we should change the animation in the future
					getActivity().overridePendingTransition(R.anim.cards_list_enter, R.anim.cards_list_out);
					
				} else if (which == 1) {
					/** This is for get money **/
					// Get selected card first, the money will be deposited into your selected card
					getSelectedCard();
					
					// Encrypted with the public key for security purpose
					Encryption encryptionObj = AsyncLogin.EncryptionObj == null ? AsyncRegister.EncryptionObj : AsyncLogin.EncryptionObj;
					String inputData = "{\"cardID\":\""+ cardId +"\", \"customerTimestamp\":\""+ System.currentTimeMillis() +"\"}";
					encryptedData = encryptionObj.generateEncryptedData(inputData);
					
					logger.info("Encrypted data is: " + encryptedData);
					
					// Show the encrypted cardID
					BravoPaymentDialog paymentDialog = new BravoPaymentDialog(getActivity());
					paymentDialog.generateQRCode(encryptedData);
				}
			}
		});
		
		AlertDialog dialog = moneyTransferBuilder.create();
		dialog.show();
	}
	
	/**
	 * Check if current user is authenticated
	 * @return
	 */
	private boolean isAuthenticated() {
		if (MainActivity.getLoginStatus() == false) {
			showLoginActivity();
			return false;
		} else {
			return true;
		}
	}

	/**
	 * The callback method to send nfc message
	 */
	@SuppressLint("NewApi")
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		logger.info("Creating the NFC message");
		String nfcText = encryptedData;
		NdefMessage msg = new NdefMessage(
                new NdefRecord[] { NdefRecord.createMime(
                        "application/com.bravo.bravoclient.beam", nfcText.getBytes())
        });
		return msg;
	}
	
}