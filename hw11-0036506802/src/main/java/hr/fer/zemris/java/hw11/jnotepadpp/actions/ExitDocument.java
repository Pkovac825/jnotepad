package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.util.Constants;

/**
 * Akcija koja je sposobna izaći iz {@link MultipleDocumentModel}a.
 * Usput pokušava spremiti sve promijenjene datoteke.
 *
 * @author Petar Kovač
 *
 */
public class ExitDocument extends LocalizableAction{


	private static final long serialVersionUID = -3201747062050420955L;
	
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;
	/**
	 * Vršna komponenta.
	 */
	private JFrame parent;
	
	public ExitDocument(MultipleDocumentModel model, JFrame parent, ILocalizationProvider provider) {
		super("Exit", provider);
		this.model = model;
		this.parent = parent;
		addDescription();
	}

	private void addDescription() {
		this.putValue(
				Action.NAME,
				"Exit");
		this.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control E"));
		this.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_E);
		this.putValue(
				Action.SHORT_DESCRIPTION, 
				"Exit file.");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for(int i = 0; i < this.model.getNumberOfDocuments(); i++) {
			if(!saveOption(this.model.getDocument(i))) return;
		}
		
		
		List<SingleDocumentModel> copy = new ArrayList<SingleDocumentModel>();
		model.forEach((doc) -> copy.add(doc));
		
		Iterator<SingleDocumentModel> it = copy.iterator();
		while(it.hasNext()) {
			this.model.closeDocument(it.next());
		}
		Constants.stopMe = true;
		parent.dispose();
	}

	private boolean saveOption(SingleDocumentModel doc) {
		SingleDocumentModel current = doc;
		if(current.isModified()) {
			
			int chosenOption = JOptionPane.showOptionDialog((Component) model, 
					"File was modified.\n"
					+ "Would you like to save changes?",
					"Save",
					JOptionPane.YES_NO_CANCEL_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,
					new Object[]{"Yes", "No", "Cancel"},
					"Yes");
			if(chosenOption == JOptionPane.YES_OPTION) {
				if(current.getFilePath() != null) {
					model.saveDocument(current, current.getFilePath());
				} else {
					Path path = chosenFile();
					if(path == null) return false;
					model.saveDocument(current, path);
				}
			}
			if(chosenOption == JOptionPane.CANCEL_OPTION) return false;
		}
		return true;
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
}
