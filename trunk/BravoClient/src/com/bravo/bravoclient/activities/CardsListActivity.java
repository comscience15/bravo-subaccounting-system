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
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

public class CardsListActivity extends ListActivity{
	
	private final Logger logger = Logger.getLogger(CardsListActivity.class.getName());
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.activity_cards_list);
	    
	    final ListView listView = (ListView) findViewById(android.R.id.list);
	    CardListDAO cardListDAO = new CardListDAO(CardsListActivity.this);
	    cardListDAO.openDB();
	    final ArrayList<Card> cardList = cardListDAO.getAllCards();
	    cardListDAO.closeDB();
	    
	    final CardsListAdapter adapter = new CardsListAdapter(CardsListActivity.this, R.layout.card_list_row, R.id.cardsList_content, cardList);
	    
	    listView.setAdapter(adapter);
	    
	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long id) {
				logger.log(Level.INFO, "Card has been clicked");
			}
		});
//	    // Create a progress bar to display while the list loads
//        ProgressBar progressBar = new ProgressBar(this);
//        progressBar.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
//                LayoutParams.WRAP_CONTENT));
//        progressBar.setIndeterminate(true);
//        getListView().setEmptyView(progressBar);

	}
	
	@Override
	public void onBackPressed() {
		Intent toCardsFragment = new Intent(CardsListActivity.this.getApplicationContext(), MainActivity.class);
		toCardsFragment.putExtra("Activity", "CardsList");
		toCardsFragment.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		CardsListActivity.this.startActivity(toCardsFragment);
		CardsListActivity.this.overridePendingTransition(R.anim.go_back_enter, R.anim.go_back_out);
	}
	
}
