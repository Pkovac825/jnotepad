package hr.fer.zemris.java.hw11.jnotepadpp.util;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.ImageIcon;
/**
 * Pomoćna klasa za postavljanje učitavanje slike.
 *
 * @author Petar Kovač
 *
 */
public class ImageLoader {


	/**
	 * Metoda koja dohvaća i sliku i postavlja njenu veličinu.
	 * 
	 * @param resourceName Relativni put do datoteke
	 * @param cls Klasa u čijoj se resursima traži slika
	 * @return Slika
	 * @throws IOException Ako je dan krivi put do slike.
	 * @throws RuntimeException Ako slika ne postoji.
	 */
	public static ImageIcon loadImage(String resourceName, Class<?> cls) throws IOException {
		InputStream is = cls.getResourceAsStream(resourceName);
		if(is==null) {
			throw new RuntimeException();
		}
		byte[] bytes;

		bytes = is.readAllBytes();

		is.close();
		ImageIcon imageIcon = new ImageIcon(bytes);
		Image image = imageIcon.getImage();
		Image newimg = image.getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH);
		imageIcon = new ImageIcon(newimg);
		return imageIcon;

	}
}
