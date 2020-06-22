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
import hr.fer.zemris.java.hw11.jnotepadpp.util.Constants;

/**
 * Akcija koja zna stvoriti novi dokument.
 *
 * @author Petar Kovač
 *
 */
public class NewDocument extends LocalizableAction implements MultipleDocumentListener{


	private static final long serialVersionUID = -3201747062050420955L;
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;
	/**
	 * Zastavica koja govori može li se dodati još dokumenata.
	 */
	private boolean moreDocuments = true;
	
	/**
	 * Maksimalan broj dokumenata
	 */
	private static final int MAX_DOC = Constants.MAX_DOC;
	
	public NewDocument(MultipleDocumentModel model, ILocalizationProvider provider) {
		super("New", provider);
		this.model = model;
		model.addMultipleDocumentListener(this);
		addDescription();
	}

	private void addDescription() {
		this.putValue(
				Action.NAME,
				"New");
		this.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control N"));
		this.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_N);
		this.putValue(
				Action.SHORT_DESCRIPTION, 
				"Create new file.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(moreDocuments) {
			model.createNewDocument();	
		} else {
			showMaxDocMessage();
		}
	}

	private void showMaxDocMessage() {
		JOptionPane.showMessageDialog((Component) model,
				"You have reached the maximum number of documents,"
				+ "\nplease delete some "
				+ "documents to add new documents."
				, "Reached document limit", 
				JOptionPane.INFORMATION_MESSAGE);
	}

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
	}

	@Override
	public void documentAdded(SingleDocumentModel model) {
		moreDocuments = (this.model.getNumberOfDocuments() < MAX_DOC);
	}

	@Override
	public void documentRemoved(SingleDocumentModel model) {
		moreDocuments = (this.model.getNumberOfDocuments() < MAX_DOC);
	}
}
