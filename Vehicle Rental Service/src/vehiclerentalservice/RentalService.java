package vehiclerentalservice;

import vehiclerentalservice.exceptions.BranchNotFoundException;
import vehiclerentalservice.exceptions.DuplicateBranchException;
import vehiclerentalservice.rentalstrategy.RentalStrategy;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RentalService {
    private final Map<String, Branch> branches;
    private RentalStrategy strategy;
    private final Set<String> licenseIds;

    public RentalService(RentalStrategy strategy) {
        if(strategy == null) throw new IllegalArgumentException("Strategy cannot be null");

        this.branches = new HashMap<>();
        this.strategy = strategy;
        this.licenseIds = new HashSet<>();
    }

    public void addBranch(String branchName) {
        if(branchName == null || branchName.isBlank()) {
            throw new IllegalArgumentException("Please provide a branch name");
        }

        branchName = branchName.trim().toLowerCase();
        if(branches.containsKey(branchName)) {
            throw new DuplicateBranchException(branchName);
        }
        Branch br = new Branch();
        branches.put(branchName, br);
    }


    public void addVehicle(String vehicleId, VehicleType vehicleType, String branchName) {

        if(branchName == null || branchName.isBlank()) {
            throw new IllegalArgumentException("Please provide a branch name");
        }

        if(vehicleId == null || vehicleId.isBlank()) {
            throw new IllegalArgumentException("Please provide valid vehicle details");
        }

        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }

        vehicleId = vehicleId.trim();

        if (licenseIds.contains(vehicleId)) {
            throw new IllegalArgumentException("Vehicle with given id already exists");
        }

        branchName = branchName.trim().toLowerCase();

        Branch branch = branches.get(branchName);

        if (branch == null) {
            throw new BranchNotFoundException(branchName);
        }

        branch.addVehicle(new Vehicle(vehicleId, vehicleType));
        licenseIds.add(vehicleId);
    }

    public void allocatePrice(String branchName, VehicleType vehicleType, double price) {
        if(branchName == null || branchName.isBlank()) {
            throw new IllegalArgumentException("Please provide a branch name");
        }

        branchName = branchName.trim().toLowerCase();
        Branch branch = branches.get(branchName);

        if(branch == null) {
            throw new BranchNotFoundException(branchName);
        }

        branches.get(branchName).setPrice(vehicleType, price);
    }

    public Booking bookVehicle(VehicleType vehicleType, LocalDateTime startTime, LocalDateTime endTime) {
        if (vehicleType == null) {
            throw new IllegalArgumentException("Vehicle type cannot be null");
        }

        if (startTime == null || endTime == null) {
            throw new IllegalArgumentException("Start and end time cannot be null");
        }

        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException(
                    "Start time must be before end time"
            );
        }
        return strategy.findVehicle(branches, vehicleType, startTime, endTime);
    }

    public void setRentalStrategy(RentalStrategy strategy) {
        if(strategy == null) throw new IllegalArgumentException("Strategy cannot be null");

        this.strategy = strategy;
    }

}
