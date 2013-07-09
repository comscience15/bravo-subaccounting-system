package com.bravo.webapp.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.bravo.webapp.bean.Transaction;

public class TransactionRowMapper implements RowMapper<Transaction> {
	private RowMapper<Transaction> rowMapper = null;
	
	public TransactionRowMapper(){
		rowMapper= new BeanPropertyRowMapper<Transaction>(Transaction.class);
	}

	@Override
	public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
		Transaction transaction = rowMapper.mapRow(rs, rowNum);
		transaction.setTotalAmount(transaction.getTotalAmount().abs());
		
		return transaction;
	}

}
