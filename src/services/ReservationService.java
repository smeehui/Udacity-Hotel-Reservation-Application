package services;

import models.customers.Customer;
import models.reservations.Reservation;
import models.rooms.IRoom;
import models.rooms.enums.RoomType;
import utils.DateUtils;
import utils.MenuRenderer;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    if (reservationData.isEmpty()) {
      return roomData;
    }

    try {
//      Find fully booked single rooms and fully booked double room asynchronously
      var getFullyBookedSingleRoomsTask = findBookedSingleRoom(checkInDate, checkOutDate);
      var getFullyBookedDoubleRoomsTask = findBookedDoubleRooms(checkInDate, checkOutDate);
      CompletableFuture.allOf(getFullyBookedSingleRoomsTask, getFullyBookedDoubleRoomsTask).join();

      var fullyBookedSingleRooms = getFullyBookedSingleRoomsTask.get();
      var fullyBookedDoubleRooms = getFullyBookedDoubleRoomsTask.get();

//      Concat unavailable rooms
      var unavailableRooms = Stream.concat(fullyBookedSingleRooms.stream(), fullyBookedDoubleRooms.stream()).toList();
//      Filter out unavailable rooms
      return roomData.stream().filter(r -> !unavailableRooms.contains(r)).toList();
    } catch (InterruptedException | ExecutionException e) {
      System.out.println("An unexpected error happened, please try again later");
      return List.of();
    }
  }

  private CompletableFuture<List<IRoom>> findBookedDoubleRooms(Date checkInDate, Date checkOutDate) {
    return CompletableFuture.completedFuture(reservationData
        .stream()
        .filter(r -> {
          var isDoubleRoom = RoomType.DOUBLE.toString().equals(r.getRoom().getRoomType());
          var isCheckInDateBooked = DateUtils.isDateBetween(r.getCheckInDate(), checkInDate, checkOutDate);
          var isCheckOutDateBooked = DateUtils.isDateBetween(r.getCheckOutDate(), checkInDate, checkOutDate);
          return isDoubleRoom && (isCheckInDateBooked || isCheckOutDateBooked);
        })
//        Grouping by room to find booked times
        .collect(Collectors.groupingBy(
            Reservation::getRoom
        )).entrySet()
        .stream()
//        Only get the double rooms which had been booked two times
        .filter(e -> e.getValue().size() == 2)
        .map(Map.Entry::getKey)
        .toList());
  }

  private CompletableFuture<List<IRoom>> findBookedSingleRoom(Date checkInDate, Date checkOutDate) {
    return CompletableFuture.completedFuture(reservationData
        .stream()
        .filter(r -> {
          var isSingleRoom = RoomType.SINGLE.toString().equals(r.getRoom().getRoomType());
          var isCheckInDateBooked = DateUtils.isDateBetween(r.getCheckInDate(), checkInDate, checkOutDate);
          var isCheckOutDateBooked = DateUtils.isDateBetween(r.getCheckOutDate(), checkInDate, checkOutDate);
          return isSingleRoom && (isCheckInDateBooked || isCheckOutDateBooked);
        })
        .map(Reservation::getRoom)
        .toList());
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

//// Test method to validate finding room
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
//    System.out.println(service.findRooms(checkin, DateUtils.fromString("2022-12-15"))); // should be room2 and room3
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
