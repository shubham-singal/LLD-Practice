package vehiclerentalservice.exceptions;

public class BranchNotFoundException extends RuntimeException {
    public BranchNotFoundException(String branchName) {
        super("Branch " + branchName + " not found");
    }
}
