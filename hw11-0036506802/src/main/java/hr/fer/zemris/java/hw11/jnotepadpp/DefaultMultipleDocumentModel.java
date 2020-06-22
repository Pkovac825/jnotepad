package hr.fer.zemris.java.hw11.jnotepadpp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.util.ImageLoader;


/**
 * Konkretna implementacija dokumenta koji može sadržavati druge dokumente.
 *
 * @author Petar Kovač
 *
 */
public class DefaultMultipleDocumentModel extends JTabbedPane implements MultipleDocumentModel, SingleDocumentListener{
	/**
	 * Kolekcija svih trenutno sadržanih dokumenata.
	 */
	private List<SingleDocumentModel> tabs;
	/**
	 * Dokument na kojeg ovaj {@link MultipleDocumentModel} trenutno 'gleda'.
	 */
	private SingleDocumentModel doc;
	/**
	 * Promatrači ovog {@link MultipleDocumentModel}a.
	 */
	private List<MultipleDocumentListener> listeners;
	/**
	 * Indeks trenutno odabrane kartice.
	 */
	private int currentTabIndex;
	

	// ===============================SERIAL VERSION ID====================//
	private static final long serialVersionUID = -8635934856830430077L;

	
	
	/**
	 * Zadani konstruktor, stvara potrebne kolekcije za rad ovog modela.
	 */
	public DefaultMultipleDocumentModel() {
		this.tabs = new ArrayList<>();
		this.listeners = new ArrayList<>();
		this.addChangeListener(changeListener);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<SingleDocumentModel> iterator() {
		return tabs.iterator();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SingleDocumentModel createNewDocument() {
		doc = new DefaultSingleDocumentModel(null, "");
		addTab(doc);
		return doc;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SingleDocumentModel getCurrentDocument() {
		return getDocument(model.getSelectedIndex());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SingleDocumentModel loadDocument(Path path) {
		Objects.requireNonNull(path);
		for(int i = 0, limit = tabs.size(); i < limit; i++) {
			if(path.equals(tabs.get(i).getFilePath())) {
				setSelectedIndex(i);
				return doc;
			}
		}
		
		SingleDocumentModel document;

		try {
			document = new DefaultSingleDocumentModel(path, Files.readString(path, StandardCharsets.UTF_8));
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					this,
					"Unable to read file: " + path,
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		return addTab(document);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveDocument(SingleDocumentModel model, Path newPath) {
		if(newPath == null) {
			newPath = model.getFilePath();
		}
		
		if(!model.isModified()) return;
		
		try {
			Files.writeString(newPath, model.getTextComponent().getText(), StandardCharsets.UTF_8);
			model.setModified(false);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(
					this,
					"Error while writing into file.",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		model.setFilePath(newPath);
		setTabName(newPath);
		removeCopies();
	}

	/**
	 * Miče kopije u slučaju da korisnik prepiše preko dokumenta koji već postoji u ovom modelu.
	 */
	private void removeCopies() {
		for(int i = 0; i < tabs.size(); i++) {
			if(i != currentTabIndex) {
				if(getCurrentDocPath().equals(tabs.get(i).getFilePath())) {
					tabs.remove(i);
					this.remove(i);
				}
			}
		}
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void closeDocument(SingleDocumentModel model) {
		listeners.forEach(l -> l.documentRemoved(doc));
		tabs.remove(currentTabIndex);
		this.remove(currentTabIndex);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.add(l);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeMultipleDocumentListener(MultipleDocumentListener l) {
		listeners.remove(l);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getNumberOfDocuments() {
		return tabs.size();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public SingleDocumentModel getDocument(int index) {
		if(index < 0) {
			return null;
		}
		return tabs.get(index);
	}
	
	/**
	 * Dohvaća putanju trenutno odabranog dokumenta.
	 */
	public Path getCurrentDocPath() {
		return doc.getFilePath();
	}
	/**
	 * Dodaje jedan dokument ovom modelu zajedno sa svim promjenama koje su potrebne da model
	 * funkcionira ispravno.
	 * 
	 * @param newDoc Novi dokument kojeg se dodaje
	 * @return Dodani dokument
	 */
	private SingleDocumentModel addTab(SingleDocumentModel newDoc) {
		Path path = newDoc.getFilePath();
		
		JScrollPane pane = new JScrollPane(newDoc.getTextComponent());
		newDoc.addSingleDocumentListener(this);
		
		tabs.add(newDoc);
		this.add(pane);	
		setSelectedComponent(pane);
		setTabName(path);
		
		listeners.forEach((l) -> l.documentAdded(doc));
		insertImage(doc);
		
		return doc;
	}
	
	/**
	 * Postavlja ime odabrane kartice.
	 * @param path
	 */
	private void setTabName(Path path) {
		setTitleAt(currentTabIndex, path == null ? "unnamed" : path.getFileName().toString());
		setToolTipTextAt(currentTabIndex, path == null ? "unnamed" : path.toString());
	}
	
	/**
	 * Promatrač koji, kada se kartica modela promijeni,
	 * radi sve potrebne promjene da bi model ispravno funkcionirao.
	 */
	private final ChangeListener changeListener = new ChangeListener() {
		 public void stateChanged(ChangeEvent e) {
			 DefaultMultipleDocumentModel model = (DefaultMultipleDocumentModel) e.getSource();
			 SingleDocumentModel old = model.doc;
			 currentTabIndex = model.getSelectedIndex();
			 model.doc = model.getDocument(currentTabIndex);
			 model.listeners.forEach((l) -> l.currentDocumentChanged(old, model.doc)); 
		 }
	};


	/**
	 * Metoda koja dodaje sliku kartici.
	 * @param model Model kojem se dodaje slika
	 */
	private void insertImage(SingleDocumentModel model) {
		try {
			if(model.isModified()) {
				setIconAt(currentTabIndex, ImageLoader.loadImage("icons/modified.png", this.getClass()));
			} else {
				setIconAt(currentTabIndex, ImageLoader.loadImage("icons/saved.png", this.getClass()));
			}
		} catch (IOException e) {
			System.out.println("ERROR");
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void documentModifyStatusUpdated(SingleDocumentModel model) {
		insertImage(model);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void documentFilePathUpdated(SingleDocumentModel model) {
		Path path = model.getFilePath();
		setTitleAt(currentTabIndex, path == null ? "unnamed" : path.getFileName().toString());
		setToolTipTextAt(currentTabIndex, path == null ? "unnamed" : path.toString());
	}


}
