package interface_adapter;

/**
 * ViewModel for the Generate Random Name use case.
 * NOTE: For such a simple use case we could let the controller return the
 * generated name directly to the view, but I keep a dedicated ViewModel
 * to follow the View <-> ViewModel separation used in Clean Architecture.
 */
public class RandomNameViewModel {

    private String latestName;

    public String getLatestName() {
        return latestName;
    }

    public void setLatestName(String latestName) {
        this.latestName = latestName;
    }
}