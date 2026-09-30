package vehiclerentalservice;

public class Vehicle {
    private String licenseNum;
    private VehicleType vehicleType;

    private int odometer;

    private int fuel;


    public String getLicenseNum() {
        return licenseNum;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
