package vehiclerentalservice;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Branch {

    private static int count = 0;

    private final int branchId;
    private final List<Vehicle>  fleet;
    private final Map<String, List<Booking>> bookingsByVehicle;
    private final Map<VehicleType, Double> prices;

    public Branch() {
        this.branchId = ++Branch.count;
        fleet = new ArrayList<>();
        bookingsByVehicle = new HashMap<>();
        prices = new HashMap<>();
    }

    public Double getPrice(VehicleType vehicleType) {
        if(prices.get(vehicleType) == null) {
            throw new IllegalArgumentException("Vehicle Type doesn't exist");
        }
        return prices.get(vehicleType);
    }


    public void setPrice(VehicleType vehicleType, double price) {
        if(vehicleType == null || price <= 0) {
            throw new IllegalArgumentException("Please enter valid vehicle type/price");
        }
        prices.put(vehicleType, price);
    }

    public void addVehicle(Vehicle vehicle) {
        if(vehicle == null || vehicle.getLicenseNum() == null || vehicle.getLicenseNum().isBlank()) {
            throw new IllegalArgumentException("Please provide valid Vehicle");
        }

        fleet.add(vehicle);
    }

    public Vehicle findAvailableVehicle(VehicleType vehicleType, LocalDateTime startTime, LocalDateTime endTime) {

        for (Vehicle vehicle : fleet) {
            if (vehicle.getVehicleType() == vehicleType
                    && isVehicleAvailable(vehicle, startTime, endTime)) {
                return vehicle;
            }
        }

        return null;
    }

    private boolean isVehicleAvailable(Vehicle vehicle, LocalDateTime startTime, LocalDateTime endTime) {

        List<Booking> vehicleBookings = bookingsByVehicle.getOrDefault(vehicle.getLicenseNum(), List.of());

        for (Booking booking : vehicleBookings) {
            if(startTime.isBefore(booking.getEndTime()) && endTime.isAfter(booking.getStartTime())) { //check if overlaps
                return false;
            }
        }

        return true;
    }

    public Map<VehicleType, VehicleInventory> getInventory(LocalDateTime startTime, LocalDateTime endTime) {

        Map<VehicleType, VehicleInventory> vehicleInventory = new HashMap<>();
        for(Vehicle vehicle : fleet) {
            VehicleType vt = vehicle.getVehicleType();

            if(!vehicleInventory.containsKey(vt)) {
                vehicleInventory.put(vt, new VehicleInventory(new ArrayList<>(), new ArrayList<>()));
            }
            if(isVehicleAvailable(vehicle, startTime, endTime)) {
                vehicleInventory.get(vt).available().add(vehicle);
            } else {
                vehicleInventory.get(vt).unavailable().add(vehicle);
            }
        }

        return  vehicleInventory;
    }

    public void addBooking(Booking booking) {
        String vehicleId = booking.getVehicle().getLicenseNum();

        bookingsByVehicle
                .computeIfAbsent(vehicleId, key -> new ArrayList<>())
                .add(booking);
    }

    public boolean hasPrice(VehicleType vehicleType) {
        return prices.containsKey(vehicleType);
    }

    public int getBranchId() {
        return branchId;
    }
}
