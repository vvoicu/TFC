package com.monarch.tools.connectors;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;

import com.monarch.tools.utils.ConfigUtils;

public class SqlDbConnection {

	static final String DB_URL = ConfigUtils.getProperty("DB_URL");
	static final String USER = ConfigUtils.getProperty("DB_USER");
	static final String PASS = ConfigUtils.getProperty("DB_PASS");

	public static Properties setSqlConnectionProperties() {
		Properties result = new Properties();
		result.put("noTimezoneConversionForDateType", true);
		return result;
	}

	/**
	 * Connect to DB - data is grabbed from the config files - and execute the given
	 * query
	 * 
	 * @param dbName
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, String>> createDbConnection(String dbName, String query) {
		List<HashMap<String, String>> results = new ArrayList<>();
		try {
			Connection connNow = DriverManager.getConnection(DB_URL, USER, PASS);
			Statement statement = connNow.createStatement();
			ResultSet rs = statement.executeQuery(query);
			results = convertResultSetToList(rs);

			DbUtils.close(connNow);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * Grab Stream data from the sql database. columnName will identify the specific
	 * column in case the query parameter is generally written and contains more
	 * columns.
	 * 
	 * @param dbName
	 * @param query
	 * @param columnName
	 * @return {@link InputStream}
	 * @throws SQLException
	 */
	public InputStream createDbConnectionDataAsInputStream(String dbName, String query, String columnName) throws SQLException {

		Connection connNow = DriverManager.getConnection(DB_URL, USER, PASS);
		connNow.setCatalog(dbName);
		Statement statement = connNow.createStatement();
		ResultSet rs = statement.executeQuery(query);

		InputStream isData = null;

		// if (rs.getFetchSize() > 0)
		if (rs.next()) {
			isData = rs.getBinaryStream(columnName);
		}
		// else
		// System.out.println("Could not extract kdm key. No entries found for query: "
		// + query);
		DbUtils.close(connNow);
		return isData;
	}

	/**
	 * Convert ResultSet to java list with hashMap key,value It is a very general
	 * representation of sql return results
	 * 
	 * @param rs {@link ResultSet}
	 * @return
	 * @throws SQLException
	 */
	public List<HashMap<String, String>> convertResultSetToList(ResultSet rs) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		int columns = md.getColumnCount();
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		while (rs.next()) {
			HashMap<String, String> row = new HashMap<String, String>(columns);
			for (int i = 1; i <= columns; ++i) {
				// ensure datetime is not converted to local time
				// if (md.getColumnTypeName(i).contains("DATETIME"))
				row.put(md.getColumnName(i), rs.getString(i));
				// else
				// row.put(md.getColumnName(i), rs.getObject(i));
			}
			list.add(row);
		}

		return list;
	}

}
