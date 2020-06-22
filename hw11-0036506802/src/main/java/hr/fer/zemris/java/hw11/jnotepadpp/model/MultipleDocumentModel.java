package hr.fer.zemris.java.hw11.jnotepadpp.model;

import java.nio.file.Path;

/**
 * Sučelje koje modelira dokument koji u sebi sadrži nula ili više primjeraka {@link SingleDocumentModel}a.
 *
 *
 */
public interface MultipleDocumentModel extends Iterable<SingleDocumentModel> {
	
	/**
	 * Stvara novi dokument.
	 * @return Novi dokument
	 */
	SingleDocumentModel createNewDocument();
	
	/**
	 * Vraća trenutno odabrani dokument.
	 * 
	 * @return Trenutno odabrani dokument
	 */
	SingleDocumentModel getCurrentDocument();
	
	/**
	 * Učitava dokument s danom putanjom s datotečnog sustava.
	 * 
	 * @param path Putanja do dokumenta
	 * @return Učitani dokument
	 */
	SingleDocumentModel loadDocument(Path path);
	
	/**
	 * Sprema dan dokument na danu putanju ili na putanju dokumenta, ako je dana putanja jednaka <code>null</code>
	 * 
	 * @param model Dokument kojeg treba spremiti
	 * @param newPath Putanja
	 */
	void saveDocument(SingleDocumentModel model, Path newPath);
	
	/**
	 * Zatvara dokument koji je dan kao argument.
	 * 
	 * @param model Dokument kojeg želimo zatvoriti
	 */
	void closeDocument(SingleDocumentModel model);
	
	/**
	 * Dodaje promatrača ovom {@link MultipleDocumentModel}u.
	 * 
	 * @param l Promatrač
	 */
	void addMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Miče promatrača iz kolekcije promatrača ovog {@link MultipleDocumentModel}a.
	 * 
	 * @param l Promatrač kojeg treba maknuti iz kolekcije promatrača
	 */
	void removeMultipleDocumentListener(MultipleDocumentListener l);
	
	/**
	 * Vraća broj dokumenata ovog {@link MultipleDocumentModel}a.
	 * @return Broj dokumenata
	 */
	int getNumberOfDocuments();
	
	/**
	 * Dohvaća dokument s indeksom index.
	 * @param index Indeks traženog dokumenta
	 * @return Dokument na poziciji index
	 */
	SingleDocumentModel getDocument(int index);
}