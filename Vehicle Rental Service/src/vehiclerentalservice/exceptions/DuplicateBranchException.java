package vehiclerentalservice.exceptions;

public class DuplicateBranchException extends RuntimeException {
    public DuplicateBranchException(String branchName) {
        super("Branch " + branchName + " already exists");
    }
}
