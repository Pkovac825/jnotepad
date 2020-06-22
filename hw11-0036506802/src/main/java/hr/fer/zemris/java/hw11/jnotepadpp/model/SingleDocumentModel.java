package hr.fer.zemris.java.hw11.jnotepadpp.model;

import java.nio.file.Path;

import javax.swing.JTextArea;

/**
 * Sučelje koje modelira jedan otvoreni dokument.
 *
 *
 */
public interface SingleDocumentModel {
	
	/**
	 * Vraća tekstualnu komponentu ovog dokumenta.
	 */
	JTextArea getTextComponent();
	
	/**
	 * Vraća put do ovog dokumenta.
	 */
	Path getFilePath();
	
	/**
	 * Postavlja put do ovog dokumenta na novu vrijednost.
	 * @param path Nova vrijednost puta
	 */
	void setFilePath(Path path);
	
	/**
	 * @return <code>true</code> ako je dokument modificiran, false inače.
	 */
	boolean isModified();
	
	/**
	 * Postavlja zastavicu modificiranosti ovog dokumenta na novu vriejdnost.
	 * @param modified Zastavica modificiranosti
	 */
	void setModified(boolean modified);
	
	/**
	 * Dodaje promatrača ovom dokumentu.
	 * @param l Promatrač
	 */
	void addSingleDocumentListener(SingleDocumentListener l);
	
	/**
	 * Miče danog promatrača iz kolekcije promatrača.
	 * @param l Promatrač
	 */
	void removeSingleDocumentListener(SingleDocumentListener l);
	
}