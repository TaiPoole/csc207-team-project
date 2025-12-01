package permissions;

import common.Permission;
import common.User;

/** Interactor for changing permissions.
 *  implements input boundary conditions
 */
public class ManagePermissionsInteractor implements ManagePermissionsInputBoundary {
    private final ManagePermissionsOutputBoundary presenter;
    private final ServerPermissionsGateway gateway;

    /** Basic Constructor.
     *
     * @param gateway server gateway to try changing the perms
     * @param presenter view for updating after gateway attempt returns
     *
     */
    public ManagePermissionsInteractor(ServerPermissionsGateway gateway,
                                       ManagePermissionsOutputBoundary presenter) {
        this.gateway = gateway;
        this.presenter = presenter;
    }

    @Override
    public void execute(ManagePermissionsInputData inputData) {
        try {
            Permission perm = Permission.valueOf(inputData.getPermissionName());
            User user = new User(inputData.getUsername());

            // Send request to server via gateway
            boolean sent = gateway.requestPermissionChange(inputData.getCurrentUser(), user, perm, inputData.channel);

            if (sent) {
                presenter.prepareSuccessView(new ManagePermissionsOutputData(
                        "Added Permission" + user.getUsername(), true));
            } else {
                presenter.prepareFailView("Failed to send permission request to server.");
            }

        } catch (IllegalArgumentException e) {
            presenter.prepareFailView("Invalid Permission Type: " + inputData.getPermissionName());
        }
    }
}