package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.util.Buffer;

/**
 * Akcija koja je sposobna izrezati odabrani tekst.
 *
 * @author Petar Kovač
 *
 */
public class CutAction extends LocalizableAction implements MultipleDocumentListener, CaretListener{


	private static final long serialVersionUID = -3201747062050420955L;
	
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;
	/**
	 * {@link Buffer}
	 */
	private Buffer buffer;

	public CutAction(MultipleDocumentModel model, Buffer buffer, ILocalizationProvider provider) {
		super("Cut", provider);
		this.model = model;
		this.buffer = buffer;
		model.addMultipleDocumentListener(this);
		addDescription();
	}

	private void addDescription() {
		this.putValue(
				Action.NAME,
				"Cut");
		this.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control X"));
		this.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_X);
		this.putValue(
				Action.SHORT_DESCRIPTION, 
				"Copy and eras selected text.");
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		
		try {
			String buffer = editor.getDocument().getText(
					Math.min(editor.getCaret().getDot(), editor.getCaret().getMark()),
					Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark()));
			editor.getDocument().remove(
					Math.min(editor.getCaret().getDot(), editor.getCaret().getMark()),
					Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark()));
			this.buffer.setBuffer(buffer);
		} catch (BadLocationException e1) {
			setEnabled(false);
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		int dot = e.getDot();
		int mark = e.getMark();
		if((dot - mark) == 0) {
			this.setEnabled(false);
		} else {
			this.setEnabled(true);
		}
	}

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		if(previousModel != null) {
			previousModel.getTextComponent().removeCaretListener(this);
		}
		if(currentModel != null) {
			currentModel.getTextComponent().addCaretListener(this);
			this.setEnabled(false);
		}
	}

	@Override
	public void documentAdded(SingleDocumentModel model) {
	}

	@Override
	public void documentRemoved(SingleDocumentModel model) {
	}
}
