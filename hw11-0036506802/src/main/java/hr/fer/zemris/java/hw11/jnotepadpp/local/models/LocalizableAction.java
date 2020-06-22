package hr.fer.zemris.java.hw11.jnotepadpp.local.models;

import javax.swing.AbstractAction;
import javax.swing.Action;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * {@link Action} koja ima mogućnost internacionalizacije.
 *
 *
 */
public abstract class LocalizableAction extends AbstractAction implements ILocalizationListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Ključ za internacionalizaciju.
	 */
	private String key;
	/**
	 * Lokalizator.
	 */
	private ILocalizationProvider provider;
	
	/**
	 * Zadani konstruktor. Postavlja ključ za lokalizaciju i dodaje ovaj objekt kao promatrača lokalizatoru.
	 * @param key
	 * @param provider
	 */
	public LocalizableAction(String key, ILocalizationProvider provider) {
		this.key = key;
		this.provider = provider;
		provider.addLocalizationListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void localizationChanged() {
		this.putValue(Action.NAME, provider.getString(key));
		this.putValue(Action.SHORT_DESCRIPTION, provider.getString(key + "Desc"));
	}


}
