package com.bravo.bravoclient.activities;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.adapters.CardsListAdapter;
import com.bravo.bravoclient.model.Card;
import com.bravo.bravoclient.persistence.CardListDAO;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CardsListActivity extends ListActivity{
	
	private final Logger logger = Logger.getLogger(CardsListActivity.class.getName());
	public final static String CHOOSE_CARD = "CHOOSE_CARD";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.activity_cards_list);
	    
	    final ListView listView = (ListView) findViewById(android.R.id.list);
	    TextView emptyText = (TextView)findViewById(android.R.id.empty);
	    listView.setEmptyView(emptyText);
	    
	    CardListDAO cardListDAO = new CardListDAO(CardsListActivity.this);
	    cardListDAO.openDB();
	    ArrayList<Card> cardList = cardListDAO.getAllCards();
	    cardListDAO.closeDB();
	    
	    // If Card list from db is null, then initiate cardList, make it to empty istead of null
	    if (cardList == null) cardList = new ArrayList<Card>();
	    
	    final CardsListAdapter adapter = new CardsListAdapter(CardsListActivity.this, R.layout.card_list_row, R.id.card_list_content, cardList);
	    
	    listView.setAdapter(adapter);
	    
	    if (cardList.isEmpty() == false) {
	    
	    // Initiate the default card view
	    listView.post(new Runnable() {
			@Override
			public void run() {
				listView.getChildAt(getSelectedCardPosition()).findViewById(R.id.card_list_check_icon).setVisibility(View.VISIBLE);
			}
	    });

	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				// Reset unselected card item
				View resetRowView = (View) parent.getChildAt(getSelectedCardPosition());
				ImageView resetImageView = (ImageView) resetRowView.findViewById(R.id.card_list_check_icon);
				resetImageView.setVisibility(View.INVISIBLE);
				
				// Change the current card list item icon on click
				ImageView selectedImageView = (ImageView) view.findViewById(R.id.card_list_check_icon);
				selectedImageView.setVisibility(View.VISIBLE);
				
				// store selected card, using id here is because position is just a relative position in the current view
				setSelectCardPosition((int)id);
				Toast.makeText(getApplicationContext(),"Default card has been updated", Toast.LENGTH_SHORT).show();
				
				logger.log(Level.INFO, "Card " + id +"has been clicked");
			}
		});

	    }
	}
	
	@Override
	public void onBackPressed() {
		Intent toCardsFragment = new Intent(CardsListActivity.this.getApplicationContext(), MainActivity.class);
		toCardsFragment.putExtra("Activity", "CardsList");
		toCardsFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		CardsListActivity.this.startActivity(toCardsFragment);
		CardsListActivity.this.overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
	}
	
	private void setSelectCardPosition(int rowID) {
		SharedPreferences settings = getSharedPreferences(CHOOSE_CARD, MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("SELECTED_CARD", rowID);
		editor.apply();
	}
	
	private int getSelectedCardPosition() {
		SharedPreferences settings = getSharedPreferences(CHOOSE_CARD, MODE_PRIVATE);
		return settings.getInt("SELECTED_CARD", 0);
	}
}
