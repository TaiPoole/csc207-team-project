package permissions;

import common.Permission;
import common.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

class ManagePermissionsInteractorTest {

    private ManagePermissionsInteractor interactor;
    private TestPresenter presenter;
    private TestGateway gateway;

    @BeforeEach
    void setUp() {
        presenter = new TestPresenter();
        gateway = new TestGateway();
        interactor = new ManagePermissionsInteractor(gateway, presenter);
    }

    @Test
    @DisplayName("Should prepare success view when permission request is sent successfully")
    void testExecuteSuccess() {
        String currentUser = "admin";
        String username = "testUser";
        String permissionName = "JOIN";
        ManagePermissionsInputData inputData = new ManagePermissionsInputData(
                currentUser, username, permissionName);
        gateway.setShouldSucceed(true);

        interactor.execute(inputData);

        assertTrue(presenter.isSuccessCalled());
        assertFalse(presenter.isFailCalled());
        assertEquals("Added Permission" + username, presenter.getSuccessMessage());
        assertTrue(presenter.getOutputData().isSuccess());
        assertEquals(currentUser, gateway.getLastCurrentUser());
        assertEquals(username, gateway.getLastUser().getUsername());
        assertEquals(Permission.JOIN, gateway.getLastPermission());
    }

    @Test
    @DisplayName("Should prepare fail view when gateway fails to send request")
    void testExecuteGatewayFailure() {
        String currentUser = "admin";
        String username = "testUser";
        String permissionName = "WRITE";
        ManagePermissionsInputData inputData = new ManagePermissionsInputData(
                currentUser, username, permissionName);
        gateway.setShouldSucceed(false);

        interactor.execute(inputData);

        assertTrue(presenter.isFailCalled());
        assertFalse(presenter.isSuccessCalled());
        assertEquals("Failed to send permission request to server.", presenter.getFailMessage());
    }

    @Test
    @DisplayName("Should prepare fail view for invalid permission type")
    void testExecuteInvalidPermission() {
        String currentUser = "admin";
        String username = "testUser";
        String permissionName = "INVALID_PERMISSION";
        ManagePermissionsInputData inputData = new ManagePermissionsInputData(
                currentUser, username, permissionName);

        interactor.execute(inputData);

        assertTrue(presenter.isFailCalled());
        assertFalse(presenter.isSuccessCalled());
        assertEquals("Invalid Permission Type: INVALID_PERMISSION", presenter.getFailMessage());
    }

    @Test
    @DisplayName("Should handle various valid permission types")
    void testExecuteWithDifferentPermissions() {
        // Test WRITE permission
        ManagePermissionsInputData writeInput = new ManagePermissionsInputData(
                "admin", "user1", "WRITE");
        gateway.setShouldSucceed(true);

        interactor.execute(writeInput);

        assertTrue(presenter.isSuccessCalled());
        assertEquals(Permission.WRITE, gateway.getLastPermission());
    }

    @Test
    @DisplayName("Should handle exception from gateway")
    void testExecuteGatewayException() {
        TestGatewayWithException faultyGateway = new TestGatewayWithException();
        ManagePermissionsInteractor faultyInteractor = new ManagePermissionsInteractor(
                faultyGateway, presenter);
        ManagePermissionsInputData inputData = new ManagePermissionsInputData(
                "admin", "testUser", "READ");

        faultyInteractor.execute(inputData);
        assertTrue(presenter.isFailCalled());
    }

    private static class TestPresenter implements ManagePermissionsOutputBoundary {
        private boolean successCalled = false;
        private boolean failCalled = false;
        private ManagePermissionsOutputData outputData;
        private String failMessage;

        @Override
        public void prepareSuccessView(ManagePermissionsOutputData data) {
            this.successCalled = true;
            this.outputData = data;
        }

        @Override
        public void prepareFailView(String error) {
            this.failCalled = true;
            this.failMessage = error;
        }

        public boolean isSuccessCalled() {
            return successCalled;
        }

        public boolean isFailCalled() {
            return failCalled;
        }

        public String getSuccessMessage() {
            return outputData != null ? outputData.getMessage() : null;
        }

        public ManagePermissionsOutputData getOutputData() {
            return outputData;
        }

        public String getFailMessage() {
            return failMessage;
        }
    }

    // Test double for ServerPermissionsGateway
    private static class TestGateway extends ServerPermissionsGateway {
        private boolean shouldSucceed = true;
        private String lastCurrentUser;
        private User lastUser;
        private Permission lastPermission;

        public TestGateway() {
            super(null);
        }

        @Override
        public boolean requestPermissionChange(String currentUser, User user, Permission permission) {
            this.lastCurrentUser = currentUser;
            this.lastUser = user;
            this.lastPermission = permission;
            return shouldSucceed;
        }

        public void setShouldSucceed(boolean shouldSucceed) {
            this.shouldSucceed = shouldSucceed;
        }

        public String getLastCurrentUser() {
            return lastCurrentUser;
        }

        public User getLastUser() {
            return lastUser;
        }

        public Permission getLastPermission() {
            return lastPermission;
        }
    }

    // double that just throws
    private static class TestGatewayWithException extends ServerPermissionsGateway {
        public TestGatewayWithException() {
            super(null);
        }

        @Override
        public boolean requestPermissionChange(String currentUser, User user, Permission permission) {
            throw new RuntimeException("Gateway error");
        }
    }
}