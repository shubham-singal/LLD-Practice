package vehiclerentalservice.rentalstrategy;


import vehiclerentalservice.Branch;
import vehiclerentalservice.VehicleType;

import java.time.LocalDateTime;
import java.util.Map;

public interface RentalStrategy {

    VehicleSelection selectVehicle(Map<String, Branch> branches, VehicleType vehicleType, LocalDateTime startTime, LocalDateTime endTime);
}

