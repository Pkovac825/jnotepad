package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Color;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;

/**
 * Model jednog jednsotavnog tekstualnog dokumenta.
 *
 * @author Petar Kovač
 */
public class DefaultSingleDocumentModel implements SingleDocumentModel {
	/**
	 * Put do dokumenta. Može biti null.
	 */
	private Path path;
	/**
	 * Tekstualni okvir ovog dokumenta.
	 */
	private JTextArea text;
	/**
	 * Zastavica koja govori je li {@link #text} bio modificiran.
	 */
	private boolean modified = false;
	/**
	 * Promatrači ovog dokumenta.
	 */
	private List<SingleDocumentListener> listeners;
	
	/**
	 * Zadani konstruktor.
	 * @param path Put do dokumenta na datotečnom sustavu
	 * @param text Tekst koji se upisuje u {@link #text}
	 */
	public DefaultSingleDocumentModel(Path path, String text) {
		this.path = path;
		this.text = new JTextArea(text);
		this.text.setBorder(BorderFactory.createLineBorder(
						new Color(Integer.parseInt("33b1ff", 16)), 2));
		this.text.getDocument().addDocumentListener(changeListener);
		listeners = new ArrayList<>();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JTextArea getTextComponent() {
		return text;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Path getFilePath() {
		return path;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setFilePath(Path path) {
		Objects.requireNonNull(path);
		this.path = path;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isModified() {
		return modified;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setModified(boolean modified) {
		if(modified == this.modified) return;
		this.modified = modified;
		notifyListeners();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addSingleDocumentListener(SingleDocumentListener l) {
		listeners.add(l);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSingleDocumentListener(SingleDocumentListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Obavještava promatrače da je došla promjena u tekstu ovog dokumenta.
	 */
	private void notifyListeners() {
		listeners.forEach((s) -> s.documentModifyStatusUpdated(this));; 
	}
	
	private final DocumentListener changeListener = new DocumentListener() {	
		@Override
		public void removeUpdate(DocumentEvent e) {
			DefaultSingleDocumentModel.this.setModified(true);
		}
		
		@Override
		public void insertUpdate(DocumentEvent e) {
			DefaultSingleDocumentModel.this.setModified(true);
		}
		@Override
		public void changedUpdate(DocumentEvent e) {
			DefaultSingleDocumentModel.this.setModified(true);
		}
	};

}
