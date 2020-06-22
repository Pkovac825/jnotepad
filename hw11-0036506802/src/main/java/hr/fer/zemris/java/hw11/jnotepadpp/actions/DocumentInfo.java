package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;

/**
 * Akcija koja je sposobna prikazivati informacije o {@link MultipleDocumentModel}u.
 *
 * @author Petar Kovač
 *
 */
public class DocumentInfo extends LocalizableAction implements MultipleDocumentListener {


	private static final long serialVersionUID = -3201747062050420955L;
	
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;
	/**
	 * Broj znakova.
	 */
	private int charCount;
	/**
	 * Brojs slova.
	 */
	private int letterCount;
	/**
	 * Broj linija.
	 */
	private long lineCount;
	
	public DocumentInfo(MultipleDocumentModel model, ILocalizationProvider provider) {
		super("Info", provider);
		this.model = model;
		addDescription();
	}

	private void addDescription() {
		this.putValue(
				Action.NAME,
				"Info");
		this.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control I"));
		this.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_I);
		this.putValue(
				Action.SHORT_DESCRIPTION, 
				"Show document statistics.");
		setEnabled(false);
	}
	
	public int getCharCount() {
		return charCount;
	}

	public int getLetterCount() {
		return letterCount;
	}

	public long getLineCount() {
		return lineCount;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String text = model.getCurrentDocument().getTextComponent().getText();
		
		JOptionPane.showMessageDialog((Component) model,
									"Your document has " +  text.length() +
									" characters, \n" + text.replaceAll("\\s", "").length() +
									" non-blank characters, and " + text.lines().count() +
									" lines.", "Document Info", 
									JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		setEnabled(currentModel != null);
	}

	@Override
	public void documentAdded(SingleDocumentModel model) {
	}

	@Override
	public void documentRemoved(SingleDocumentModel model) {
	}

}
