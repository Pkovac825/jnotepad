package hr.fer.zemris.java.hw11.jnotepadpp.local;

public class LocalizationProviderBridge extends AbstractLocalizationProvider implements ILocalizationListener, ILocalizationProvider{
	private boolean connected = false;
	private String language;
	private ILocalizationProvider provider;
	
	private ILocalizationListener listener = new ILocalizationListener() {
		@Override
		public void localizationChanged() {
			language = LocalizationProvider.getInstance().getLanguage();
			LocalizationProviderBridge.this.localizationChanged();
		}
	};
	
	
	public LocalizationProviderBridge(ILocalizationProvider provider) {
		this.provider = provider;
	}
	
	public void connect() {
		if(!connected) {
			provider.addLocalizationListener(listener);
		}
	}
	
	public void disconnect() {
		provider.removeLocalizationListener(listener);
		connected = false;
	}
	
	@Override
	public String getString(String word) {
		return provider.getString(word);
	}

	@Override
	public void localizationChanged() {
		this.fire();
	}

	@Override
	public String getLanguage() {
		return language;
	}

}
