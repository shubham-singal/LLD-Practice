package vehiclerentalservice;

public class Vehicle {
    private String licenseNum;
    private VehicleType vehicleType;


    public Vehicle(String licenseNum, VehicleType vehicleType) {
        this.licenseNum = licenseNum;
        this.vehicleType = vehicleType;
    }

    public String getLicenseNum() {
        return licenseNum;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }
}
