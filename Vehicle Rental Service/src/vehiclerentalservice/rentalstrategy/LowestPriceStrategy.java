package vehiclerentalservice.rentalstrategy;

import vehiclerentalservice.Booking;
import vehiclerentalservice.Branch;
import vehiclerentalservice.Vehicle;
import vehiclerentalservice.VehicleType;

import java.time.LocalDateTime;
import java.util.Map;

public class LowestPriceStrategy implements RentalStrategy {

    @Override
    public Booking findVehicle(
            Map<String, Branch> branches,
            VehicleType vehicleType,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        Branch cheapestBranch = null;
        Vehicle selectedVehicle = null;
        double cheapestPrice = Double.MAX_VALUE;

        for (Branch branch : branches.values()) {

            Vehicle vehicle = branch.findAvailableVehicle(
                    vehicleType,
                    startTime,
                    endTime
            );

            if (vehicle == null) {
                continue;
            }

            double price = branch.getPrice(vehicleType);

            if (price < cheapestPrice) {
                cheapestPrice = price;
                cheapestBranch = branch;
                selectedVehicle = vehicle;
            }
        }

        if (selectedVehicle == null) {
            throw new IllegalStateException(
                    "No vehicle available for requested time"
            );
        }

        Booking booking = new Booking(
                cheapestBranch.getBranchId(),
                selectedVehicle,
                cheapestPrice,
                startTime,
                endTime
        );

        cheapestBranch.addBooking(booking);

        return booking;
    }
}
