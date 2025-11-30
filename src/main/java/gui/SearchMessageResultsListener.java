package gui;

import interfaceadapter.SearchMessageViewModel;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Listener that displays search results when the view model updates.
 */
public class SearchMessageResultsListener implements PropertyChangeListener {

    private final JComponent parent;
    private final SearchMessageViewModel viewModel;

    /**
     * Constructs the listener.
     *
     * @param parent the parent component for dialogs
     * @param viewModel the search view model
     */
    public SearchMessageResultsListener(JComponent parent,
                                        SearchMessageViewModel viewModel) {
        this.parent = parent;
        this.viewModel = viewModel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (!SearchMessageViewModel.RESULTS_PROPERTY.equals(evt.getPropertyName())) {
            return;
        }

        List<String> results = viewModel.getResults();
        String text;

        if (results == null || results.isEmpty()) {
            text = "No messages matched your search.";
        } else {
            text = String.join("\n", results);
        }

        JTextArea textArea = new JTextArea(text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 300));

        JOptionPane.showMessageDialog(
                parent,
                scrollPane,
                "Search results",
                JOptionPane.PLAIN_MESSAGE
        );
    }
}