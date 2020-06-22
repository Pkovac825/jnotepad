package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationProvider extends AbstractLocalizationProvider{
	private String language = "en";
	private ResourceBundle bundle;
	private static LocalizationProvider provider;
	
	private LocalizationProvider() {
	}


	public static LocalizationProvider getInstance() {
		if(provider == null) {
			provider = new LocalizationProvider();
		} 
		return provider;
	}
	
	@Override
	public String getString(String word) {
		return bundle.getString(word);
	}
	
	public void setLanguage(String lang) {
		language = lang;
		provider.bundle =
				 ResourceBundle.getBundle("hr.fer.zemris.java.hw11.jnotepadpp.local.translation",
				 Locale.forLanguageTag(language));
		provider.fire();
	}


	@Override
	public String getLanguage() {
		return language;
	}
	

}
