package com.bodastage.cm.networkaudit.jobs;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class DBResultSetExtractor implements ResultSetExtractor<List<String>> {
	private static final Logger logger = LoggerFactory.getLogger(DBResultSetExtractor.class);
	
	ArrayList<String> list = new ArrayList<String>();
	
	@Override
	public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {

        ResultSetMetaData meta = rs.getMetaData();    

        for (int i = 1; i <=  meta.getColumnCount(); i++) {
            list.add(meta.getColumnName(i));
        }

        return list;
	}


}
