package eu.ebdit.sqleasy;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * Delegujici result set. Vsechny metody jsou presmerovany
 * na prislusne metody result setu ziskaneho v konstruktoru 
 * @author vorany
 *
 */
abstract class ResultSetWrapper implements ResultSet{
	
	/**
	 * {@link ResultSet}, na ktery jsou delegovany vsechny metody
	 */
	private final ResultSet wrapped;
	
	/**
	 * Vytvori novy obalovy {@link ResultSet}
	 * @param wrapped	{@link ResultSet}, na ktery budou vsechny metody delegovany
	 */
	public ResultSetWrapper(ResultSet wrapped) {
		this.wrapped = wrapped;
	}

	public boolean absolute(int row) throws SQLException {
		return this.wrapped.absolute(row);
	}

	public void afterLast() throws SQLException {
		this.wrapped.afterLast();
	}

	public void beforeFirst() throws SQLException {
		this.wrapped.beforeFirst();
	}

	public void cancelRowUpdates() throws SQLException {
		this.wrapped.cancelRowUpdates();
	}

	public void clearWarnings() throws SQLException {
		this.wrapped.clearWarnings();
	}

	public void close() throws SQLException {
		this.wrapped.close();
	}

	public void deleteRow() throws SQLException {
		this.wrapped.deleteRow();
	}

	public int findColumn(String columnName) throws SQLException {
		return this.wrapped.findColumn(columnName);
	}

	public boolean first() throws SQLException {
		return this.wrapped.first();
	}

	public Array getArray(int i) throws SQLException {
		return this.wrapped.getArray(i);
	}

	public Array getArray(String colName) throws SQLException {
		return this.wrapped.getArray(colName);
	}

	public InputStream getAsciiStream(int columnIndex) throws SQLException {
		return this.wrapped.getAsciiStream(columnIndex);
	}

	public InputStream getAsciiStream(String columnName) throws SQLException {
		return this.wrapped.getAsciiStream(columnName);
	}

	@Deprecated
	public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
		return this.wrapped.getBigDecimal(columnIndex, scale);
	}

	public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
		return this.wrapped.getBigDecimal(columnIndex);
	}

	@Deprecated
	public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
		return this.wrapped.getBigDecimal(columnName, scale);
	}

	public BigDecimal getBigDecimal(String columnName) throws SQLException {
		return this.wrapped.getBigDecimal(columnName);
	}

	public InputStream getBinaryStream(int columnIndex) throws SQLException {
		return this.wrapped.getBinaryStream(columnIndex);
	}

	public InputStream getBinaryStream(String columnName) throws SQLException {
		return this.wrapped.getBinaryStream(columnName);
	}

	public Blob getBlob(int i) throws SQLException {
		return this.wrapped.getBlob(i);
	}

	public Blob getBlob(String colName) throws SQLException {
		return this.wrapped.getBlob(colName);
	}

	public boolean getBoolean(int columnIndex) throws SQLException {
		return this.wrapped.getBoolean(columnIndex);
	}

	public boolean getBoolean(String columnName) throws SQLException {
		return this.wrapped.getBoolean(columnName);
	}

	public byte getByte(int columnIndex) throws SQLException {
		return this.wrapped.getByte(columnIndex);
	}

	public byte getByte(String columnName) throws SQLException {
		return this.wrapped.getByte(columnName);
	}

	public byte[] getBytes(int columnIndex) throws SQLException {
		return this.wrapped.getBytes(columnIndex);
	}

	public byte[] getBytes(String columnName) throws SQLException {
		return this.wrapped.getBytes(columnName);
	}

	public Clob getClob(int i) throws SQLException {
		return this.wrapped.getClob(i);
	}

	public Clob getClob(String colName) throws SQLException {
		return this.wrapped.getClob(colName);
	}

	public int getConcurrency() throws SQLException {
		return this.wrapped.getConcurrency();
	}

	public String getCursorName() throws SQLException {
		return this.wrapped.getCursorName();
	}

	public Date getDate(int columnIndex, Calendar cal) throws SQLException {
		return this.wrapped.getDate(columnIndex, cal);
	}

	public Date getDate(int columnIndex) throws SQLException {
		return this.wrapped.getDate(columnIndex);
	}

	public Date getDate(String columnName, Calendar cal) throws SQLException {
		return this.wrapped.getDate(columnName, cal);
	}

	public Date getDate(String columnName) throws SQLException {
		return this.wrapped.getDate(columnName);
	}

	public double getDouble(int columnIndex) throws SQLException {
		return this.wrapped.getDouble(columnIndex);
	}

	public double getDouble(String columnName) throws SQLException {
		return this.wrapped.getDouble(columnName);
	}

	public int getFetchDirection() throws SQLException {
		return this.wrapped.getFetchDirection();
	}

	public int getFetchSize() throws SQLException {
		return this.wrapped.getFetchSize();
	}

	public float getFloat(int columnIndex) throws SQLException {
		return this.wrapped.getFloat(columnIndex);
	}

	public float getFloat(String columnName) throws SQLException {
		return this.wrapped.getFloat(columnName);
	}

	public Reader getCharacterStream(int columnIndex) throws SQLException {
		return this.wrapped.getCharacterStream(columnIndex);
	}

	public Reader getCharacterStream(String columnName) throws SQLException {
		return this.wrapped.getCharacterStream(columnName);
	}

	public int getInt(int columnIndex) throws SQLException {
		return this.wrapped.getInt(columnIndex);
	}

	public int getInt(String columnName) throws SQLException {
		return this.wrapped.getInt(columnName);
	}

	public long getLong(int columnIndex) throws SQLException {
		return this.wrapped.getLong(columnIndex);
	}

	public long getLong(String columnName) throws SQLException {
		return this.wrapped.getLong(columnName);
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return this.wrapped.getMetaData();
	}

	public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
		return this.wrapped.getObject(i, map);
	}

	public Object getObject(int columnIndex) throws SQLException {
		return this.wrapped.getObject(columnIndex);
	}

	public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
		return this.wrapped.getObject(colName, map);
	}

	public Object getObject(String columnName) throws SQLException {
		return this.wrapped.getObject(columnName);
	}

	public Ref getRef(int i) throws SQLException {
		return this.wrapped.getRef(i);
	}

	public Ref getRef(String colName) throws SQLException {
		return this.wrapped.getRef(colName);
	}

	public int getRow() throws SQLException {
		return this.wrapped.getRow();
	}

	public short getShort(int columnIndex) throws SQLException {
		return this.wrapped.getShort(columnIndex);
	}

	public short getShort(String columnName) throws SQLException {
		return this.wrapped.getShort(columnName);
	}

	public Statement getStatement() throws SQLException {
		return this.wrapped.getStatement();
	}

	public String getString(int columnIndex) throws SQLException {
		return this.wrapped.getString(columnIndex);
	}

	public String getString(String columnName) throws SQLException {
		return this.wrapped.getString(columnName);
	}

	public Time getTime(int columnIndex, Calendar cal) throws SQLException {
		return this.wrapped.getTime(columnIndex, cal);
	}

	public Time getTime(int columnIndex) throws SQLException {
		return this.wrapped.getTime(columnIndex);
	}

	public Time getTime(String columnName, Calendar cal) throws SQLException {
		return this.wrapped.getTime(columnName, cal);
	}

	public Time getTime(String columnName) throws SQLException {
		return this.wrapped.getTime(columnName);
	}

	public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
		return this.wrapped.getTimestamp(columnIndex, cal);
	}

	public Timestamp getTimestamp(int columnIndex) throws SQLException {
		return this.wrapped.getTimestamp(columnIndex);
	}

	public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
		return this.wrapped.getTimestamp(columnName, cal);
	}

	public Timestamp getTimestamp(String columnName) throws SQLException {
		return this.wrapped.getTimestamp(columnName);
	}

	public int getType() throws SQLException {
		return this.wrapped.getType();
	}

	@Deprecated
	public InputStream getUnicodeStream(int columnIndex) throws SQLException {
		return this.wrapped.getUnicodeStream(columnIndex);
	}

	@Deprecated
	public InputStream getUnicodeStream(String columnName) throws SQLException {
		return this.wrapped.getUnicodeStream(columnName);
	}

	public URL getURL(int columnIndex) throws SQLException {
		return this.wrapped.getURL(columnIndex);
	}

	public URL getURL(String columnName) throws SQLException {
		return this.wrapped.getURL(columnName);
	}

	public SQLWarning getWarnings() throws SQLException {
		return this.wrapped.getWarnings();
	}

	public void insertRow() throws SQLException {
		this.wrapped.insertRow();
	}

	public boolean isAfterLast() throws SQLException {
		return this.wrapped.isAfterLast();
	}

	public boolean isBeforeFirst() throws SQLException {
		return this.wrapped.isBeforeFirst();
	}

	public boolean isFirst() throws SQLException {
		return this.wrapped.isFirst();
	}

	public boolean isLast() throws SQLException {
		return this.wrapped.isLast();
	}

	public boolean last() throws SQLException {
		return this.wrapped.last();
	}

	public void moveToCurrentRow() throws SQLException {
		this.wrapped.moveToCurrentRow();
	}

	public void moveToInsertRow() throws SQLException {
		this.wrapped.moveToInsertRow();
	}

	public boolean next() throws SQLException {
		return this.wrapped.next();
	}

	public boolean previous() throws SQLException {
		return this.wrapped.previous();
	}

	public void refreshRow() throws SQLException {
		this.wrapped.refreshRow();
	}

	public boolean relative(int rows) throws SQLException {
		return this.wrapped.relative(rows);
	}

	public boolean rowDeleted() throws SQLException {
		return this.wrapped.rowDeleted();
	}

	public boolean rowInserted() throws SQLException {
		return this.wrapped.rowInserted();
	}

	public boolean rowUpdated() throws SQLException {
		return this.wrapped.rowUpdated();
	}

	public void setFetchDirection(int direction) throws SQLException {
		this.wrapped.setFetchDirection(direction);
	}

	public void setFetchSize(int rows) throws SQLException {
		this.wrapped.setFetchSize(rows);
	}

	public void updateArray(int columnIndex, Array x) throws SQLException {
		this.wrapped.updateArray(columnIndex, x);
	}

	public void updateArray(String columnName, Array x) throws SQLException {
		this.wrapped.updateArray(columnName, x);
	}

	public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
		this.wrapped.updateAsciiStream(columnIndex, x, length);
	}

	public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
		this.wrapped.updateAsciiStream(columnName, x, length);
	}

	public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
		this.wrapped.updateBigDecimal(columnIndex, x);
	}

	public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
		this.wrapped.updateBigDecimal(columnName, x);
	}

	public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
		this.wrapped.updateBinaryStream(columnIndex, x, length);
	}

	public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
		this.wrapped.updateBinaryStream(columnName, x, length);
	}

	public void updateBlob(int columnIndex, Blob x) throws SQLException {
		this.wrapped.updateBlob(columnIndex, x);
	}

	public void updateBlob(String columnName, Blob x) throws SQLException {
		this.wrapped.updateBlob(columnName, x);
	}

	public void updateBoolean(int columnIndex, boolean x) throws SQLException {
		this.wrapped.updateBoolean(columnIndex, x);
	}

	public void updateBoolean(String columnName, boolean x) throws SQLException {
		this.wrapped.updateBoolean(columnName, x);
	}

	public void updateByte(int columnIndex, byte x) throws SQLException {
		this.wrapped.updateByte(columnIndex, x);
	}

	public void updateByte(String columnName, byte x) throws SQLException {
		this.wrapped.updateByte(columnName, x);
	}

	public void updateBytes(int columnIndex, byte[] x) throws SQLException {
		this.wrapped.updateBytes(columnIndex, x);
	}

	public void updateBytes(String columnName, byte[] x) throws SQLException {
		this.wrapped.updateBytes(columnName, x);
	}

	public void updateClob(int columnIndex, Clob x) throws SQLException {
		this.wrapped.updateClob(columnIndex, x);
	}

	public void updateClob(String columnName, Clob x) throws SQLException {
		this.wrapped.updateClob(columnName, x);
	}

	public void updateDate(int columnIndex, Date x) throws SQLException {
		this.wrapped.updateDate(columnIndex, x);
	}

	public void updateDate(String columnName, Date x) throws SQLException {
		this.wrapped.updateDate(columnName, x);
	}

	public void updateDouble(int columnIndex, double x) throws SQLException {
		this.wrapped.updateDouble(columnIndex, x);
	}

	public void updateDouble(String columnName, double x) throws SQLException {
		this.wrapped.updateDouble(columnName, x);
	}

	public void updateFloat(int columnIndex, float x) throws SQLException {
		this.wrapped.updateFloat(columnIndex, x);
	}

	public void updateFloat(String columnName, float x) throws SQLException {
		this.wrapped.updateFloat(columnName, x);
	}

	public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
		this.wrapped.updateCharacterStream(columnIndex, x, length);
	}

	public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
		this.wrapped.updateCharacterStream(columnName, reader, length);
	}

	public void updateInt(int columnIndex, int x) throws SQLException {
		this.wrapped.updateInt(columnIndex, x);
	}

	public void updateInt(String columnName, int x) throws SQLException {
		this.wrapped.updateInt(columnName, x);
	}

	public void updateLong(int columnIndex, long x) throws SQLException {
		this.wrapped.updateLong(columnIndex, x);
	}

	public void updateLong(String columnName, long x) throws SQLException {
		this.wrapped.updateLong(columnName, x);
	}

	public void updateNull(int columnIndex) throws SQLException {
		this.wrapped.updateNull(columnIndex);
	}

	public void updateNull(String columnName) throws SQLException {
		this.wrapped.updateNull(columnName);
	}

	public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
		this.wrapped.updateObject(columnIndex, x, scale);
	}

	public void updateObject(int columnIndex, Object x) throws SQLException {
		this.wrapped.updateObject(columnIndex, x);
	}

	public void updateObject(String columnName, Object x, int scale) throws SQLException {
		this.wrapped.updateObject(columnName, x, scale);
	}

	public void updateObject(String columnName, Object x) throws SQLException {
		this.wrapped.updateObject(columnName, x);
	}

	public void updateRef(int columnIndex, Ref x) throws SQLException {
		this.wrapped.updateRef(columnIndex, x);
	}

	public void updateRef(String columnName, Ref x) throws SQLException {
		this.wrapped.updateRef(columnName, x);
	}

	public void updateRow() throws SQLException {
		this.wrapped.updateRow();
	}

	public void updateShort(int columnIndex, short x) throws SQLException {
		this.wrapped.updateShort(columnIndex, x);
	}

	public void updateShort(String columnName, short x) throws SQLException {
		this.wrapped.updateShort(columnName, x);
	}

	public void updateString(int columnIndex, String x) throws SQLException {
		this.wrapped.updateString(columnIndex, x);
	}

	public void updateString(String columnName, String x) throws SQLException {
		this.wrapped.updateString(columnName, x);
	}

	public void updateTime(int columnIndex, Time x) throws SQLException {
		this.wrapped.updateTime(columnIndex, x);
	}

	public void updateTime(String columnName, Time x) throws SQLException {
		this.wrapped.updateTime(columnName, x);
	}

	public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
		this.wrapped.updateTimestamp(columnIndex, x);
	}

	public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
		this.wrapped.updateTimestamp(columnName, x);
	}

	public boolean wasNull() throws SQLException {
		return this.wrapped.wasNull();
	}

	public int getHoldability() throws SQLException {
		return this.wrapped.getHoldability();
	}

	public NClob getNClob(int columnIndex) throws SQLException {
		return this.wrapped.getNClob(columnIndex);
	}

	public NClob getNClob(String columnLabel) throws SQLException {
		return this.wrapped.getNClob(columnLabel);
	}

	public Reader getNCharacterStream(int columnIndex) throws SQLException {
		return this.wrapped.getNCharacterStream(columnIndex);
	}

	public Reader getNCharacterStream(String columnLabel) throws SQLException {
		return this.wrapped.getNCharacterStream(columnLabel);
	}

	public String getNString(int columnIndex) throws SQLException {
		return this.wrapped.getNString(columnIndex);
	}

	public String getNString(String columnLabel) throws SQLException {
		return this.wrapped.getNString(columnLabel);
	}

	public RowId getRowId(int columnIndex) throws SQLException {
		return this.wrapped.getRowId(columnIndex);
	}

	public RowId getRowId(String columnLabel) throws SQLException {
		return this.wrapped.getRowId(columnLabel);
	}

	public SQLXML getSQLXML(int columnIndex) throws SQLException {
		return this.wrapped.getSQLXML(columnIndex);
	}

	public SQLXML getSQLXML(String columnLabel) throws SQLException {
		return this.wrapped.getSQLXML(columnLabel);
	}

	public boolean isClosed() throws SQLException {
		return this.wrapped.isClosed();
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.wrapped.isWrapperFor(iface);
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		return this.wrapped.unwrap(iface);
	}

	public void updateAsciiStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		this.wrapped.updateAsciiStream(columnIndex, x, length);
	}

	public void updateAsciiStream(int columnIndex, InputStream x)
			throws SQLException {
		this.wrapped.updateAsciiStream(columnIndex, x);
	}

	public void updateAsciiStream(String columnLabel, InputStream x, long length)
			throws SQLException {
		this.wrapped.updateAsciiStream(columnLabel, x, length);
	}

	public void updateAsciiStream(String columnLabel, InputStream x)
			throws SQLException {
		this.wrapped.updateAsciiStream(columnLabel, x);
	}

	public void updateBinaryStream(int columnIndex, InputStream x, long length)
			throws SQLException {
		this.wrapped.updateBinaryStream(columnIndex, x, length);
	}

	public void updateBinaryStream(int columnIndex, InputStream x)
			throws SQLException {
		this.wrapped.updateBinaryStream(columnIndex, x);
	}

	public void updateBinaryStream(String columnLabel, InputStream x,
			long length) throws SQLException {
		this.wrapped.updateBinaryStream(columnLabel, x, length);
	}

	public void updateBinaryStream(String columnLabel, InputStream x)
			throws SQLException {
		this.wrapped.updateBinaryStream(columnLabel, x);
	}

	public void updateBlob(int columnIndex, InputStream inputStream, long length)
			throws SQLException {
		this.wrapped.updateBlob(columnIndex, inputStream, length);
	}

	public void updateBlob(int columnIndex, InputStream inputStream)
			throws SQLException {
		this.wrapped.updateBlob(columnIndex, inputStream);
	}

	public void updateBlob(String columnLabel, InputStream inputStream,
			long length) throws SQLException {
		this.wrapped.updateBlob(columnLabel, inputStream, length);
	}

	public void updateBlob(String columnLabel, InputStream inputStream)
			throws SQLException {
		this.wrapped.updateBlob(columnLabel, inputStream);
	}

	public void updateClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		this.wrapped.updateClob(columnIndex, reader, length);
	}

	public void updateClob(int columnIndex, Reader reader) throws SQLException {
		this.wrapped.updateClob(columnIndex, reader);
	}

	public void updateClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		this.wrapped.updateClob(columnLabel, reader, length);
	}

	public void updateClob(String columnLabel, Reader reader)
			throws SQLException {
		this.wrapped.updateClob(columnLabel, reader);
	}

	public void updateCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		this.wrapped.updateCharacterStream(columnIndex, x, length);
	}

	public void updateCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		this.wrapped.updateCharacterStream(columnIndex, x);
	}

	public void updateCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		this.wrapped.updateCharacterStream(columnLabel, reader, length);
	}

	public void updateCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		this.wrapped.updateCharacterStream(columnLabel, reader);
	}

	public void updateNClob(int columnIndex, NClob clob) throws SQLException {
		this.wrapped.updateNClob(columnIndex, clob);
	}

	public void updateNClob(int columnIndex, Reader reader, long length)
			throws SQLException {
		this.wrapped.updateNClob(columnIndex, reader, length);
	}

	public void updateNClob(int columnIndex, Reader reader) throws SQLException {
		this.wrapped.updateNClob(columnIndex, reader);
	}

	public void updateNClob(String columnLabel, NClob clob) throws SQLException {
		this.wrapped.updateNClob(columnLabel, clob);
	}

	public void updateNClob(String columnLabel, Reader reader, long length)
			throws SQLException {
		this.wrapped.updateNClob(columnLabel, reader, length);
	}

	public void updateNClob(String columnLabel, Reader reader)
			throws SQLException {
		this.wrapped.updateNClob(columnLabel, reader);
	}

	public void updateNCharacterStream(int columnIndex, Reader x, long length)
			throws SQLException {
		this.wrapped.updateNCharacterStream(columnIndex, x, length);
	}

	public void updateNCharacterStream(int columnIndex, Reader x)
			throws SQLException {
		this.wrapped.updateNCharacterStream(columnIndex, x);
	}

	public void updateNCharacterStream(String columnLabel, Reader reader,
			long length) throws SQLException {
		this.wrapped.updateNCharacterStream(columnLabel, reader, length);
	}

	public void updateNCharacterStream(String columnLabel, Reader reader)
			throws SQLException {
		this.wrapped.updateNCharacterStream(columnLabel, reader);
	}

	public void updateNString(int columnIndex, String string)
			throws SQLException {
		this.wrapped.updateNString(columnIndex, string);
	}

	public void updateNString(String columnLabel, String string)
			throws SQLException {
		this.wrapped.updateNString(columnLabel, string);
	}

	public void updateRowId(int columnIndex, RowId x) throws SQLException {
		this.wrapped.updateRowId(columnIndex, x);
	}

	public void updateRowId(String columnLabel, RowId x) throws SQLException {
		this.wrapped.updateRowId(columnLabel, x);
	}

	public void updateSQLXML(int columnIndex, SQLXML xmlObject)
			throws SQLException {
		this.wrapped.updateSQLXML(columnIndex, xmlObject);
	}

	public void updateSQLXML(String columnLabel, SQLXML xmlObject)
			throws SQLException {
		this.wrapped.updateSQLXML(columnLabel, xmlObject);
	}
	
	
}