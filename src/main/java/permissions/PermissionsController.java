package permissions;

import view.PermissionsView;

/** Controller for permissions.
 *  holds the interactor and view for if a perm needs to be changed
 */
public class PermissionsController {
    private final PermissionsView view;
    private final ManagePermissionsInputBoundary interactor;

    /** Basic constructor.
     *  adds handleGrantPermission as an event to the view (described below)
     *
     * @param view view to attach
     * @param interactor interactor to attach
     */
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

    /** toggles the visibility of the show permissions pop up.
     *
     */
    public void show() {
        view.setVisible(true);
    }

    /** Calls the views success display method.
     *
     * @param data message to be sent indicating success
     */
    public void prepareSuccessView(ManagePermissionsOutputData data) {
        view.showSuccess(data.getMessage());
        view.clearUsername();
    }
}