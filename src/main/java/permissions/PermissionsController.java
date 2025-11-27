package permissions;


import view.PermissionsView;

import java.awt.*;

public class PermissionsController {
    private final PermissionsView view;
    private final ManagePermissionsInputBoundary interactor;

    public PermissionsController(Frame parent, ManagePermissionsInputBoundary interactor) {
        this.view = new PermissionsView(parent);
        this.interactor = interactor;

        // Hook up the grant button
        view.addGrantButtonListener(e -> handleGrantPermission());

        view.setVisible(true);
    }

    private void handleGrantPermission() {
        String currentUser = "user";
        String username = view.getUsername();
        String permission = view.getSelectedPermission();

        if (username.isEmpty()) {
            view.showError("Username cannot be empty");
            return;
        }

        ManagePermissionsInputData inputData = new ManagePermissionsInputData(currentUser, username, permission);
        interactor.execute(inputData);
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