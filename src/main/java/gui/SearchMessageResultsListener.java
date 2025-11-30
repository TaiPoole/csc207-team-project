package gui;

import interfaceadapter.SearchMessageViewModel;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class SearchMessageResultsListener implements PropertyChangeListener {

    private final JComponent parent;
    private final SearchMessageViewModel viewModel;

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