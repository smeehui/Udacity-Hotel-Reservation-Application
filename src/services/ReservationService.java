package services;

import models.customers.Customer;
import models.reservations.Reservation;
import models.rooms.IRoom;
import models.rooms.enums.RoomType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ReservationService {
  private static ReservationService INSTANCE;
  private final Collection<IRoom> roomData = new ArrayList<>();
  private final Collection<Reservation> reservationData = new ArrayList<>();

  public static ReservationService getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ReservationService();
    }
    return INSTANCE;
  }

  public void addRoom(IRoom room) {
    for (IRoom r : roomData) {
      if (r.getRoomNumber().equals(room.getRoomNumber())) {
        throw new IllegalArgumentException("Room with number %s already exists".formatted(r.getRoomNumber()));
      }
    }
    roomData.add(room);
  }

  public void reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
    reservationData.add(new Reservation(customer, room, checkInDate, checkOutDate));
  }

  public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {

    var bookedSingleRooms = reservationData
        .stream()
        .filter(r -> RoomType.SINGLE.toString().equals(r.getRoom().getRoomType()) && (checkInDate.after(r.getCheckInDate()) || checkOutDate.before(r.getCheckOutDate())))
        .map(Reservation::getRoom)
        .toList();

    if (bookedSingleRooms.isEmpty()) {
      return roomData;
    }

    return roomData.stream().filter(r -> !bookedSingleRooms.contains(r)).toList();
  }

  public Collection<Reservation> getCustomerReservations(Customer customer) {
    return reservationData.stream().filter(r -> r.getCustomer().equals(customer)).toList();
  }

  public Collection<IRoom> getRoomData() {
    return roomData;
  }

  public void printAllReservations() {
    for (Reservation reservation : reservationData) {
      System.out.println(reservation);
      System.out.println("-*-*-*-");
    }
  }
}
