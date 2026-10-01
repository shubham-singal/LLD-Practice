package vehiclerentalservice;

import java.time.LocalDateTime;

public class Booking {
    private static int count = 0;

    private final int bookingId;
    private final int branchId;
    private final Vehicle vehicle;
    private double totalPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    //Discount discount

    public Booking(int branchId, Vehicle vehicle, double totalPrice, LocalDateTime startTime, LocalDateTime endTime) {

        this.bookingId = ++count;
        this.branchId = branchId;
        this.vehicle = vehicle;
        this.totalPrice = totalPrice;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public int getBookingId() {
        return bookingId;
    }

    public int getBranchId() {
        return branchId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

}
