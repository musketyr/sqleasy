package eu.ebdit.sqleasy;

import java.sql.SQLException;

/**
 * Obalova vyjimka pro {@link SqlException} lze pouzit vsude tam, kde 
 * je zakazano tuto vyjimku za jinych okolnosti vyhodit, napr. protoze to
 * zakazuje metoda predka, nebo rozhrani.
 * @author Vladimir Orany
 *
 */
class SqlExceptionWrapper extends RuntimeException {


	/**
	 * Vytvori novou obalovou vyjimku
	 * @param cause
	 */
	public SqlExceptionWrapper(SQLException cause) {
		super("Chyba pri praci s databazi, pro vice informaci prohlidnete detaily zabalene tridy SQLException!", cause);
	}
	
	@Override
	public synchronized Throwable initCause(Throwable cause) {
		if (cause instanceof SQLException) {
			return super.initCause(cause);
			
		}
		throw new IllegalArgumentException("Tato vyjimka muze byt vyvolana pouze SQLException");
	}
	
	@Override
	public SQLException getCause() {
		// TODO Auto-generated method stub
		return (SQLException)super.getCause();
	}
	
	/**
	 * Vrati {@link SQLException}, ktera je touto vyjimkou zabalena.
	 * @return	vyjimka, ktera je do teto vyjimky zabalena
	 */
	public SQLException getSQLExeption(){
		return getCause();
	}

}
