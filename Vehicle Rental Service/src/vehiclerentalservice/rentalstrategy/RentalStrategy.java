package vehiclerentalservice.rentalstrategy;

import vehiclerentalservice.Booking;
import vehiclerentalservice.Branch;
import vehiclerentalservice.Vehicle;
import vehiclerentalservice.VehicleType;

import java.time.LocalDateTime;
import java.util.Map;

public interface RentalStrategy {

    VehicleSelection findVehicle(Map<String, Branch> branches, VehicleType vehicleType, LocalDateTime startTime, LocalDateTime endTime);
}

