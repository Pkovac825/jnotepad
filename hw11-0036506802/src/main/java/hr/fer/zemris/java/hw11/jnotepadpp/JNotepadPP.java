package hr.fer.zemris.java.hw11.jnotepadpp;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hr.fer.zemris.java.hw11.jnotepadpp.actions.CopyAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.CutAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.DocumentInfo;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.ExitDocument;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.Languages;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.NewDocument;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.OpenDocument;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.PasteAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.RemoveDocument;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SaveAsAction;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SaveDocument;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.SortActions;
import hr.fer.zemris.java.hw11.jnotepadpp.actions.ToggleTools;
import hr.fer.zemris.java.hw11.jnotepadpp.local.FormLocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.models.LocalizableJMenu;
import hr.fer.zemris.java.hw11.jnotepadpp.model.MultipleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.model.SingleDocumentModel;
import hr.fer.zemris.java.hw11.jnotepadpp.util.Buffer;

/**
 * Klasa koja predstavlja uređivač teksta koji ima sposobnost
 * rada nad više tekstualnih datoteka odjednom.
 *
 * @author Petar Kovač
 *
 */
public class JNotepadPP extends JFrame{

	// ===============================SERIAL VERSION ID====================//
	private static final long serialVersionUID = 5729111831966518041L;

	/**
	 * {@link MultipleDocumentModel} ovog uređivača teksta.
	 */
	private DefaultMultipleDocumentModel docModel;
	/**
	 * Privremeni spremnik.
	 */
	private Buffer buffer;
	/**
	 * {@link FormLocalizationProvider}
	 */
	private FormLocalizationProvider flp;

	
	/**
	 * Zadani konstruktor.
	 */
	public JNotepadPP() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		flp = new FormLocalizationProvider(LocalizationProvider.getInstance(), this);
		docModel = new DefaultMultipleDocumentModel();
		docModel.addChangeListener(changeListener);
		buffer = new Buffer();
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				new ExitDocument(docModel, JNotepadPP.this, JNotepadPP.this.flp).actionPerformed(null);
			}
		});
		setLocation(10, 10);
		setSize(500,500);
		initGUI();
	}
	
	/**
	 * Inicijalizacija prozora.
	 */
	public void initGUI() {
		JFrame frame = new JFrame();
		Container cp = frame.getContentPane();
		
		cp.setLayout(new BorderLayout());
		cp.add(new JScrollPane(docModel), BorderLayout.CENTER);
		cp.add(new InfoStatusBar(docModel), BorderLayout.PAGE_END);
		
		Container topCP = getContentPane();
		
		
		createMenus();
		topCP.add(createToolbar(), BorderLayout.PAGE_START);
		topCP.add(cp, BorderLayout.CENTER);
	}
	
	/**
	 * Stvaranje izbornika.
	 */
	private void createMenus() {
		JMenuBar mb = new JMenuBar();
		ToggleTools toggleTool = new ToggleTools(docModel, flp);
		SortActions sortAction = new SortActions(docModel, flp);
		
		LocalizableJMenu edit = new LocalizableJMenu("Edit", flp);
		LocalizableJMenu file = new LocalizableJMenu("File", flp);
		LocalizableJMenu language = new LocalizableJMenu("Language", flp);
		LocalizableJMenu tools = new LocalizableJMenu("Tools", flp);
		LocalizableJMenu changeCase = new LocalizableJMenu("Change_Case", flp);
		LocalizableJMenu sort = new LocalizableJMenu("Sort", flp);
		
		file.add(new JMenuItem(new NewDocument(docModel, flp)));
		file.add(new JMenuItem(new OpenDocument(docModel, flp)));
		file.addSeparator();
		file.add(new JMenuItem(new SaveDocument(docModel, flp)));
		file.add(new JMenuItem(new SaveAsAction(docModel, flp)));
		file.addSeparator();
		file.add(new JMenuItem(new RemoveDocument(docModel, flp)));
		file.add(new JMenuItem(new DocumentInfo(docModel, flp)));
		file.add(new JMenuItem(new ExitDocument(docModel, this, flp)));
		
		edit.add(new JMenuItem(new CopyAction(docModel, buffer, flp)));
		edit.add(new JMenuItem(new PasteAction(docModel, buffer, flp)));
		edit.add(new JMenuItem(new CutAction(docModel, buffer, flp)));
		
		changeCase.add(new JMenuItem(toggleTool.getUppercaseAction()));
		changeCase.add(new JMenuItem(toggleTool.getLowercaseAction()));
		changeCase.add(new JMenuItem(toggleTool.getToggleAction()));
		
		sort.add(new JMenuItem(sortAction.getAsc()));
		sort.add(new JMenuItem(sortAction.getDesc()));
		
		
		language.add(new JMenuItem(Languages.hrAction(flp)));
		language.add(new JMenuItem(Languages.enAction(flp)));
		language.add(new JMenuItem(Languages.deAction(flp)));
		
		tools.add(changeCase);
		tools.add(sort);
		tools.add(new JMenuItem(sortAction.getUnique()));
		
		
		mb.add(file);
		mb.add(edit);
		mb.add(language);
		mb.add(tools);

		setJMenuBar(mb);
	}

	/**
	 * Stvaranje alatne trake.
	 * @return Stvorena alatna traka
	 */
	private JToolBar createToolbar() {
		JToolBar tb = new JToolBar();
		
		tb.setFloatable(true);
		tb.add(new JButton(new NewDocument(docModel, flp)));
		tb.add(new JButton(new OpenDocument(docModel, flp)));
		
		tb.addSeparator();
		tb.add(new JButton(new SaveDocument(docModel, flp)));
		tb.add(new JButton(new SaveAsAction(docModel, flp)));
		
		tb.addSeparator();
		tb.add(new JButton(new CopyAction(docModel, buffer, flp)));
		tb.add(new JButton(new CutAction(docModel, buffer, flp)));
		tb.add(new JButton(new PasteAction(docModel, buffer, flp)));
		
		tb.addSeparator();
		tb.add(new JButton(new RemoveDocument(docModel, flp)));
		tb.add(new JButton(new DocumentInfo(docModel, flp)));
		tb.add(new JButton(new ExitDocument(docModel, this, flp)));
		
		return tb;
		
	}
	
	/**
	 * Promatrač koji mijenja ime prozora kada se dogodi promjena nad dokumentom.
	 */
	private final ChangeListener changeListener = new ChangeListener() {
		 public void stateChanged(ChangeEvent e) {
			 if(docModel.getNumberOfDocuments() == 0) return;
			 DefaultMultipleDocumentModel model =  JNotepadPP.this.docModel;		 
			 SingleDocumentModel document = model.getCurrentDocument();
			 
			 String name = document.getFilePath() == null ? "unnamed" : document.getFilePath().toString();
			 
			 setTitle(name  + " - JNotepad++");
		 }
	};

	/**
	 * Main metoda u kojoj se pokreće EDT
	 * @param args Zanemaruju se
	 */
	public static void main(String []args) {
		SwingUtilities.invokeLater(() -> new JNotepadPP().setVisible(true));
	}
}
