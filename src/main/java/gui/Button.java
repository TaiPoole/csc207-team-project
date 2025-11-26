package gui;

import javax.swing.Action;
import javax.swing.JButton;

// TODO: why does this exist? its field text isnt even used since our constructors use the text field from superclass?
/** Button class.
 *  a type of JButton (from JSwing)
 *  wraps text and action within it for easier calls
 *  each button will be a subclass and add any other methods/ functionality needed
 */
public class Button extends JButton {
    String text;

    // TODO: Cut if this isn't used
    /** Default constructor.
     *  inherited from JButton.
     */
    public Button() {
        super();
    }

    /** Constructor holding text.
     *
     * @param text text displayed with the button
     */
    public Button(String text) {
        super(text);
    }

    // TODO: Cut if this isn't used
    /** Constructor holding an action.
     *
     * @param action action to be performed on button click
     */
    public Button(Action action) {
        super(action);
    }
}
