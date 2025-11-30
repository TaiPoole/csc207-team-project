package gui;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Clears placeholder text when focused, restores if empty.
 */
public class SearchMessageFocusListener implements FocusListener {

    private final JTextField field;
    private final String placeholder;
    private boolean firstFocus = true;   // flag to resolve the initial focus issue for swing

    public SearchMessageFocusListener(JTextField field, String placeholder) {
        this.field = field;
        this.placeholder = placeholder;
        this.field.setText(placeholder);
    }

    @Override
    public void focusGained(FocusEvent e) {
        // Skip the very first automatic focus when the window opens
        if (firstFocus) {
            firstFocus = false;
            return;
        }

        if (field.getText().equals(placeholder)) {
            field.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (field.getText().isEmpty()) {
            field.setText(placeholder);
        }
    }
}
