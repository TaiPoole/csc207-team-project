package permissions;

/** Input interface for the managing of permissions.
 *  Requires the ability to "execute", which initiates changing perms
 */
public interface ManagePermissionsInputBoundary {
    /** Execute method.
     *  a method to initiate changing perms based on the input data
     *
     * @param managePermissionsInputData data for perm changing
     */
    void execute(ManagePermissionsInputData managePermissionsInputData);
}