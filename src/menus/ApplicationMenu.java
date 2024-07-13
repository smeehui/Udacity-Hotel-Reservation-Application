package menus;

import api.admin.HotelResource;
import models.customers.Customer;
import models.rooms.IRoom;
import utils.DateUtils;
import utils.InputUtils;
import utils.MenuRenderer;

import java.util.Collection;
import java.util.Date;

import static utils.MenuRenderer.*;

public class ApplicationMenu {
  private static final HotelResource hotelResource = new HotelResource();

  public static void show() {
    while (true) {
      var options = new String[]{"Find and reserve a room", "See my reservations", "Create an Account", "Admin", "Exit"};
      renderListMenu("Welcome to the Hotel Reservation Application", options);
      var selected = InputUtils.inputInt("Please select a number from menu options:", 1, options.length);
      switch (selected) {
        case 1:
          findRoomAndReserve();
          break;
        case 2:
          findMyReservation();
          break;
        case 3:
          registerNewCustomer(null);
          break;
        case 4: {
          AdminMenu.show();
          break;
        }
        case 5: {
          System.out.println("Goodbye!");
          System.exit(1);
        }
      }
    }
  }

  private static void findMyReservation() {
    renderTitle("my reservation");
    var isContinue = true;

    while (isContinue) {
      var email = InputUtils.inputStr("Please enter the email you used to reserve a room");
      var customer = hotelResource.getCustomer(email);
      if (customer != null) {
        System.out.printf("Hello %s, here is your reservation detail:%n", customer.getFullName());
        var reservations = hotelResource.getCustomerReservations(customer);
        if (reservations.isEmpty()) {
          System.out.println("No reservations found");
        }
        renderList(reservations);
        return;
      }
      isContinue = InputUtils.inputConfirm("Customer with email: %s is not found".formatted(email));
    }

  }

  private static void findRoomAndReserve() {
    renderTitle("new reservation");
    var isContinue = true;
    var email = (String) null;
    var customer = (Customer) null;

    while (isContinue) {
      if (email == null) {
        email = InputUtils.inputStr("Please enter your email first");
      }
      if (customer == null) {
        customer = hotelResource.getCustomer(email);
      }
      if (customer == null) {
        var isConfirmed = InputUtils.inputConfirm("Customer with email is not found, do you want to register?");
        if (isConfirmed) {
          customer = registerNewCustomer(email);
        }
      }
      if (customer == null) {
        return;
      }
      System.out.printf("Hi %s:%n", customer.getFullName());
      var checkInDate = InputUtils.inputDate("Please enter your check in date");
      var today = DateUtils.fromLocalDate(DateUtils.toLocalDate(new Date()));
      if (checkInDate.before(today)) {
        System.out.println("Please enter date equals or is after current date");
        continue;
      }

      var checkOutDate = InputUtils.inputDate("Please enter your check out date");
      if (checkOutDate.before(checkInDate)) {
        System.out.println("Check out date must be after check in date");
        isContinue = InputUtils.inputConfirm("Do you want to continue reservation?");
        continue;
      }

      var availableRooms = hotelResource.findAvailableRooms(checkInDate, checkOutDate);
      if (availableRooms.isEmpty()) {
        System.out.printf("No available rooms found from %s to %s%n", DateUtils.toString(checkInDate), DateUtils.toString(checkOutDate));
        checkInDate = DateUtils.plusDays(checkInDate, 7);
        checkOutDate = DateUtils.plusDays(checkOutDate, 7);
        availableRooms = hotelResource.findAvailableRooms(
            checkInDate,
            checkOutDate

        );
        if (!availableRooms.isEmpty()) {
          System.out.println("Below are our suggestions for you in 7 days from your input");
        }
      }
      if (!availableRooms.isEmpty()) {
        reserveARoom(customer, checkInDate, checkOutDate, availableRooms);
      }
      isContinue = false;
    }

  }

  private static void reserveARoom(
      Customer customer,
      Date checkInDate,
      Date checkOutDate,
      Collection<IRoom> availableRooms
  ) {
    var isContinue = true;
    MenuRenderer.renderTitle("List available room from %s to %s".formatted(
        DateUtils.toString(checkInDate),
        DateUtils.toString(checkOutDate)
    ));
    while (isContinue) {
      MenuRenderer.renderList(availableRooms);
      var selectedRoom = (IRoom) null;
      var roomNumber = InputUtils.inputStr("Please enter room number to reserve");
      for (var r : availableRooms) {
        if (r.getRoomNumber().equals(roomNumber)) {
          selectedRoom = r;
          break;
        }
      }
      if (selectedRoom == null) {
        isContinue = InputUtils.inputConfirm("Room is not listed, do you want to select others?");
        continue;
      }

      if (checkInDate == null) {
        checkInDate = InputUtils.inputDate("Please enter your check in date");
      }
      var isCheckInDateConfirmed =
          InputUtils.inputConfirm("Is %s the checkin date you want?".formatted(DateUtils.toString(checkInDate)));
      if (!isCheckInDateConfirmed) {
        checkInDate = null;
        continue;
      }

      if (checkOutDate == null) {
        checkOutDate = InputUtils.inputDate("Please enter your check out date");
      }
      var isCheckOutDateConfirmed =
          InputUtils.inputConfirm("Is %s the checkout date you want?".formatted(DateUtils.toString(checkOutDate)));
      if (!isCheckOutDateConfirmed) {
        checkOutDate = null;
        continue;
      }

      if (checkOutDate.before(checkInDate)) {
        System.out.println("Check out date must be after check in date");
        checkInDate = null;
        checkOutDate = null;
        isContinue = InputUtils.inputConfirm("Do you want to continue reservation?");
        continue;
      }

      if (hotelResource.findAvailableRooms(checkInDate, checkOutDate).isEmpty()) {
        System.out.printf(
            "No available rooms found from %s to %s%n%n",
            DateUtils.toString(checkInDate),
            DateUtils.toString(checkOutDate)
        );
        System.out.println("Please try again.");
        return;
      }

      var isReservationConfirmed =
          InputUtils.inputConfirm("""
              Are you sure want to reserve this room?:
              %s
              Checkin date: %s
              Checkout date: %s
              """.formatted(selectedRoom, DateUtils.toString(checkInDate), DateUtils.toString(checkOutDate)));
      if (isReservationConfirmed) {
        var reservation = hotelResource.bookARoom(customer.getEmail(), selectedRoom, checkInDate, checkOutDate);
        System.out.printf("Reservation succeed%n");
        System.out.println("Reservation details:");
        System.out.println(reservation);
      }
      return;
    }
  }

  private static Customer registerNewCustomer(String email) {
    renderTitle("customer registration");
    var isContinue = true;
    while (isContinue) {
      var firstName = InputUtils.inputStr("Please enter first name");
      var lastName = InputUtils.inputStr("Please enter last name");
      try {
        if (email != null) {
          var isEmailConfirmed = InputUtils.inputConfirm("Is this your email?: %s".formatted(email));
          if (isEmailConfirmed) {
            hotelResource.createCustomer(email, firstName, lastName);
            System.out.println("Registration successful");
            return new Customer(email, firstName, lastName);
          }
        }
        email = InputUtils.inputStr("Please enter your email");
        hotelResource.createCustomer(email, firstName, lastName);
        System.out.println("Registration successful");
        return new Customer(email, firstName, lastName);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
        email = null;
        isContinue = InputUtils.inputConfirm("Do you want to continue registration?");
      }
    }
    return null;
  }
}
