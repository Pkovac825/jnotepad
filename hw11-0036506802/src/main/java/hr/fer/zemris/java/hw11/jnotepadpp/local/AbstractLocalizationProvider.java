package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLocalizationProvider implements ILocalizationProvider{
	
	private List<ILocalizationListener> list = new ArrayList<>();

	
	
	public AbstractLocalizationProvider() {
	}

	@Override
	public void addLocalizationListener(ILocalizationListener l) {
		list.add(l);
	}

	@Override
	public void removeLocalizationListener(ILocalizationListener l) {
		list.remove(l);
	}

	void fire() {
		list.forEach((s) -> s.localizationChanged());
	}

}
