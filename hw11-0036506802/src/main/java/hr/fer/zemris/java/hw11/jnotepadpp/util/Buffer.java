package hr.fer.zemris.java.hw11.jnotepadpp.util;

/**
 * Klasa koja služi za čuvanje teksta.
 *
 * @author Petar Kovač
 *
 */
public class Buffer {
	/**
	 * Sačuvani tekst.
	 */
	private String buffer = "";

	/**
	 * Dobavlja sačuvani tekst.
	 */
	public String getBuffer() {
		return buffer;
	}

	/**
	 * Postavlja novu vrijednost sačuvanog teksta.
	 * @param buffer Novi tekst
	 */
	public void setBuffer(String buffer) {
		this.buffer = buffer;
	}
	
	
}
