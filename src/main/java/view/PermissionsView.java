package view;

import common.Permission;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/** Permissions view for handling the permissions popup.
 *
 */
public class PermissionsView extends JDialog {
    private final JComboBox<String> permissionComboBox;
    private final JTextField usernameField;
    private final JTextField channelField;
    private final JButton grantButton;
    private final JButton cancelButton;
    private final JLabel statusLabel;

    /** Constructor. For the view.
     *  Initializes it based on its JDialog parent, and then sets up the functions on the popup
     *
     * @param parent parent frame for easier initialization/latching
     */
    public PermissionsView(Frame parent) {
        super(parent, "Manage Permissions", true);

        // Initialize components
        this.permissionComboBox = new JComboBox<>(getPermissionNames());
        this.usernameField = new JTextField(20);
        this.channelField = new JTextField(20);
        this.grantButton = new JButton("Grant Permission");
        this.cancelButton = new JButton("Cancel");
        this.statusLabel = new JLabel(" ");

        layoutComponents();
        setupListeners();

        setSize(450, 250);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
    }

    private String[] getPermissionNames() {
        Permission[] permissions = Permission.values();
        String[] names = new String[permissions.length];
        for (int i = 0; i < permissions.length; i++) {
            names[i] = permissions[i].name();
        }
        return names;
    }

    private void layoutComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel usernameLabel = new JLabel("Target Username:");
        usernameLabel.setPreferredSize(new Dimension(130, 25));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);

        JPanel channelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel channelLabel = new JLabel("Channel:");
        channelLabel.setPreferredSize(new Dimension(130, 25));
        channelPanel.add(channelLabel);
        channelPanel.add(channelField);

        JPanel permissionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel permissionLabel = new JLabel("Permission Type:");
        permissionLabel.setPreferredSize(new Dimension(130, 25));
        permissionPanel.add(permissionLabel);
        permissionPanel.add(permissionComboBox);

        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusLabel.setForeground(Color.BLUE);
        statusPanel.add(statusLabel);

        // Add spacing and panels
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(usernamePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(channelPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(permissionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        mainPanel.add(statusPanel);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        buttonPanel.add(grantButton);
        buttonPanel.add(cancelButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        cancelButton.addActionListener(e -> dispose());
    }

    public String getSelectedPermission() {
        return (String) permissionComboBox.getSelectedItem();
    }

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getChannel() {
        return channelField.getText().trim();
    }

    /** Clears the username input field. */
    public void clearUsername() {
        usernameField.setText("");
    }

    /** Displays a success message.
     *
     * @param message the success message to display
     */
    public void showSuccess(String message) {
        statusLabel.setForeground(new Color(0, 128, 0));
        statusLabel.setText(message);
    }

    /** Displays an error message.
     *
     * @param error the error message to display
     */
    public void showError(String error) {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText(error);
    }

    /** Adds an action listener to the grant button.
     *
     * @param listener the action listener
     */
    public void addGrantButtonListener(ActionListener listener) {
        grantButton.addActionListener(listener);
    }
}