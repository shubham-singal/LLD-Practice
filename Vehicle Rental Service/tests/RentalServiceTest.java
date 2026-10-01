import org.junit.Before;
import org.junit.Test;
import vehiclerentalservice.Booking;
import vehiclerentalservice.RentalService;
import vehiclerentalservice.VehicleType;
import vehiclerentalservice.exceptions.DuplicateBranchException;
import vehiclerentalservice.exceptions.VehicleNotAvailableException;
import vehiclerentalservice.rentalstrategy.LowestPriceStrategy;
import vehiclerentalservice.rentalstrategy.RentalStrategy;

import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

public class RentalServiceTest {

    private RentalService rentalService;

    @Before
    public void setUp() {
        RentalStrategy rentalStrategy = new LowestPriceStrategy();
        rentalService = new RentalService(rentalStrategy);
    }

    @Test
    public void testAddBranchAndBookCar() {
        rentalService.addBranch("Vasanth Vihar");
        rentalService.addVehicle("DL 101", VehicleType.Sedan, "Vasanth Vihar");
        rentalService.allocatePrice("Vasanth Vihar", VehicleType.Sedan, 50.00);
        Booking booking = rentalService.bookVehicle(VehicleType.Sedan, LocalDateTime.of(2026, Month.OCTOBER, 21, 15, 0, 0), LocalDateTime.of(2026, Month.OCTOBER, 21, 19, 0, 0));

        assertEquals(200, booking.getTotalPrice(), 0.001);
        assertEquals("DL 101", booking.getVehicle().getLicenseNum());
        assertEquals(VehicleType.Sedan, booking.getVehicle().getVehicleType());
    }

    @Test(expected = VehicleNotAvailableException.class)
    public void testVehicleNotAvailableException() {
        rentalService.addBranch("Vasanth Vihar");
        rentalService.addVehicle("DL 101", VehicleType.Sedan, "Vasanth Vihar");
        rentalService.allocatePrice("Vasanth Vihar", VehicleType.Sedan, 50.00);
        Booking booking = rentalService.bookVehicle(VehicleType.Sedan, LocalDateTime.of(2026, Month.OCTOBER, 21, 15, 0, 0), LocalDateTime.of(2026, Month.OCTOBER, 21, 19, 0, 0));

        rentalService.bookVehicle(VehicleType.Sedan, LocalDateTime.of(2026, Month.OCTOBER, 21, 18, 0, 0), LocalDateTime.of(2026, Month.OCTOBER, 21, 22, 0, 0));
    }

    @Test
    public void testVehicleAvailableDifferentTime() {
        rentalService.addBranch("Vasanth Vihar");
        rentalService.addVehicle("DL 101", VehicleType.Sedan, "Vasanth Vihar");
        rentalService.allocatePrice("Vasanth Vihar", VehicleType.Sedan, 50.00);
        Booking booking = rentalService.bookVehicle(VehicleType.Sedan, LocalDateTime.of(2026, Month.OCTOBER, 21, 15, 0, 0), LocalDateTime.of(2026, Month.OCTOBER, 21, 19, 0, 0));

        Booking booking2 = rentalService.bookVehicle(VehicleType.Sedan, LocalDateTime.of(2026, Month.OCTOBER, 21, 10, 0, 0), LocalDateTime.of(2026, Month.OCTOBER, 21, 15   , 0, 0));
    }

    @Test(expected = DuplicateBranchException.class)
    public void testDuplicateBranchException() {
        rentalService.addBranch("Vasanth Vihar");
        rentalService.addBranch("Vasanth Vihar");
    }


}