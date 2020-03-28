package me.khudyakov.semanticanalyzer.editor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class CodeAreaEditingListener implements DocumentListener {

    private int openBracesNum = 0;

    @Override
    public void insertUpdate(DocumentEvent e) {
        Document document = e.getDocument();
        String inserted = getInsertedText(document, e.getOffset(), e.getLength());
        for (int i = 0; i < inserted.length(); i++) {
            char ch = inserted.charAt(i);
            if (ch == '{') {
                openBracesNum++;
            } else if (ch == '}') {
                openBracesNum--;
            }
        }

        if ("\n".equals(inserted)) {
            String offset = createOffset();
            SwingUtilities.invokeLater(() -> {
                try {
                    document.insertString(e.getOffset() + 1, offset, null);
                } catch (BadLocationException ex) {
                    // do nothing
                }
            });
        } else if("}".equals(inserted)) {
            SwingUtilities.invokeLater(() -> {
                try {
                    String text = document.getText(e.getOffset() - 4, 4);
                    if("    ".equals(text)) {
                        document.remove(e.getOffset() - 4, 4);
                    }
                } catch (BadLocationException ex) {
                    // do nothing
                }
            });
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {

    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        // do nothing
    }

    private String createOffset() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < openBracesNum; i++) {
            builder.append("    ");
        }
        return builder.toString();
    }

    private String getInsertedText(Document document, int offset, int len) {
        String text = "";
        try {
            text = document.getText(offset, len);
        } catch (BadLocationException e) {
            // never happens
        }
        return text;
    }
}
