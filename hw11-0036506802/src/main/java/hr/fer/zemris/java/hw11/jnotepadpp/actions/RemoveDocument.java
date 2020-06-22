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
 * Akcija koja zna maknuti jedan dokument iz {@link MultipleDocumentModel}a.
 *
 * @author Petar Kovač
 *
 */
public class RemoveDocument extends LocalizableAction implements MultipleDocumentListener{


	private static final long serialVersionUID = -3201747062050420955L;
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;
	
	public RemoveDocument(MultipleDocumentModel model, ILocalizationProvider provider) {
		super("Remove", provider);
		this.model = model;
		model.addMultipleDocumentListener(this);
		addDescription();
	}

	private void addDescription() {
		this.putValue(
				Action.NAME,
				"Remove");
		this.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control R"));
		this.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_R);
		this.putValue(
				Action.SHORT_DESCRIPTION, 
				"Remove current file.");
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		SingleDocumentModel current = model.getCurrentDocument();
		if(model.getCurrentDocument().isModified()) {
			
			int chosenOption = JOptionPane.showOptionDialog((Component) model, 
					"File was modified.\n"
					+ "Would you like to save changes?",
					"Save",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					new Object[]{"Yes", "No"},
					"Yes");
			if(chosenOption == JOptionPane.YES_OPTION) {
				if(current.getFilePath() != null) {
					model.saveDocument(current, current.getFilePath());
				} else {
					Path path = chosenFile();
					if(path == null) return;
					model.saveDocument(current, path);
				}
			}
		}
		model.closeDocument(current);
	}

	private Path chosenFile() {
		SingleDocumentModel current = model.getCurrentDocument();
		
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogTitle("Save file");
		if(jfc.showOpenDialog((Component) model) != JFileChooser.APPROVE_OPTION) return null;
		
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
					return jfc.getSelectedFile().toPath();
				}
				
			} else {
				return current.getFilePath();
			}
		}
		if(!Files.exists(path)) {
			return jfc.getSelectedFile().toPath();
		}
		return null;
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
