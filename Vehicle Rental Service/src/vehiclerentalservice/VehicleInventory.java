package vehiclerentalservice;

import java.util.List;

public record VehicleInventory(
        List<Vehicle> available,
        List<Vehicle> unavailable
) {
}
