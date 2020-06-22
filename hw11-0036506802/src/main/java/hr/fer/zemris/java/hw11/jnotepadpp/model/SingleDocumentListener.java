package hr.fer.zemris.java.hw11.jnotepadpp.model;

/**
 * Modelira promatrača za {@link SingleDocumentModel}.
 *
 *
 */
public interface SingleDocumentListener {
	
	/**
	 * Metoda koju promatrač izvodi kada se status modificiranosti promijenio.
	 * 
	 * @param model Modificiran dokument
	 */
	void documentModifyStatusUpdated(SingleDocumentModel model);
	
	/**
	 * Metoda koju promatrač izvodi kada se putanja dokumenta promijenila.
	 * 
	 * @param model Dokument kojem je promijenjena putanja
	 */
	void documentFilePathUpdated(SingleDocumentModel model);
}
