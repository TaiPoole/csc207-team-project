package interfaceadapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * View model for search message results.
 */
public class SearchMessageViewModel {
    public static final String RESULTS_PROPERTY = "results";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private List<String> results = new ArrayList<>();

    /**
     * Updates the search results and notifies listeners.
     *
     * @param newResults the new result list
     */
    public void setResults(List<String> newResults) {
        this.results = new ArrayList<>(newResults);
        support.firePropertyChange(RESULTS_PROPERTY, null, this.results);
    }

    /**
     * Returns the current results.
     *
     * @return the results list
     */
    public List<String> getResults() {
        return results;
    }

    /**
     * Adds a listener for updates to this view model.
     *
     * @param listener the listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
