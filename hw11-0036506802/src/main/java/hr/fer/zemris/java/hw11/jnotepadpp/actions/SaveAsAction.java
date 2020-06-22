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

/**
 * Akcija koja zna spremiti jedan dokument {@link MultipleDocumentModel} i
 * upitati korisnika na koju putanju je želi spremiti.
 *
 * @author Petar Kovač
 *
 */
public class SaveAsAction extends LocalizableAction implements MultipleDocumentListener{

private static final long serialVersionUID = 8837040887680407762L;
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;

	
	public SaveAsAction(MultipleDocumentModel model, ILocalizationProvider provider) {
		super("Save_As", provider);
		this.model = model;
		model.addMultipleDocumentListener(this);

		addDescription();
	}


	private void addDescription() {
		putValue(
				Action.NAME,
				"Save As");
		putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("F12"));
		putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_A);
		putValue(
				Action.SHORT_DESCRIPTION, 
				"Save document to disk.");
		setEnabled(false);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		SingleDocumentModel current = model.getCurrentDocument();
		
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Save file");
		if(jfc.showOpenDialog((Component) model) != JFileChooser.APPROVE_OPTION) return;
		
		Path path = jfc.getSelectedFile().toPath();
		if(Files.isWritable(path)) {
			if(!path.equals(current.getFilePath())) {
				int chosenOption = JOptionPane.showOptionDialog((Component) model, 
												"File already exists.\n"
												+ "Do you want to overwrite file?",
												"Save As",
												JOptionPane.YES_NO_OPTION,
												JOptionPane.QUESTION_MESSAGE,
												null,
												new Object[]{"Yes", "No"},
												"Yes");
				if(chosenOption == JOptionPane.YES_OPTION) {
					model.saveDocument(current, jfc.getSelectedFile().toPath());
				}
			} else {
				model.saveDocument(current, current.getFilePath());
			}
		}
		
		if(!Files.exists(path)) {
			model.saveDocument(current, jfc.getSelectedFile().toPath());
		}
	}


	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		setEnabled(currentModel != null);
	}


	@Override
	public void documentAdded(SingleDocumentModel model) {
		setEnabled(true);
	}


	@Override
	public void documentRemoved(SingleDocumentModel model) {
	}

}
