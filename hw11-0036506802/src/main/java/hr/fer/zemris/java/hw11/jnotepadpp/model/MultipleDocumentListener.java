package hr.fer.zemris.java.hw11.jnotepadpp.model;

/**
 * Sučelje modelira promatrača jednog {@link MultipleDocumentModel}a.
 */
public interface MultipleDocumentListener {
	/**
	 * Metoda koju promatrač izvodi kada se trenutni dokument promijenio na neki drugi dokument.
	 * Jedan od paramatera, ali ne i oba, može biti null.
	 * 
	 * @param previousModel Prijašnji dokument
	 * @param currentModel Trenutni dokument
	 */
	void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel);
	/**
	 * Metoda koju promatrač izvodi kada je dodan dokument {@link MultipleDocumentModel}u.
	 * 
	 * @param model Dodani dokument
	 */
	void documentAdded(SingleDocumentModel model);
	/**
	 * Metoda koju promatrač izvodi kada {@link MultipleDocumentModel} makne jedan dokument.
	 * 
	 * @param model Maknuti dokument
	 */
	void documentRemoved(SingleDocumentModel model);
}