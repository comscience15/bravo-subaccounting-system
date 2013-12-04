package com.bravo.bravoclient.persistence;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bravo.bravoclient.model.Card;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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
		localDB = localDBHelper.getWritableDatabase();
	}
	
	/**
	 * Close the opened database
	 */
	public void closeDB() {
		localDBHelper.close();
	}

	/**
	 * Insert single card into local databse
	 * @param card
	 */
	public void insertCard(Card card) {
		String cardID = card.getCardId();
		double loyaltyPoint = card.getLoyaltyPoint();
		String merchantAccNo = card.getMerchantAccNo();
		double balance = card.getBalance();
		
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_CARD_ID, cardID);
		values.put(SQLiteHelper.COLUMN_LOYALTY_POINT, loyaltyPoint);
		values.put(SQLiteHelper.COLUMN_MERCHANT_ACCOUNT_NUMBER, merchantAccNo);
		values.put(SQLiteHelper.COLUMN_BALANCE, balance);
		
		localDB.beginTransaction();
		try {
			long insertId = localDB.insert(SQLiteHelper.TABLE_NAME, null, values);
			logger.log(Level.INFO, "Card has been insterted into the db with the insertID: " + insertId);
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
	public void insertCards(ArrayList<Card> cardList) {
		for (Card card : cardList) {
			insertCard(card);
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
		
	}
	
	/**
	 * Get particular card based on cardID
	 * @param cardID
	 * @return
	 */
	public Card getCard(String cardID) {
		return null;
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
			logger.log(Level.INFO, "Get card list with: " + cursor.getCount() + "cards");
			
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Card card = cursorToCard(cursor);
				cardList.add(card);
				cursor.moveToNext();
			}
			cursor.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to get all cards from db");
		} finally {
			localDB.endTransaction();
		}
		return cardList;
	}
	
	private Card cursorToCard(Cursor cursor) {
		Card card = new Card();
		card.setCardId(cursor.getString(0));
		card.setLoyaltyPoint(cursor.getInt(1));
		card.setMerchantAccNo(cursor.getString(2));
		card.setBalance(cursor.getDouble(3));
		return card;
	}
	
}
