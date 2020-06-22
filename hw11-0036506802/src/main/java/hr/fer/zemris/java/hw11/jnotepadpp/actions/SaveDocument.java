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

import hr.fer.zemris.java.hw11.jnotepadpp.DefaultMultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;

/**
 * Akcija koja zna spremiti jedan dokument {@link MultipleDocumentListener}a.
 *
 * @author Petar Kovač
 *
 */
public class SaveDocument extends LocalizableAction implements SingleDocumentListener, MultipleDocumentListener{
	
	private static final long serialVersionUID = -1968245057300416077L;
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private DefaultMultipleDocumentModel model;
	
	
	public SaveDocument(DefaultMultipleDocumentModel model, ILocalizationProvider provider) {
		super("Save", provider);
		this.model = model;
		model.addMultipleDocumentListener(this);
		addDescription();
	}


	private void addDescription() {
		this.putValue(
				Action.NAME,
				"Save");
		this.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control S"));
		this.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_S);
		this.putValue(
				Action.SHORT_DESCRIPTION, 
				"Save document to disk.");
		setEnabled(false);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		SingleDocumentModel current = model.getCurrentDocument();
		
		if(current.getFilePath() != null) {
			model.saveDocument(current, model.getCurrentDocPath());
		} else {
			saveAsOption();
		}
	}
	
	private void saveAsOption() {
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
	public void documentModifyStatusUpdated(SingleDocumentModel model) {
		this.setEnabled(model.isModified());
	}


	@Override
	public void documentFilePathUpdated(SingleDocumentModel model) {
	}


	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		if(previousModel != null) {
			previousModel.removeSingleDocumentListener(this);
		}
		if(currentModel != null) {
			currentModel.addSingleDocumentListener(this);
		}
		setEnabled(currentModel != null);
	}


	@Override
	public void documentAdded(SingleDocumentModel model) {
	}


	@Override
	public void documentRemoved(SingleDocumentModel model) {
	
	}



}
