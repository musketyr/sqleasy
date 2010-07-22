package eu.ebdit.sqleasy;

import java.sql.ResultSet;
import java.sql.SQLException;



/**
 * Tato trida vytovri obal nad result setem, ktery znemoznuje upravovat
 * result set.
 * Nasledujici metody jsou zakazany:
 * <ul>
 * <li>{@link #absolute(int)}
 * <li>{@link #afterLast()}
 * <li>{@link #beforeFirst()}
 * <li>{@link #cancelRowUpdates()}
 * <li>{@link #clearWarnings()}
 * <li>{@link #deleteRow()}
 * <li>{@link #first()}
 * <li>{@link #insertRow()}
 * <li>{@link #last()}
 * <li>{@link #moveToCurrentRow()}
 * <li>{@link #moveToInsertRow()}
 * <li>{@link #next()}
 * <li>{@link #previous()}
 * <li>{@link #relative(int)}
 * <li>{@link #setFetchDirection(int)}¨
 * <li>{@link #setFetchSize(int)}
 * </ul>
 * @author Vladimir Orany
 *
 */
class CursorlessResultSet extends ResultSetWrapper {
	
	/**
	 * Vytovri novy {@link ResultSet}, ktery ma zakazne
	 * metody, ktere by mohli zmenit kurzor.
	 * @param wrapped
	 */
	CursorlessResultSet(final ResultSet wrapped) {
		super(wrapped);
	}

	@Override
	public void afterLast() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void beforeFirst() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancelRowUpdates() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean first() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean last() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean next() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void moveToCurrentRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void moveToInsertRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean absolute(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void insertRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void deleteRow() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean previous() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean relative(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchSize(final int arg0) throws SQLException {
		throw new UnsupportedOperationException();
	}
}