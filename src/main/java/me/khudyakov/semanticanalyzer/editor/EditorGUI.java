package me.khudyakov.semanticanalyzer.editor;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.Document;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditorGUI extends JFrame implements ActionListener {

    public static void main(String[] args) {
        new EditorGUI();
    }

    // Menus
    private JMenu fileMenu;
    private JMenu editMenu;
    private JMenu runMenu;
    private JMenuItem exit;
    private JMenuItem undoEdit, redoEdit, selectAll, copy, paste, cut;

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

    // Undo manager for managing the storage of the undos
    // so that the can be redone if requested
    private UndoManager undo;

    public EditorGUI() {
        super("JavaEdit");

        // Create Text Area
        createCodeArea();
        createOutputArea();

        // Create Menus
        fileMenu();
        editMenu();
        runMenu();

        // Create Undo Manager for managing undo/redo commands
        undoMan();

        // Create Window
        createEditorWindow();
    }

    private JFrame createEditorWindow() {
        editorWindow = new JFrame("Simple IDE");
        editorWindow.setVisible(true);
        //editorWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
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
        document.addDocumentListener(new CodeChangedListener(document));


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

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(runMenu);

        return menuBar;
    }

    private UndoManager undoMan() {
        // Listener for undo and redo functions to document
        undo = new UndoManager();
        codeArea.getDocument().addUndoableEditListener(e -> undo.addEdit(e.getEdit()));

        return undo;
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

    private void editMenu() {
        editMenu = new JMenu("Edit");
        editMenu.setPreferredSize(new Dimension(40, 20));

        // Add file menu items
        undoEdit = new JMenuItem("Undo");
        undoEdit.addActionListener(this);
        undoEdit.setPreferredSize(new Dimension(100, 20));
        undoEdit.setEnabled(true);

        redoEdit = new JMenuItem("Redo");
        redoEdit.addActionListener(this);
        redoEdit.setPreferredSize(new Dimension(100, 20));
        redoEdit.setEnabled(true);

        selectAll = new JMenuItem("Select All");
        selectAll.addActionListener(this);
        selectAll.setPreferredSize(new Dimension(100, 20));
        selectAll.setEnabled(true);

        copy = new JMenuItem("Copy");
        copy.addActionListener(this);
        copy.setPreferredSize(new Dimension(100, 20));
        copy.setEnabled(true);

        paste = new JMenuItem("Paste");
        paste.addActionListener(this);
        paste.setPreferredSize(new Dimension(100, 20));
        paste.setEnabled(true);

        cut = new JMenuItem("Cut");
        cut.addActionListener(this);
        cut.setPreferredSize(new Dimension(100, 20));
        cut.setEnabled(true);

        // Add items to menu
        editMenu.add(undoEdit);
        editMenu.add(redoEdit);
        editMenu.add(selectAll);
        editMenu.add(copy);
        editMenu.add(paste);
        editMenu.add(cut);
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
        } else if (event.getSource() == undoEdit) {
            try {
                undo.undo();
            } catch (CannotUndoException cu) {
                cu.printStackTrace();
            }
        } else if (event.getSource() == redoEdit) {
            try {
                undo.redo();
            } catch (CannotUndoException cur) {
                cur.printStackTrace();
            }
        } else if (event.getSource() == selectAll) {
            codeArea.selectAll();
        } else if (event.getSource() == copy) {
            codeArea.copy();
        } else if (event.getSource() == paste) {
            codeArea.paste();
        } else if (event.getSource() == cut) {
            codeArea.cut();
        }
    }

    public JTextArea getCodeArea() {
        return codeArea;
    }

    public void setCodeArea(JTextArea text) {
        codeArea = text;
    }
}