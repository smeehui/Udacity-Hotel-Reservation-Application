package api.admin;

import models.customers.Customer;
import models.rooms.IRoom;
import services.CustomerService;
import services.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {
  private final CustomerService customerService;
  private final ReservationService reservationService;

  public AdminResource() {
    this.customerService = CustomerService.getInstance();
    this.reservationService = ReservationService.getInstance();
  }

  public Customer getCustomer(String email) {
    return customerService.getCustomer(email);
  }

  public void addRoom(List<IRoom> room) {
    for (var r : room) {
      try {
        reservationService.addRoom(r);
        System.out.println("Added room " + r.getRoomNumber());
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public Collection<IRoom> getAllRooms() {
    return reservationService.getRoomData();
  }

  public Collection<Customer> getAllCustomers() {
    return customerService.getAllCustomers();
  }

  public void displayAllReservation() {
    reservationService.printAllReservations();
  }
}
