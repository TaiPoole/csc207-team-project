package permissions;

/** Interface for outputting a change in permissions.
 *  requires the ability to update the UI on success and fail.
 *
 */
public interface ManagePermissionsOutputBoundary {
    /** Update the UI on perm manage success.
     *
     * @param outputData required data for updating
     */
    void prepareSuccessView(ManagePermissionsOutputData outputData);

    /** Update the UI when a perm cant be changed.
     *
     * @param error String to display error message
     */
    void prepareFailView(String error);
}