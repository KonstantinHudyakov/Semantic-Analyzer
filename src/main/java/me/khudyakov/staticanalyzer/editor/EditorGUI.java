package me.khudyakov.staticanalyzer.editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EditorGUI extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new EditorGUI();
    }

    // Menus
    private JMenu fileMenu;
    private JMenu runMenu;
    private JMenuItem exit;

    // Window
    private JFrame editorWindow;
    private JPanel contentPane;
    private Border contentPaneBorder;

    // Text Area
    private JTextArea codeArea;
    private Border codeAreaBorder;
    private JScrollPane codeAreaScroll;
    private Font textFont;

    // Output Area
    private JTextArea outputArea;
    private Border outputAreaBorder;
    private JScrollPane outputAreaScroll;

    public EditorGUI() {
        super("JavaEdit");

        // Create Text Area
        createCodeArea();
        createOutputArea();

        // Create Menus
        fileMenu();
        runMenu();

        // Create Window
        createEditorWindow();
    }

    private JFrame createEditorWindow() {
        editorWindow = new JFrame("Simple IDE");
        editorWindow.setVisible(true);

        editorWindow.setDefaultCloseOperation(EXIT_ON_CLOSE);
        // Create Menu Bar
        editorWindow.setJMenuBar(createMenuBar());

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setVgap(20);
        contentPane = new JPanel(borderLayout);
        contentPaneBorder = BorderFactory.createBevelBorder(0, Color.WHITE, Color.WHITE);
        contentPane.setBorder(contentPaneBorder);

        contentPane.add(codeAreaScroll, BorderLayout.CENTER);
        contentPane.add(outputAreaScroll, BorderLayout.SOUTH);

        editorWindow.setContentPane(contentPane);
        editorWindow.pack();
        // Centers application on screen
        editorWindow.setLocationRelativeTo(null);
        editorWindow.setPreferredSize(new Dimension(800, 500));

        return editorWindow;
    }

    private JTextArea createCodeArea() {
        codeAreaBorder = BorderFactory.createBevelBorder(0, Color.WHITE, Color.WHITE);
        codeArea = new JTextArea(10, 50);
        codeArea.setEditable(true);
        codeArea.setBorder(BorderFactory.createLoweredBevelBorder());

        textFont = new Font("Consolas", 0, 16);
        codeArea.setFont(textFont);

        codeAreaScroll = new JScrollPane(codeArea);
        codeAreaScroll.setPreferredSize(new Dimension(800, 350));

        Document document = codeArea.getDocument();
        document.addDocumentListener(new CodeChangedListener(this));

        return codeArea;
    }

    private JTextArea createOutputArea() {
        outputAreaBorder = BorderFactory.createBevelBorder(0, Color.WHITE, Color.WHITE);
        outputArea = new JTextArea(5, 50);
        OutputAreaWriter.setOutputArea(outputArea);
        outputArea.setEditable(false);
        outputArea.setBorder(BorderFactory.createLoweredBevelBorder());

        outputArea.setFont(textFont);

        outputAreaScroll = new JScrollPane(outputArea);
        outputAreaScroll.setPreferredSize(new Dimension(800, 150));

        return outputArea;
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

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(runMenu);

        return menuBar;
    }

    private void fileMenu() {
        // Create File Menu
        fileMenu = new JMenu("File");
        fileMenu.setPreferredSize(new Dimension(40, 20));

        exit = new JMenuItem("Exit");
        exit.addActionListener(this);
        exit.setPreferredSize(new Dimension(100, 20));
        exit.setEnabled(true);

        fileMenu.add(exit);
    }

    private void runMenu() {
        runMenu = new JMenu("Run");
        runMenu.setPreferredSize(new Dimension(40, 20));
        runMenu.addMenuListener(new RunMenuListener(codeArea.getDocument()));
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == exit) {
            System.exit(0);
        }
    }

    public JTextArea getCodeArea() {
        return codeArea;
    }

    public void setCodeArea(JTextArea text) {
        codeArea = text;
    }
}