package hr.fer.zemris.java.hw11.jnotepadpp.actions;


import java.awt.event.ActionEvent;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.swing.Action;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableAction;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;
/**
 * Klasa koja sadrži sve metode potrebne za izvođenje akcija sortiranja i izbacivanja.
 *
 * @author Petar Kovač
 *
 */
public class SortActions implements MultipleDocumentListener, CaretListener{
	/**
	 * {@link MultipleDocumentModel} ove akcije.
	 */
	private MultipleDocumentModel model;
	/**
	 * Lokalizator.
	 */
	private ILocalizationProvider provider;
	/**
	 * {@link Collator}
	 */
	private static Collator col = Collator.getInstance(new Locale("en"));
	/**
	 * {@link AscSortAction}
	 */
	private static AscSortAction asc;
	/**
	 * {@link DescSortAction}
	 */
	private static DescSortAction desc;
	/**
	 * {@link UniqueAction}
	 */
	private static UniqueAction unique;

	public SortActions(MultipleDocumentModel model, ILocalizationProvider provider) {
		this.model = model;
		this.provider = provider;
		model.addMultipleDocumentListener(this);
		provider.addLocalizationListener(new ILocalizationListener() {	
			@Override
			public void localizationChanged() {
				SortActions.col = Collator.getInstance(
				new Locale(LocalizationProvider.getInstance().getLanguage()));
			}
		});
	}
	
	public AscSortAction getAsc() {
		if(asc == null) {
			asc = new AscSortAction(model, provider);
		}
		return asc;
	}
	
	public DescSortAction getDesc() {
		if(desc == null) {
			desc = new DescSortAction(model, provider);
		}
		return desc;
	}
	
	public UniqueAction getUnique() {
		if(unique == null) {
			unique = new UniqueAction(model, provider);
		}
		return unique;
	}
	
	
	public static int ascSort(String s1, String s2) {
		return col.compare(s1, s2);
	}
	
	public static int descSort(String s1, String s2) {
		return ascSort(s2, s1);
	}

	/**
	 * Akcija koja zna poredati izabrane linije teksta uzlaznim poretkom s obzirom na trenutni
	 * odabrani jezik.
	 *
	 * @author Petar Kovač
	 *
	 */
	private static class AscSortAction extends LocalizableAction{

		private static final long serialVersionUID = -3201747062050420955L;	
		/**
		 * {@link MultipleDocumentModel} ove akcije.
		 */
		private MultipleDocumentModel model;

		public AscSortAction (MultipleDocumentModel model, ILocalizationProvider provider) {
			super("Ascending", provider);
			this.model = model;
			addDescription();
		}
		private void addDescription() {
			this.putValue(
					Action.NAME,
					"Ascending Sort");
			this.putValue(
					Action.SHORT_DESCRIPTION, 
					"Sorts selected lines in ascending order.");
			this.setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent editor = model.getCurrentDocument().getTextComponent();
			Caret caret = editor.getCaret();
			Document doc = editor.getDocument();
			Element root = doc.getDefaultRootElement();
			int dot = Math.min(caret.getMark(), caret.getDot());
			int mark = Math.max(caret.getMark(), caret.getDot());
			int dotRow = root.getElementIndex(dot);
			int markRow = root.getElementIndex(mark);
			dot = root.getElement(dotRow).getStartOffset();
			mark = root.getElement(markRow).getEndOffset();
			
			try {
				String []lines = doc.getText(dot, mark - dot).split("\n");
				doc.remove(dot, mark - dot - 1);
				List<String> comparable = new ArrayList<String>();
				for(String line : lines) {
					comparable.add(line);
				}
				Collections.sort(comparable, SortActions::ascSort);
				for(String line : comparable) {
					doc.insertString(dot, line + "\n", null);
					dot += line.length() + 1;
				}
			} catch (BadLocationException e1) {
			}

		}
	}
	/**
	 * Akcija koja zna poredati izabrane linije teksta silaznim poretkom s obzirom na trenutni
	 * odabrani jezik.
	 *
	 * @author Petar Kovač
	 *
	 */
	private static class DescSortAction extends LocalizableAction{

		private static final long serialVersionUID = -3201747062050420955L;	
		/**
		 * {@link MultipleDocumentModel} ove akcije.
		 */
		private MultipleDocumentModel model;

		public DescSortAction (MultipleDocumentModel model, ILocalizationProvider provider) {
			super("Descending", provider);
			this.model = model;
			addDescription();
		}
		private void addDescription() {
			this.putValue(
					Action.NAME,
					"Descending Sort");
			this.putValue(
					Action.SHORT_DESCRIPTION, 
					"Sorts selected lines in descending order.");
			this.setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent editor = model.getCurrentDocument().getTextComponent();
			Caret caret = editor.getCaret();
			Document doc = editor.getDocument();
			Element root = doc.getDefaultRootElement();
			int dot = Math.min(caret.getMark(), caret.getDot());
			int mark = Math.max(caret.getMark(), caret.getDot());
			int dotRow = root.getElementIndex(dot);
			int markRow = root.getElementIndex(mark);
			dot = root.getElement(dotRow).getStartOffset();
			mark = root.getElement(markRow).getEndOffset();
			
			try {
				String []lines = doc.getText(dot, mark - dot).split("\n");
				doc.remove(dot, mark - dot - 1);
				List<String> comparable = new ArrayList<String>();
				for(String line : lines) {
					comparable.add(line);
				}
				Collections.sort(comparable, SortActions::descSort);
				for(String line : comparable) {
					doc.insertString(dot, line + "\n", null);
					dot += line.length() + 1;
				}
			} catch (BadLocationException e1) {
			}
		}
	}
	
	/**
	 * Akcija koja zna izbaciti duplikate iz trenutno odabranih linija.
	 *
	 * @author Petar Kovač
	 *
	 */
	private static class UniqueAction extends LocalizableAction{

		private static final long serialVersionUID = -3201747062050420955L;	
		/**
		 * {@link MultipleDocumentModel} ove akcije.
		 */
		private MultipleDocumentModel model;

		public UniqueAction (MultipleDocumentModel model, ILocalizationProvider provider) {
			super("Unique", provider);
			this.model = model;
			addDescription();
		}
		private void addDescription() {
			this.putValue(
					Action.NAME,
					"Unique");
			this.putValue(
					Action.SHORT_DESCRIPTION, 
					"Removes duplicates from selected lines.");
			this.setEnabled(false);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JTextComponent editor = model.getCurrentDocument().getTextComponent();
			Caret caret = editor.getCaret();
			Document doc = editor.getDocument();
			Element root = doc.getDefaultRootElement();
			int dot = Math.min(caret.getMark(), caret.getDot());
			int mark = Math.max(caret.getMark(), caret.getDot());
			int dotRow = root.getElementIndex(dot);
			int markRow = root.getElementIndex(mark);
			dot = root.getElement(dotRow).getStartOffset();
			mark = root.getElement(markRow).getEndOffset();
			
			try {
				String []lines = doc.getText(dot, mark - dot).split("\n");
				doc.remove(dot, mark - dot - 1);
				List<String> comparable = new ArrayList<String>();
				for(String line : lines) {
					comparable.add(line);
				}
				comparable = comparable.stream().distinct().collect(Collectors.toList());
				for(String line : comparable) {
					doc.insertString(dot, line + "\n", null);
					dot += line.length() + 1;
				}
			} catch (BadLocationException e1) {
			}
		}
	}

	@Override
	public void caretUpdate(CaretEvent e) {
		int dot = e.getDot();
		int mark = e.getMark();
		if((dot - mark) == 0) {
			asc.setEnabled(false);
			desc.setEnabled(false);
			unique.setEnabled(false);
		} else {
			asc.setEnabled(true);
			desc.setEnabled(true);
			unique.setEnabled(true);

		}
	}

	@Override
	public void currentDocumentChanged(SingleDocumentModel previousModel, SingleDocumentModel currentModel) {
		if(previousModel != null) {
			previousModel.getTextComponent().removeCaretListener(this);
		}
		if(currentModel != null) {
			currentModel.getTextComponent().addCaretListener(this);
			asc.setEnabled(false);
			desc.setEnabled(false);
			unique.setEnabled(false);
		}
	}

	@Override
	public void documentAdded(SingleDocumentModel model) {
	}

	@Override
	public void documentRemoved(SingleDocumentModel model) {
	}
}
