package permissions;

import view.PermissionsView;

/** Presenter for the manage perms message dialogue.
 *
 */
public class ManageMessagePresenter implements ManagePermissionsOutputBoundary {
    private final PermissionsView view;

    /** Basic constructor.
     *
     * @param view the view for the presenter to call
     */
    public ManageMessagePresenter(PermissionsView view) {
        this.view = view;
    }

    @Override
    public void prepareSuccessView(ManagePermissionsOutputData outputData) {
        view.showSuccess(outputData.getMessage());
        view.clearUsername();
    }

    @Override
    public void prepareFailView(String error) {
        view.showError(error);
    }
}
