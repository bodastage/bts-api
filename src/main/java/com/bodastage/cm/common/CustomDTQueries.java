package com.bodastage.cm.common;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.datatables.mapping.Column;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;


/**
 * Runs custom data table queries
 * 
 */
public class CustomDTQueries {
	private JdbcTemplate jdbcTemplate;
	private String tablename = null;

	private static final Logger logger = LoggerFactory.getLogger(CustomDTQueries.class);

	/**
	 * Build data table query
	 * 
	 * @param input
	 * @param paginate
	 * @param isCount
	 * @return
	 */
	private String buildDTQuery(DataTablesInput input, boolean paginate, boolean isCount) {
		String sql = "SELECT * FROM " + this.tablename;
		if (isCount == true) {
			sql = "SELECT count(*) FROM " + this.tablename + " T1 ";
		}

		List<Column> columns = input.getColumns();
		int columnSearchCnt = 0;
		String inputSearchValue = input.getSearch().getValue();
		for (int i = 0; i < columns.size(); i++) {
			Column column = columns.get(i);
			if (column.getSearchable() == true) {
				String columnName = column.getName();

				String columnSearchValue = column.getSearch().getValue();
				if (columnSearchValue.length() > 0) {
					if (columnSearchCnt == 0)
						sql += " WHERE ";
					if (columnSearchCnt > 0)
						sql += " OR ";
					sql += " UPPER(" + columnName + ") LIKE UPPER('%" + columnSearchValue + "%') ";
					columnSearchCnt++;
				}

				if (columnSearchValue.length() == 0 && inputSearchValue.length() > 0) {
					if (columnSearchCnt == 0)
						sql += " WHERE ";
					if (columnSearchCnt > 0)
						sql += " OR ";
					sql += " UPPER(" + columnName + ") LIKE UPPER('%" + inputSearchValue + "%') ";
					columnSearchCnt++;
				}

			}
		}

		// @TODO: /check datasource drive to appropriate handle the pagination
		if (paginate == true) {
			int start = input.getStart();
			int length = input.getLength();
			sql += " LIMIT " + start + " , " + length; // for mysql
		}

		return sql;
	}

	/**
	 * Set datasource
	 * 
	 * @param dataSource
	 */
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * findAll implementation
	 * 
	 * @param input
	 * @return
	 */
	public DataTablesOutput<Map<String, Object>> findAll(DataTablesInput input) {
		DataTablesOutput<Map<String, Object>> dtOutput = new DataTablesOutput<Map<String, Object>>();

		int cnt = this.getCount(input);

		dtOutput.setData(this.getList(input));
		dtOutput.setDraw(input.getDraw());
		dtOutput.setRecordsFiltered(cnt);
		dtOutput.setRecordsTotal(cnt);
		return dtOutput;
	}

	/**
	 * Get record count
	 * 
	 * @param input
	 * @return
	 */
	public int getCount(DataTablesInput input) {
		String sql = this.buildDTQuery(input, false, true);
		return this.jdbcTemplate.queryForObject(sql, Integer.class);
	}

	/**
	 * Get list
	 * 
	 * @param input
	 * @return
	 */
	public List<Map<String, Object>> getList(DataTablesInput input) {

		String sql = this.buildDTQuery(input, true, false);
		return this.jdbcTemplate.queryForList(sql);
	}

	/**
	 * Get columns
	 * 
	 * @param input
	 * @return
	 */
	public List<DBTableColumn> getColumns(DataTablesInput input) {
		String queryBuilder = this.buildDTQuery(input, false, false);
		List<DBTableColumn> columns = new ArrayList<DBTableColumn>();
		this.jdbcTemplate.query(queryBuilder.toString(), new ResultSetExtractor<Integer>() {
			public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				for (int i = 1; i <= columnCount; i++) {
					DBTableColumn column = new DBTableColumn();
					column.setName(rsmd.getColumnName(i));
					column.setType(rsmd.getColumnTypeName(i));
					column.setTypeCode(rsmd.getColumnType(i));
					// column.setTableName(sqlTable.getName().toUpperCase());
					columns.add(column);
				}

				return columnCount;
			}
		});

		return columns;
	}

	/**
	 * Set the table name
	 * 
	 * @param tablename
	 * @return void
	 */
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

}