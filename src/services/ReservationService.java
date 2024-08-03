package services;

import models.customers.Customer;
import models.reservations.Reservation;
import models.rooms.IRoom;
import utils.DateUtils;
import utils.MenuRenderer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

public class ReservationService {
  private static ReservationService INSTANCE;
  private final Collection<IRoom> roomData = new HashSet<>();
  private final Collection<Reservation> reservationData = new HashSet<>();

  private ReservationService() {
  }

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
    if (reservationData.isEmpty()) {
      return roomData;
    }
//    [Old code]
//    try {
//      Find fully booked single rooms and fully booked double room asynchronously
//      var getFullyBookedSingleRoomsTask = CompletableFuture.supplyAsync(() -> findBookedSingleRoom(checkInDate, checkOutDate));
//      var getFullyBookedDoubleRoomsTask = CompletableFuture.supplyAsync(() -> findBookedDoubleRooms(checkInDate, checkOutDate));
//      CompletableFuture.allOf(getFullyBookedSingleRoomsTask, getFullyBookedDoubleRoomsTask).join();
//
//      var fullyBookedSingleRooms = getFullyBookedSingleRoomsTask.get();
//      var fullyBookedDoubleRooms = getFullyBookedDoubleRoomsTask.get();


////      Concat unavailable rooms
//      var unavailableRooms = new ArrayList<>(fullyBookedSingleRooms);
//      unavailableRooms.addAll(fullyBookedDoubleRooms);

//      var availableRooms = new ArrayList<IRoom>();
//
//      for (IRoom r : roomData) {
////      Filter out unavailable rooms
//        if (!unavailableRooms.contains(r)) {
//          availableRooms.add(r);
//        }
//      }
//      return availableRooms;
//    } catch (InterruptedException | ExecutionException e) {
//      System.out.println("An unexpected error happened, please try again later");
//      return List.of();
//    }
    var unavailableRooms = findBookedRoom(checkInDate, checkOutDate);
    var availableRooms = new ArrayList<IRoom>();

    for (IRoom r : roomData) {
//      Filter out unavailable rooms
      if (!unavailableRooms.contains(r)) {
        availableRooms.add(r);
      }
    }
    return availableRooms;
  }

//  private Collection<IRoom> findBookedDoubleRooms(Date checkInDate, Date checkOutDate) {
//    var bookedRooms = new ArrayList<IRoom>();
//    var grouping = new HashMap<IRoom, List<Reservation>>();
//    for (var r : reservationData) {
//      var isDoubleRoom = RoomType.DOUBLE.toString().equals(r.getRoom().getRoomType());
//      var isCheckInDateBooked = DateUtils.isDateBetween(r.getCheckInDate(), checkInDate, checkOutDate);
//      var isCheckOutDateBooked = DateUtils.isDateBetween(r.getCheckOutDate(), checkInDate, checkOutDate);
//      if (isDoubleRoom && (isCheckInDateBooked || isCheckOutDateBooked)) {
//        if (grouping.containsKey(r.getRoom())) {
//          grouping.get(r.getRoom()).add(r);
//        } else {
//          grouping.put(r.getRoom(), new ArrayList<>());
//          grouping.get(r.getRoom()).add(r);
//        }
//      }
//    }
//    for (var entry : grouping.entrySet()) {
//      if (entry.getValue().size() == 2) {
//        bookedRooms.add(entry.getKey());
//      }
//    }
//    return bookedRooms;
//  }

//  private Collection<IRoom> findBookedSingleRoom(Date checkInDate, Date checkOutDate) {
//    var bookedRooms = new ArrayList<IRoom>();
//    for (var reservation : reservationData) {
//      var isSingleRoom = RoomType.SINGLE.toString().equals(reservation.getRoom().getRoomType());
//      var isCheckInDateBooked = DateUtils.isDateBetween(reservation.getCheckInDate(), checkInDate, checkOutDate);
//      var isCheckOutDateBooked = DateUtils.isDateBetween(reservation.getCheckOutDate(), checkInDate, checkOutDate);
//
//      if (isSingleRoom && (isCheckInDateBooked || isCheckOutDateBooked)) {
//        bookedRooms.add(reservation.getRoom());
//      }
//    }
//    return bookedRooms;
//  }

  private Collection<IRoom> findBookedRoom(Date checkInDate, Date checkOutDate) {
    var bookedRooms = new ArrayList<IRoom>();
    for (var reservation : reservationData) {
      var isCheckInDateBooked = DateUtils.isDateBetween(reservation.getCheckInDate(), checkInDate, checkOutDate);
      var isCheckOutDateBooked = DateUtils.isDateBetween(reservation.getCheckOutDate(), checkInDate, checkOutDate);

      if ((isCheckInDateBooked || isCheckOutDateBooked)) {
        bookedRooms.add(reservation.getRoom());
      }
    }
    return bookedRooms;
  }

  public Collection<Reservation> getCustomerReservations(Customer customer) {
    return reservationData.stream().filter(r -> r.getCustomer().equals(customer)).toList();
  }

  public Collection<IRoom> getRoomData() {
    return roomData;
  }

  public void printAllReservations() {
    if (reservationData.isEmpty()) {
      System.out.println("No reservation found");
      return;
    }
    MenuRenderer.renderList(reservationData, "-*-*-*-");
  }

//  // Test method to validate finding room
//  public static void main(String[] args) {
//    var service = new ReservationService();
//    var room1 = new Room("1", 12.0, RoomType.SINGLE);
//    var room2 = new Room("2", 12.0, RoomType.DOUBLE);
//    var room3 = new FreeRoom("3", RoomType.SINGLE);
//    service.addRoom(room1);
//    service.addRoom(room2);
//    service.addRoom(room3);
//
//    var customer = new Customer("alo@co.cc", "F", "L");
//
//    var checkin = DateUtils.fromString("2022-12-12");
//    var checkout = DateUtils.fromString("2022-12-14");
//
//    service.reserveARoom(customer, room1, checkin, checkout);
//    service.reserveARoom(customer, room2, DateUtils.fromString("2022-12-13"), DateUtils.fromString("2022-12-15"));
//
//// should be room2 and room3
//    System.out.println(service.findRooms(checkin, DateUtils.fromString("2022-12-15")));
//
////    Reserve room2 -> room2 is booked 2 times
//    service.reserveARoom(customer, room2, DateUtils.fromString("2022-12-13"), DateUtils.fromString("2022-12-15"));
//
////  should be room3 only
//    System.out.println(service.findRooms(DateUtils.fromString("2022-12-13"), DateUtils.fromString("2022-12-15")));
//
////  should be room3 only
//    System.out.println(service.findRooms(DateUtils.fromString("2022-12-14"), DateUtils.fromString("2022-12-15")));
//
////    should be room1 and room 3
//    System.out.println(service.findRooms(DateUtils.fromString("2022-12-15"), DateUtils.fromString("2022-12-16")));
//
////    should be room3 only
//    System.out.println(service.findRooms(DateUtils.fromString("2022-12-11"), DateUtils.fromString("2022-12-16")));
//
////    should be all rooms
//    System.out.println(service.findRooms(DateUtils.fromString("2024-12-11"), DateUtils.fromString("2024-12-16")));
//  }
}
