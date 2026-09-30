package vehiclerentalservice.exceptions;

public class VehicleNotAvailableException extends RuntimeException{
    public VehicleNotAvailableException(String vehicle) {
        super(vehicle + " currently not available");
    }
}
