package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.util.Buffer;

/**
 * Akcija koja zna zalijepiti odabrani tekst.
 *
 * @author Petar Kovač
 *
 */
public class PasteAction extends LocalizableAction implements MultipleDocumentListener{


	private static final long serialVersionUID = -3201747062050420955L;
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;
	/**
	 * {@link Buffer}.
	 */
	private Buffer buffer;


	public PasteAction(MultipleDocumentModel model, Buffer buffer, ILocalizationProvider provider) {
		super("Paste", provider);
		this.model = model;
		this.buffer = buffer;
		model.addMultipleDocumentListener(this);
		addDescription();
	}

	private void addDescription() {
		this.putValue(
				Action.NAME,
				"Paste");
		this.putValue(
				Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke("control V"));
		this.putValue(
				Action.MNEMONIC_KEY,
				KeyEvent.VK_V);
		this.putValue(
				Action.SHORT_DESCRIPTION, 
				"Paste copied text.");
		this.setEnabled(false);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		try {
			editor.getDocument().remove(
					Math.min(editor.getCaret().getDot(), editor.getCaret().getMark()),
					Math.abs(editor.getCaret().getDot() - editor.getCaret().getMark()));
		} catch (BadLocationException e1) {
		}

		editor.insert(buffer.getBuffer(), editor.getCaretPosition());
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
