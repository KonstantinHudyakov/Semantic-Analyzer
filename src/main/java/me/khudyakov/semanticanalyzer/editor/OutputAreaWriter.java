package me.khudyakov.semanticanalyzer.editor;

import javax.swing.*;

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
}
