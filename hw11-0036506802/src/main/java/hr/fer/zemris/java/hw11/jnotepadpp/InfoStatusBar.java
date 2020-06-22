package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.Color;
import java.awt.GridLayout;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Caret;
import javax.swing.text.Element;

import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentListener;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.util.Constants;

/**
 * Razred koji predstavlja statusnu traku za jedan {@link JNotepadPP}.
 *
 * @author Petar Kovač
 *
 */
public class InfoStatusBar extends JPanel implements MultipleDocumentListener, CaretListener{
	/**
	 * Broj redaka.
	 */
	private static final int ROW_COUNT = 1;
	/**
	 * Broj stupaca.
	 */
	private static final int COLUMN_COUNT = 2;
	/**
	 * Model od kojeg statusna traka dobiva informacije.
	 */
	private MultipleDocumentModel model;
	/**
	 * Labele u kojima se upisuju informacije.
	 */
	private List<JLabel> information;
	/**
	 * Dretva koja upravlja satom.
	 */
	private Thread clock;
	/**
	 * Formatter koji određuje format ispisa vremenskog trenutka.
	 */
	private DateTimeFormatter formatter;
	

	private static final long serialVersionUID = -7380124425251526852L;
	/**
	 * Zadani konstruktor
	 * @param model Model nad kojim statusna traka radi
	 */
	public InfoStatusBar(MultipleDocumentModel model) {
		super();
		this.model = model;
		formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		setBorder(BorderFactory.createMatteBorder(4, 1, 1, 1, Color.GRAY));
		setBackground(Color.LIGHT_GRAY);
		
		information = new ArrayList<>();
		
		clock = new Thread(() -> {
			while(true) {
				try {
					Thread.sleep(500);
				} catch(Exception ex) {}
				if(Constants.stopMe) break;
				SwingUtilities.invokeLater(()->{
					updateTime();
				});
			}
		});
		clock.setDaemon(true);;
		clock.start();
		
		
		setLayout(new GridLayout(ROW_COUNT, COLUMN_COUNT, 0, 1));
		
		JFrame first = new JFrame();
		JLabel firstLeft = new JLabel("length: 0", JLabel.LEFT);
		firstLeft.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
		firstLeft.setBackground(Color.LIGHT_GRAY);
		first.getContentPane().add(firstLeft);
		information.add(firstLeft);
		
		JFrame second = new JFrame();
		second.setLayout(new GridLayout(1, 2));
		JLabel secondLeft = new JLabel("Ln:1 Col:1 Sel:0", JLabel.LEFT);
		second.getContentPane().add(secondLeft);
		secondLeft.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.WHITE));
		secondLeft.setBackground(Color.LIGHT_GRAY);
		information.add(secondLeft);
		
		JLabel secondRight = new JLabel(formatter.format(LocalDateTime.now()), JLabel.RIGHT);
		second.getContentPane().add(secondRight);
		secondRight.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.WHITE));
		secondRight.setBackground(Color.LIGHT_GRAY);
		information.add(secondRight);
		
		add(first.getContentPane());
		add(second.getContentPane());
		
		model.addMultipleDocumentListener(this);

	}
	
	/**
	 * Metoda za obnavljanje statusa sata.
	 */
	private void updateTime() {
		information.get(2).setText(formatter.format(LocalDateTime.now()));
	}


	@Override
	public void caretUpdate(CaretEvent e) {	
		JTextArea editor = model.getCurrentDocument().getTextComponent();
		String text = editor.getText();
		Caret caret = editor.getCaret();
		Element root = editor.getDocument().getDefaultRootElement();
		
		int row = root.getElementIndex(caret.getDot());

		information.get(0).setText("length:" + text.length());
		information.get(1).setText(String.format(
									"Ln:%d Col:%d Sel:%d",
									row + 1,
									caret.getDot() - root.getElement(row).getStartOffset() + 1, 
									Math.abs(caret.getDot() - caret.getMark())));

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
			caretUpdate(null);
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

}
