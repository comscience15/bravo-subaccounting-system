package com.bravo.bravoclient.persistence;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import com.bravo.bravoclient.model.Card;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class CardListDAO {
	private Logger logger = Logger.getLogger(CardListDAO.class.getName());
	private String[] allColumns = {SQLiteHelper.COLUMN_CARD_ID, SQLiteHelper.COLUMN_LOYALTY_POINT, 
			SQLiteHelper.COLUMN_MERCHANT_ACCOUNT_NUMBER, SQLiteHelper.COLUMN_BALANCE};
	private SQLiteDatabase localDB;
	private SQLiteHelper localDBHelper;
	
	public CardListDAO(Context context) {
		localDBHelper = new SQLiteHelper(context);
	}
	
	/**
	 * Open a the database for reading and writing
	 */
	public void openDB() {
		logger.log(Level.INFO, "Open DB");
		localDB = localDBHelper.getWritableDatabase();
	}
	
	/**
	 * Close the opened database
	 */
	public void closeDB() {
		logger.log(Level.INFO, "Close DB");
		localDBHelper.close();
	}

	/**
	 * Insert single card into local database
	 * @param card
	 */
	public void insertCard(Card card, int insertRowID) {
		String cardID = card.getCardId();
		double loyaltyPoint = card.getLoyaltyPoint();
		String merchantAccNo = card.getMerchantAccNo();
		double balance = card.getBalance();
		int rowID = insertRowID;
		
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_ID, rowID);
		values.put(SQLiteHelper.COLUMN_CARD_ID, cardID);
		values.put(SQLiteHelper.COLUMN_LOYALTY_POINT, loyaltyPoint);
		values.put(SQLiteHelper.COLUMN_MERCHANT_ACCOUNT_NUMBER, merchantAccNo);
		values.put(SQLiteHelper.COLUMN_BALANCE, balance);
		
		localDB.beginTransaction();
		try {
			long insertId = localDB.insert(SQLiteHelper.TABLE_NAME, null, values);
			logger.log(Level.INFO, "Card has been insterted into the db with the insertID: " + insertId + " , the rowID is: " + rowID);
			localDB.setTransactionSuccessful();
		} catch (Exception e){
			logger.log(Level.SEVERE, "Falied to insert card into db");
		} finally {
			localDB.endTransaction();
		}
	}
	
	/**
	 * Insert card list into the local database
	 * @param cardList
	 */
	public void insertCards(ArrayList<JSONObject> cardList) {
		if (cardList == null) return;
		
		// Delete all current cards before inserting cardList
		deleteAllCards();
		
		Iterator<JSONObject> cardListIterator = cardList.iterator();
		int nextRowID = getNumberOfRows();
		try {
			while (cardListIterator.hasNext()) {
				Card card = JSONToCard(cardListIterator.next());
				insertCard(card, nextRowID++);
			}
		} catch (JSONException e) {
			logger.log(Level.SEVERE, "Failed to parse JSONObject to card");
		} finally {
			
		}
	}
	
	/**
	 * Delete particular card based on cardID
	 * @param card
	 */
	public void deleteCard(Card card) {
		String cardID = card.getCardId();
		localDB.beginTransaction();
		try {
			int numberAffected = localDB.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.COLUMN_CARD_ID + " = " + cardID , null);
			localDB.setTransactionSuccessful();
			logger.log(Level.INFO, "Card has been deleted from db, " + numberAffected + " card has been deleted" );
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to delete card from db");
		} finally {
			localDB.endTransaction();
		}
	}
	
	/**
	 * Delete all card
	 */
	public void deleteAllCards() {
		localDB.beginTransaction();
		try {
			int numberAffected = localDB.delete(SQLiteHelper.TABLE_NAME, null , null);
			localDB.setTransactionSuccessful();
			logger.log(Level.INFO, "Cards have been deleted from db, " + numberAffected + " cards have been deleted" );
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to delete all cards from db");
		} finally {
			localDB.endTransaction();
		}
	}
	
	/**
	 * Get particular card based on cardID
	 * @param cardID
	 * @return
	 */
	public Card getCard(String cardID) {
		Card card = null;
		localDB.beginTransaction();
		try {
			Cursor cursor = localDB.query(SQLiteHelper.TABLE_NAME, allColumns, SQLiteHelper.COLUMN_CARD_ID + "=?", new String[] { cardID }, null, null, null);
			
			logger.log(Level.INFO, "Get the card with card ID" + cardID);
			
			localDB.setTransactionSuccessful();
			cursor.moveToFirst();
			card = cursorToCard(cursor);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to get particular card in db");
		} finally {
			localDB.endTransaction();
		}
		return card;
	}
	
	/**
	 * Get the particular card based on the RowID
	 * @param ColumnId
	 * @return
	 */
	public Card getCard(int RowID) {
		Card card = null;
		localDB.beginTransaction();
		try {
			Cursor cursor = localDB.query(SQLiteHelper.TABLE_NAME, allColumns, SQLiteHelper.COLUMN_ID + "=?", new String[] { String.valueOf(RowID) }, null, null, null);
			localDB.setTransactionSuccessful();
			cursor.moveToFirst();
			card = cursorToCard(cursor);
			
			logger.log(Level.INFO, MessageFormat.format("Get the {0} card with cardID {1}", new Object[] {RowID, card.getCardId()}));

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to get particular card in db");
		} finally {
			localDB.endTransaction();
		}
		return card;
	}
	
	/**
	 * Get all cards in the local database
	 * @return
	 */
	public ArrayList<Card> getAllCards() {
		ArrayList<Card> cardList = new ArrayList<Card>();
		
		localDB.beginTransaction();
		try {
			Cursor cursor = localDB.query(SQLiteHelper.TABLE_NAME, allColumns, null, null, null, null, null);
			localDB.setTransactionSuccessful();
			logger.log(Level.INFO, "Get card list with: " + cursor.getCount() + " cards");
			
			cursor.moveToFirst();
			while (cursor.isAfterLast() == false) {
				Card card = cursorToCard(cursor);
				cardList.add(card);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			logger.log(Level.WARNING, "Failed to get all cards from db");
		} finally {
			localDB.endTransaction();
		}
		return cardList;
	}
	
	public void updateCardBalance(int RowID, double balance) {
		localDB.beginTransaction();
		try {
			ContentValues cv = new ContentValues();
			cv.put(SQLiteHelper.COLUMN_BALANCE, balance); //These Fields should be your String values of actual column names
			localDB.update(SQLiteHelper.TABLE_NAME, cv, SQLiteHelper.COLUMN_ID + "=" + RowID, null);
			localDB.setTransactionSuccessful();
			
			logger.log(Level.INFO, MessageFormat.format("Update the {0} card with balance {1}", new Object[] {RowID, balance}));

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to get particular card in db");
		} finally {
			localDB.endTransaction();
		}
	}
	
	private Card cursorToCard(Cursor cursor) {
		Card card = new Card();
		card.setCardId(cursor.getString(0));
		card.setLoyaltyPoint(cursor.getInt(1));
		card.setMerchantAccNo(cursor.getString(2));
		card.setBalance(cursor.getDouble(3));
		return card;
	}
	
	private Card JSONToCard(JSONObject JSONCard) throws JSONException {
		Card card = new Card();
		card.setCardId(JSONCard.getString("cardID"));
		card.setLoyaltyPoint(JSONCard.getInt("loyaltyPoint"));
		card.setMerchantAccNo(JSONCard.getString("merchantAccNo"));
		card.setBalance(JSONCard.getDouble("balance"));
		return card;
	}
	
	private int getNumberOfRows() {
		localDB.beginTransaction();
		try {
		Cursor cursor = localDB.query(SQLiteHelper.TABLE_NAME, allColumns, null, null, null, null, null);
		localDB.setTransactionSuccessful();
		return cursor.getCount();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to get the current row ID");
			throw new SQLiteException();
		} finally {
			localDB.endTransaction();
		}
	}
	
}
