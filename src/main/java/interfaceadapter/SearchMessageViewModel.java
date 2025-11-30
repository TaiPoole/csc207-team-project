package interfaceadapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class SearchMessageViewModel {
    public static final String RESULTS_PROPERTY = "results";

    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private List<String> results = new ArrayList<>();

    public void setResults(List<String> newResults) {
        this.results = new ArrayList<>(newResults);
        support.firePropertyChange(RESULTS_PROPERTY, null, this.results);
    }

    public List<String> getResults() {
        return results;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
