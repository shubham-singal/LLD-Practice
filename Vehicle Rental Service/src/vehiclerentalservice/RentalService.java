package vehiclerentalservice;

import vehiclerentalservice.exceptions.BranchNotFoundException;
import vehiclerentalservice.exceptions.DuplicateBranchException;
import vehiclerentalservice.rentalstrategy.RentalStrategy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class RentalService {
    private Map<String, Branch> branches;
    private RentalStrategy strategy;

    public RentalService(RentalStrategy strategy) {
        if(strategy == null) throw new IllegalArgumentException("Strategy cannot be null");

        this.branches = new HashMap<>();
        this.strategy = strategy;
    }

    public void addBranch(String branchName) {
        if(branchName == null || branchName.isEmpty()) {
            throw new IllegalArgumentException("Please provide a branch name");
        }

        branchName = branchName.trim().toLowerCase();
        if(branches.containsKey(branchName)) {
            throw new DuplicateBranchException(branchName);
        }
        Branch br = new Branch(branchName);
        branches.put(branchName, br);
    }

    public void allocatePrice(String branchName, VehicleType vehicleType, double price) {
        if(branchName == null || branchName.isEmpty()) {
            throw new IllegalArgumentException("Please provide a branch name");
        }

        branchName = branchName.trim().toLowerCase();
        Branch branch = branches.get(branchName);

        if(branch == null) {
            throw new BranchNotFoundException(branchName);
        }

        branches.get(branchName).setPrice(vehicleType, price);
    }

    public void addVehicle(
            String vehicleId,
            VehicleType vehicleType,
            String branchName) {

        branchName = branchName.trim().toLowerCase();

        Branch branch = branches.get(branchName);

        if (branch == null) {
            throw new BranchNotFoundException(branchName);
        }

        branch.addVehicle(new Vehicle(vehicleId, vehicleType));
    }

    public Booking bookVehicle(VehicleType vehicleType, LocalDateTime startTime, LocalDateTime endTime) {
        return strategy.findVehicle(branches, vehicleType, startTime, endTime);
    }

    public void setRentalStrategy(RentalStrategy strategy) {
        if(strategy == null) throw new IllegalArgumentException("Strategy cannot be null");

        this.strategy = strategy;
    }
}
