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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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

    @Test
    public void testConcurrentBookingSameVehicle() throws InterruptedException {
        rentalService.addBranch("Vasanth Vihar");
        rentalService.addVehicle(
                "DL 101",
                VehicleType.Sedan,
                "Vasanth Vihar"
        );
        rentalService.allocatePrice(
                "Vasanth Vihar",
                VehicleType.Sedan,
                50.00
        );

        LocalDateTime startTime =
                LocalDateTime.of(2026, Month.OCTOBER, 21, 15, 0);

        LocalDateTime endTime =
                LocalDateTime.of(2026, Month.OCTOBER, 21, 19, 0);

        ExecutorService executor = Executors.newFixedThreadPool(2);

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        AtomicInteger successfulBookings = new AtomicInteger();
        AtomicInteger failedBookings = new AtomicInteger();

        Runnable bookingTask = () -> {
            try {
                ready.countDown();

                start.await();

                rentalService.bookVehicle(
                        VehicleType.Sedan,
                        startTime,
                        endTime
                );

                successfulBookings.incrementAndGet();

            } catch (VehicleNotAvailableException e) {
                failedBookings.incrementAndGet();

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        executor.submit(bookingTask);
        executor.submit(bookingTask);

        // Wait until both threads are ready
        ready.await();

        // Release both threads at roughly the same time
        start.countDown();

        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(1, successfulBookings.get());
        assertEquals(1, failedBookings.get());
    }


}