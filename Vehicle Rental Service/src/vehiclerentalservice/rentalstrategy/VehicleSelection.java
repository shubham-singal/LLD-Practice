package vehiclerentalservice.rentalstrategy;

import vehiclerentalservice.Branch;
import vehiclerentalservice.Vehicle;

public record VehicleSelection(
        Branch branch,
        Vehicle vehicle,
        double hourlyPrice) {
}
