package vehiclerentalservice.rentalstrategy;

import vehiclerentalservice.Branch;
import vehiclerentalservice.Vehicle;
import vehiclerentalservice.VehicleType;
import vehiclerentalservice.exceptions.VehicleNotAvailableException;

import java.time.LocalDateTime;
import java.util.Map;

public class LowestPriceStrategy implements RentalStrategy {

    @Override
    public VehicleSelection selectVehicle(Map<String, Branch> branches, VehicleType vehicleType, LocalDateTime startTime, LocalDateTime endTime) {

        Branch cheapestBranch = null;
        Vehicle selectedVehicle = null;
        double cheapestPrice = Double.MAX_VALUE;

        for (Branch branch : branches.values()) {

            if (!branch.hasPrice(vehicleType)) {
                continue;
            }

            Vehicle vehicle = branch.findAvailableVehicle(vehicleType, startTime, endTime);

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
            throw new VehicleNotAvailableException(vehicleType.toString());
        }


        return new VehicleSelection(cheapestBranch, selectedVehicle, cheapestPrice);
    }
}
