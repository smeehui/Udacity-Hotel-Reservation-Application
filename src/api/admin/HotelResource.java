package api.admin;

import models.customers.Customer;
import models.reservations.Reservation;
import models.rooms.IRoom;
import services.CustomerService;
import services.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
  private final CustomerService customerService;
  private final ReservationService reservationService;

  public HotelResource(CustomerService customerService, ReservationService reservationService) {
    this.customerService = customerService;
    this.reservationService = reservationService;
  }

  public Customer getCustomer(String email) {
    return customerService.getCustomer(email);
  }

  public void createCustomer(String email, String firstName, String lastName) {
    var allCustomers = customerService.getAllCustomers();
    var created = new Customer(email, firstName, lastName);
    if (allCustomers.contains(created)) {
      throw new IllegalArgumentException("Customer's email already exists");
    }
    customerService.addCustomer(email, firstName, lastName);
  }

  public IRoom getRoom(String roomNumber) {
    for (var room : reservationService.getRoomData()) {
      if (room.getRoomNumber().equals(roomNumber)) {
        return room;
      }
    }
    return null;
  }

  public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
    var customer = getCustomer(customerEmail);
    if (customer == null) {
      throw new IllegalArgumentException("Customer not found!");
    }
    System.out.println("Finding available rooms...");
    Collection<IRoom> rooms = reservationService.findRooms(checkInDate, checkOutDate);
    return  null;
  }
}
