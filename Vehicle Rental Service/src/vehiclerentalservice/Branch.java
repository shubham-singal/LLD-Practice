package vehiclerentalservice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch {

    static int count = 0;

    private final int branchId;
    private String branchName;
    private List<Vehicle> fleet;
    private final Map<String, List<Booking>> bookingsByVehicle;
    private List<Booking> bookings;
    private final Map<VehicleType, Double> price;

    public Branch(String branchName) {
        this.branchId = ++Branch.count;
        this.branchName = branchName;
        fleet = new ArrayList<>();
        bookingsByVehicle = new HashMap<>();
        price = new HashMap<>();
    }

    public Double getPrice(VehicleType vehicleType) {
        if(price.get(vehicleType) == null) {
            throw new IllegalArgumentException("Vehicle Type doesn't exist");
        }
        return price.get(vehicleType);
    }


    public void setPrice(VehicleType vehicleType, double price) {
        this.price.put(vehicleType, price);
    }

    public Vehicle findAvailableVehicle(
            VehicleType vehicleType,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        for (Vehicle vehicle : fleet) {

            if (vehicle.getVehicleType() != vehicleType) {
                continue;
            }

            if (isVehicleAvailable(vehicle, startTime, endTime)) {
                return vehicle;
            }
        }

        return null;
    }

    private boolean isVehicleAvailable(
            Vehicle vehicle,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        List<Booking> vehicleBookings =
                bookingsByVehicle.getOrDefault(
                        vehicle.getLicenseNum(),
                        List.of()
                );

        for (Booking booking : vehicleBookings) {

            boolean overlaps =
                    startTime.isBefore(booking.getEndTime())
                            && endTime.isAfter(booking.getStartTime());

            if (overlaps) {
                return false;
            }
        }

        return true;
    }

    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    public int getBranchId() {
        return branchId;
    }
}
