package hr.fer.zemris.java.hw11.jnotepadpp.local.models;

import javax.swing.JMenu;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener;
import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

/**
 * {@link JMenu} koji ima mogućnost internacionalizacije.
 *
 * @author Petar Kovač
 *
 */
public class LocalizableJMenu extends JMenu implements ILocalizationListener{
	
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
	public LocalizableJMenu (String key, ILocalizationProvider provider) {
		super(key);
		
		
		this.key = key;
		this.provider = provider;
		provider.addLocalizationListener(this);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void localizationChanged() {
		this.setText(provider.getString(key));
	}


}
