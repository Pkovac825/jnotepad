package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.util.Constants;

/**
 * Akcija koja zna otvoriti dokument na nekoj putanji.
 *
 * @author Petar Kovač
 *
 */
public class OpenDocument extends LocalizableAction implements MultipleDocumentListener{
	
	private static final long serialVersionUID = 8837040887680407762L;
	
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;
	/**
	 * Zastavica koja govori može li se dodati još dokumenata.
	 */
	private boolean moreDocuments = true;
	/**
	 * Maksimalan broj dokumenata.
	 */
	private static final int MAX_DOC = Constants.MAX_DOC;
	
	public OpenDocument(MultipleDocumentModel model, ILocalizationProvider provider) {
		super("Open", provider);
		this.model = model;
		model.addMultipleDocumentListener(this);
		addDescription();
	}

	private void addDescription() {
		this.putValue(
				Action.NAME,
				"Open");
		this.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control O"));
		this.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_O);
		this.putValue(
				Action.SHORT_DESCRIPTION, 
				"Open file from disk.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(moreDocuments) {
			JFileChooser jfc = new JFileChooser();
			jfc.setDialogTitle("Open file");
			if(jfc.showOpenDialog((Component) model) != JFileChooser.APPROVE_OPTION) return;
			
			if(!Files.isReadable(jfc.getSelectedFile().toPath())) {
				JOptionPane.showMessageDialog(
						(Component) model,
						"Unable to read file: " + jfc.getSelectedFile().toPath().toString(),
						"Error",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			Path path = jfc.getSelectedFile().toPath();
			model.loadDocument(path);
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
