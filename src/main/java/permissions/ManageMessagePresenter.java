package permissions;

import view.PermissionsView;

public class ManageMessagePresenter implements ManagePermissionsOutputBoundary {
    private final PermissionsView view;

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
