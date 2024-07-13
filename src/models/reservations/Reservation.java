package models.reservations;

import models.customers.Customer;
import models.rooms.IRoom;
import utils.DateUtils;

import java.util.Date;

public class Reservation {
  private final Customer customer;
  private final IRoom room;
  private final Date checkInDate;
  private final Date checkOutDate;

  public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
    this.customer = customer;
    this.room = room;
    this.checkInDate = checkInDate;
    this.checkOutDate = checkOutDate;
  }

  public Customer getCustomer() {
    return customer;
  }

  public IRoom getRoom() {
    return room;
  }

  public Date getCheckInDate() {
    return checkInDate;
  }

  public Date getCheckOutDate() {
    return checkOutDate;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Reservation r) {
      return room.equals(r.getRoom())
          && customer.equals(r.getCustomer())
          && checkInDate.equals(r.getCheckInDate())
          && checkOutDate.equals(r.getCheckOutDate());
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("""
        %s,
        %s,
        Check in date: %s,
        Check out date: %s
        """, room, customer, DateUtils.toString(checkInDate), DateUtils.toString(checkOutDate));
  }
}
