package permissions;

public interface ManagePermissionsOutputBoundary {
    void prepareSuccessView(ManagePermissionsOutputData outputData);
    void prepareFailView(String error);
}