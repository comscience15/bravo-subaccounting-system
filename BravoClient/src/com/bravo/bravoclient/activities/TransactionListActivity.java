package com.bravo.bravoclient.activities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.bravoclient.R;
import com.bravo.bravoclient.adapters.TransactionListAdapter;
import com.bravo.bravoclient.model.Transaction;
import com.bravo.https.util.HttpResponseHandler;

import android.os.Bundle;
import android.app.ListActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class TransactionListActivity extends ListActivity {

	private static final Logger logger = Logger.getLogger(TransactionListActivity.class.getName());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction_list);
		
		final ListView listView = (ListView) findViewById(android.R.id.list);
	    TextView emptyText = (TextView)findViewById(android.R.id.empty);
	    listView.setEmptyView(emptyText);
	    
	    ArrayList<JSONObject> transactionListJSON = null;
	    ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
	    try {
	    	transactionListJSON = HttpResponseHandler.toArrayList(getIntent().getStringExtra("jsonResponse"));
		} catch (JSONException e) {
			logger.severe(e.getMessage());
		}
	    
	    // Initiate Transaction, and add to list
	    Iterator<JSONObject> iterator = transactionListJSON.iterator();
	    while (iterator.hasNext()) {
	    	Transaction transaction = new Transaction();
	    	JSONObject currentJSONObject = iterator.next();
	    	try {
				transaction.setReceiptNo(currentJSONObject.getString("receiptNo"));
				transaction.setTransactionID(currentJSONObject.getString("transactionId"));
				transaction.setTransactionType(currentJSONObject.getString("transactionType"));
				transaction.setTransactionDate(new Date(Long.valueOf(currentJSONObject.getString("transactionDate"))));
				transaction.setTotalAmount(new BigDecimal(currentJSONObject.getString("totalAmount")));
			} catch (JSONException e) {
				logger.severe(e.getMessage());
			}
	    	transactionList.add(transaction);
	    	transaction = null; // Garbage collection
	    }
	   
	    final TransactionListAdapter adapter = new TransactionListAdapter(TransactionListActivity.this, R.layout.transaction_list_row, R.id.transaction_list_content, transactionList);
	    
	    listView.setAdapter(adapter);
	    
	    final ArrayList<Transaction> transactions = transactionList;
	    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				logger.info("Selected transactino history with the transactionId is: " + transactions.get(position).getTransactionID());
			}
		});
	}

	

}
