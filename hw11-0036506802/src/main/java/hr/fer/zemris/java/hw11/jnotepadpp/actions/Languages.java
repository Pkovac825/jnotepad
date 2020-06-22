package hr.fer.zemris.java.hw11.jnotepadpp.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableAction;

/**
 * Klasa koja čuva metode koje znaju stvarati primjerke akcija koje mijenjaju jezik.
 */
public class Languages {
	
	/**
	 * Metoda tvornica za dobivanje jedne {@link HrAction}
	 */
	public static LocalizableAction hrAction (ILocalizationProvider provider) {
		return new HrAction(provider);
	}
	/**
	 * Metoda tvornica za dobivanje jedne {@link EnAction}
	 */
	public static LocalizableAction enAction (ILocalizationProvider provider) {
		return new EnAction(provider);
	}
	/**
	 * Metoda tvornica za dobivanje jedne {@link DeAction}
	 */
	public static LocalizableAction deAction (ILocalizationProvider provider) {
		return new DeAction(provider);
	}
	
	/**
	 * Akcija koja zna promijeniti jezik na hrvatski.
	 *
	 * @author Petar Kovač
	 *
	 */
	private static class HrAction extends LocalizableAction {
		
		private static final long serialVersionUID = -3201747062050420955L;
		

		public HrAction(ILocalizationProvider provider) {
			super("Croatian", provider);
			addDescription();
		}
		
		private void addDescription() {
			this.putValue(
					Action.NAME,
					"Croatian");
			this.putValue(
					Action.SHORT_DESCRIPTION, 
					"Switch language to Croatian.");
		}


		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("hr");
		}
	}
	
	/**
	 * Akcija koja zna promijeniti jezik na engleski.
	 *
	 * @author Petar Kovač
	 *
	 */
	private static class EnAction extends LocalizableAction {
		
		private static final long serialVersionUID = -3201747062050420955L;
		

		public EnAction(ILocalizationProvider provider) {
			super("English", provider);
			addDescription();
		}
		
		private void addDescription() {
			this.putValue(
					Action.NAME,
					"English");
			this.putValue(
					Action.SHORT_DESCRIPTION, 
					"Switch language to English.");
		}


		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("en");
		}
	}

	/**
	 * Akcija koja zna promiejniti jezik na njemački.
	 *
	 * @author Petar Kovač
	 *
	 */
	private static class DeAction extends LocalizableAction {
		
		private static final long serialVersionUID = -3201747062050420955L;
		
	
		public DeAction(ILocalizationProvider provider) {
			super("German", provider);
			addDescription();
		}
		
		private void addDescription() {
			this.putValue(
					Action.NAME,
					"German");
			this.putValue(
					Action.SHORT_DESCRIPTION, 
					"Switch language to German.");
		}
	
	
		@Override
		public void actionPerformed(ActionEvent e) {
			LocalizationProvider.getInstance().setLanguage("de");
		}
	}
}
