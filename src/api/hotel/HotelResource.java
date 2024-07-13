package api.hotel;

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

  public HotelResource() {
    this.customerService = CustomerService.getInstance();
    this.reservationService = ReservationService.getInstance();
  }

  public Customer getCustomer(String email) {
    return customerService.getCustomer(email);
  }

  public void createCustomer(String email, String firstName, String lastName) {
    var allCustomers = customerService.getAllCustomers();
    for (var c : allCustomers) {
      if (c.getEmail().equals(email)) {
        throw new IllegalArgumentException("Email already exists");
      }
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

  public Collection<Reservation> getCustomerReservations(Customer customer){
    return reservationService.getCustomerReservations(customer);
  }

  public Collection<IRoom> findAvailableRooms(Date checkInDate, Date checkOutDate) {
    return reservationService.findRooms(checkInDate, checkOutDate);
  }

  public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date checkOutDate) {
    var customer = getCustomer(customerEmail);
    reservationService.reserveARoom(customer, room, checkInDate, checkOutDate);
    return new Reservation(customer, room, checkInDate, checkOutDate);
  }
}
