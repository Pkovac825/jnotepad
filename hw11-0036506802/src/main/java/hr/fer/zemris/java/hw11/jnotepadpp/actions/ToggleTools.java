package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.Action;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;

/**
 * Klasa koja sadrži sve metode potrebne za rad akcija koje prebacuju slova iz velikih
 * u mala i obratno.
 *
 * @author Petar Kovač
 *
 */
public class ToggleTools implements MultipleDocumentListener, CaretListener{
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;
	/**
	 * Lokalizator.
	 */
	private ILocalizationProvider provider;
	/**
	 * {@link UppercaseAction}
	 */
	private static UppercaseAction up;
	/**
	 * {@link LowercaseAction}
	 */
	private static LowercaseAction lower;
	/**
	 * {@link ToggleAction}
	 */
	private static ToggleAction toggle;
	
	
	
	public ToggleTools(MultipleDocumentModel model, ILocalizationProvider provider) {
		this.model = model;
		this.provider = provider;
		model.addMultipleDocumentListener(this);
	}
	
	public UppercaseAction getUppercaseAction() {
		if(up == null) {
			up = new UppercaseAction(model, provider);
		}
		return up;
	}
	
	public LowercaseAction getLowercaseAction() {
		if(lower == null) {
			lower = new LowercaseAction(model, provider);
		}
		return lower;
	}
	
	public ToggleAction getToggleAction() {
		if(toggle == null) {
			toggle = new ToggleAction(model, provider);
		}
		return toggle;
	}
	
	public static String toggle(String text, boolean toggleUp, boolean toggleDown) {
		StringBuilder builder = new StringBuilder();
		for(int i = 0, limit = text.length(); i < limit; i++) {
			Character currentChar = text.charAt(i);
			if(Character.isUpperCase(currentChar) && toggleDown) {
				builder.append(Character.toLowerCase(currentChar));
			}
			if(Character.isLowerCase(currentChar) && toggleUp) {
				builder.append(Character.toUpperCase(currentChar));
			} else {
				builder.append(currentChar);
			}
		}
		return builder.toString();
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void caretUpdate(CaretEvent e) {
		int dot = e.getDot();
		int mark = e.getMark();
		if((dot - mark) == 0) {
			up.setEnabled(false);
			lower.setEnabled(false);
			toggle.setEnabled(false);
		} else {
			up.setEnabled(true);
			lower.setEnabled(true);
			toggle.setEnabled(true);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		if(previousModel != null) {
			previousModel.getTextComponent().removeCaretListener(this);
		}
		if(currentModel != null) {
			currentModel.getTextComponent().addCaretListener(this);
			up.setEnabled(false);
			lower.setEnabled(false);
			toggle.setEnabled(false);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void documentAdded(SingleDocumentModel model) {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void documentRemoved(SingleDocumentModel model) {
	}
	
	/**
	 * Akcija koja mijenja slova ovog teksta u velika slova.
	 */
	private static class UppercaseAction extends LocalizableAction{
		
		private static final long serialVersionUID = -3201747062050420955L;	
		private MultipleDocumentModel model;

		public UppercaseAction (MultipleDocumentModel model, ILocalizationProvider provider) {
			super("Uppercase", provider);
			this.model = model;
			addDescription();
		}
		private void addDescription() {
			this.putValue(
					Action.NAME,
					"Uppercase");
			this.putValue(
					Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke("control U"));
			this.putValue(
					Action.MNEMONIC_KEY,
					KeyEvent.VK_U);
			this.putValue(
					Action.SHORT_DESCRIPTION, 
					"Toggle selected text to uppercase.");
			this.setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = model.getCurrentDocument().getTextComponent();
			Caret caret = editor.getCaret();
			Document doc = editor.getDocument();
			int from = Math.min(caret.getDot(), caret.getMark());
			int len = Math.abs(caret.getDot() - caret.getMark());
			try {
				String buffer = ToggleTools.toggle(editor.getSelectedText(), true, false);
				doc.remove(from, len);
				doc.insertString(from, buffer, null);
			} catch (BadLocationException e1) {
				setEnabled(false);
			}
		}
	}

	/**
	 * Akcija koja mijenja slova ovog teksta u mala slova.
	 */
	private static class LowercaseAction extends LocalizableAction{
		
		private static final long serialVersionUID = -3201747062050420955L;	
		private MultipleDocumentModel model;

		public LowercaseAction (MultipleDocumentModel model, ILocalizationProvider provider) {
			super("Lowercase", provider);
			this.model = model;
			addDescription();
		}
		private void addDescription() {
			this.putValue(
					Action.NAME,
					"Lowercase");
			this.putValue(
					Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke("control L"));
			this.putValue(
					Action.MNEMONIC_KEY,
					KeyEvent.VK_L);
			this.putValue(
					Action.SHORT_DESCRIPTION, 
					"Toggle selected text to lowercase.");
			this.setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = model.getCurrentDocument().getTextComponent();
			Caret caret = editor.getCaret();
			Document doc = editor.getDocument();
			int from = Math.min(caret.getDot(), caret.getMark());
			int len = Math.abs(caret.getDot() - caret.getMark());
			try {
				String buffer = ToggleTools.toggle(editor.getSelectedText(), false, true);
				doc.remove(from, len);
				doc.insertString(from, buffer, null);
			} catch (BadLocationException e1) {
				setEnabled(false);
			}
		}
	}
	
	/**
	 * Akcija koja mijenja slova ovog teksta iz velikih u mala i iz malih u velika.
	 */
	private static class ToggleAction extends LocalizableAction{

		private static final long serialVersionUID = -3201747062050420955L;	
		private MultipleDocumentModel model;

		public ToggleAction (MultipleDocumentModel model, ILocalizationProvider provider) {
			super("Toggle", provider);
			this.model = model;
			addDescription();
		}
		private void addDescription() {
			this.putValue(
					Action.NAME,
					"Toggle");
			this.putValue(
					Action.ACCELERATOR_KEY,
					KeyStroke.getKeyStroke("control T"));
			this.putValue(
					Action.MNEMONIC_KEY,
					KeyEvent.VK_T);
			this.putValue(
					Action.SHORT_DESCRIPTION, 
					"Toggle selected text.");
			this.setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextArea editor = model.getCurrentDocument().getTextComponent();
			Caret caret = editor.getCaret();
			Document doc = editor.getDocument();
			int from = Math.min(caret.getDot(), caret.getMark());
			int len = Math.abs(caret.getDot() - caret.getMark());
			try {
				String buffer = ToggleTools.toggle(editor.getSelectedText(), true, true);
				doc.remove(from, len);
				doc.insertString(from, buffer, null);
			} catch (BadLocationException e1) {
				setEnabled(false);
			}
		}
	}
}
