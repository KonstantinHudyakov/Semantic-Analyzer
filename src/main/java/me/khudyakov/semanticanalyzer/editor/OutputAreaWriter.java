package me.khudyakov.semanticanalyzer.editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class OutputAreaWriter {

    private static JTextArea outputArea;

    public static void setOutputArea(JTextArea outputArea) {
        OutputAreaWriter.outputArea = outputArea;
    }

    public static void setContent(String text) {
        if (outputArea != null) {
            outputArea.setText(text);
        }
    }

    public static void println(String text) {
        if(outputArea != null) {
            outputArea.append(text + "\n");
        }
    }

    public static void print(String text) {
        if(outputArea != null) {
            outputArea.append(text);
        }
    }

    public static void clear() {
        if(outputArea != null) {
            outputArea.setText("");
        }
    }

    public static String getText() {
        String text = "";
        if(outputArea != null) {
            Document document = outputArea.getDocument();
            try {
                text = document.getText(0, document.getLength());
            } catch (BadLocationException e) {
                // never happens
            }
        }
        return text;
    }
}
