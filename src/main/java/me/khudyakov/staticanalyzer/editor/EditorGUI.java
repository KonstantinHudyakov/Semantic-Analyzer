package me.khudyakov.staticanalyzer.editor;

import me.khudyakov.staticanalyzer.service.*;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditorGUI extends JFrame {

    public static void main(String[] args) {
        new EditorGUI();
    }

    private final CodeParser codeParser = new CodeParserImpl();
    private final SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzerImpl();
    private final SyntaxTreeVisitor programExecutionVisitor = new ProgramExecutionVisitor();
    private final SyntaxTreeCache syntaxTreeCache = new SyntaxTreeCache(3);
    private final FeatureFinder framingIfFinder = new FramingIfFinder();

    private final JTextArea codeArea;

    public EditorGUI() {
        super("JavaEdit");
        codeArea = createCodeArea();
        JScrollPane codeAreaScroll = new JScrollPane(codeArea);
        codeAreaScroll.setPreferredSize(new Dimension(800, 350));

        JTextArea outputArea = createOutputArea();
        JScrollPane outputAreaScroll = new JScrollPane(outputArea);
        outputAreaScroll.setPreferredSize(new Dimension(800, 150));

        createEditorWindow(codeAreaScroll, outputAreaScroll);
    }

    private void createEditorWindow(JScrollPane codeAreaScroll, JScrollPane outputAreaScroll) {
        JFrame editorWindow = new JFrame("Simple IDE");
        editorWindow.setVisible(true);

        editorWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        editorWindow.setJMenuBar(createMenuBar());

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(20);
        JPanel contentPane = new JPanel(borderLayout);
        contentPane.setBorder(BorderFactory.createBevelBorder(0, Color.WHITE, Color.WHITE));

        contentPane.add(codeAreaScroll, BorderLayout.CENTER);
        contentPane.add(outputAreaScroll, BorderLayout.SOUTH);

        editorWindow.setContentPane(contentPane);
        editorWindow.pack();
        // Centers application on screen
        editorWindow.setLocationRelativeTo(null);
        editorWindow.setPreferredSize(new Dimension(800, 500));
    }

    public void showPopupLabel(String text, int x, int y) {
        JLabel popupLabel = new JLabel(text);

        popupLabel.setPreferredSize(new Dimension(200, 25));
        PopupFactory popupFactory = PopupFactory.getSharedInstance();
        Popup popup = popupFactory.getPopup(this, popupLabel, x, y);

        popupLabel.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                popup.hide();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // do nothing
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // do nothing
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // do nothing
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // do nothing
            }
        });

        popup.show();
    }

    private JTextArea createCodeArea() {
        JTextArea codeArea = new JTextArea(10, 50);
        codeArea.setEditable(true);
        codeArea.setBorder(BorderFactory.createLoweredBevelBorder());
        codeArea.setFont(new Font("Consolas", Font.PLAIN, 16));

        Document document = codeArea.getDocument();
        document.addDocumentListener(new CodeChangedListener(this, codeParser, syntaxAnalyzer, syntaxTreeCache, framingIfFinder));

        return codeArea;
    }

    private JTextArea createOutputArea() {
        JTextArea outputArea = new JTextArea(5, 50);
        OutputAreaWriter.setOutputArea(outputArea);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLoweredBevelBorder());
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 16));

        return outputArea;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu());
        menuBar.add(runMenu());

        return menuBar;
    }

    private JMenu fileMenu() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setPreferredSize(new Dimension(40, 20));

        JMenuItem exit = new JMenuItem("Exit");
        exit.setPreferredSize(new Dimension(100, 20));
        exit.setEnabled(true);
        exit.addActionListener(e -> System.exit(0));

        fileMenu.add(exit);
        return fileMenu;
    }

    private JMenu runMenu() {
        JMenu runMenu = new JMenu("Run");
        runMenu.setPreferredSize(new Dimension(40, 20));
        runMenu.addMenuListener(new RunMenuListener(codeArea.getDocument(), codeParser, syntaxAnalyzer, programExecutionVisitor));

        return runMenu;
    }

    public JTextArea getCodeArea() {
        return codeArea;
    }
}