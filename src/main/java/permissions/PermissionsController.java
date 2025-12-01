package permissions;

import view.PermissionsView;

public class PermissionsController {
    private final PermissionsView view;
    private final ManagePermissionsInputBoundary interactor;

    public PermissionsController(PermissionsView view, ManagePermissionsInputBoundary interactor) {
        this.view = view;
        this.interactor = interactor;

        // Hook up the grant button
        view.addGrantButtonListener(e -> handleGrantPermission());

    }

    private void handleGrantPermission() {
        String currentUser = "user";
        String username = view.getUsername();
        String permission = view.getSelectedPermission();

        if (username.isEmpty()) {
            view.showError("Username cannot be empty");
            return;
        }

        ManagePermissionsInputData inputData = new ManagePermissionsInputData(currentUser, username, permission, view.getChannel());
        interactor.execute(inputData);
    }

    public void show() {
        view.setVisible(true);
    }

    // Implement SetPermissionsOutputBoundary
    public void prepareSuccessView(ManagePermissionsOutputData data) {
        view.showSuccess(data.getMessage());
        view.clearUsername();
    }

    public void prepareFailView(String error) {
        view.showError(error);
    }
}