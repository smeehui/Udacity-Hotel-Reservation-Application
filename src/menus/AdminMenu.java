package menus;

import api.admin.AdminResource;
import constants.Constants;
import models.rooms.FreeRoom;
import models.rooms.IRoom;
import models.rooms.Room;
import models.rooms.enums.RoomType;
import utils.InputUtils;

import java.util.ArrayList;

import static utils.MenuRenderer.renderListMenu;
import static utils.MenuRenderer.renderTitle;

public class AdminMenu {
  private static final AdminResource adminResource = new AdminResource();

  public static void show() {
    while (true) {
      var options = new String[]{"See all Customers", "See all Rooms", "See all Reservations", "Add a Room", "Back to " +
          "main menu"};
      renderListMenu("Admin Menu", options);
      var selected = InputUtils.inputInt(Constants.DEFAULT_MENU_SELECTION_TITLE, 1, options.length);

      switch (selected) {
        case 1: {
          renderTitle("List of customers");
          var customers = adminResource.getAllCustomers();
          if (customers.isEmpty()) {
            System.out.println("No customers found");
            break;
          }
          var count = 0;
          for (var c : customers) {
            System.out.printf("%s. %s\n", ++count, c);
          }
        }
        break;
        case 2: {
          renderTitle("List of rooms");
          var rooms = adminResource.getAllRooms();
          if (rooms.isEmpty()) {
            System.out.println("No rooms found");
            break;
          }
          var count = 0;
          for (var r : rooms) {
            System.out.printf("%s. %s\n", ++count, r);
          }
        }
        break;
        case 3:
          renderTitle("List of reservations");
          adminResource.displayAllReservation();
          break;
        case 4:
          addNewRoom();
          break;
        case 5:
          return;
      }
    }
  }

  private static void addNewRoom() {
    renderTitle("add new room");
    var rooms = new ArrayList<IRoom>();
    var isContinue = true;
    while (isContinue) {
      var roomNumber = InputUtils.inputInt("Please enter room number: ",null,null).toString();
      if (rooms.stream().anyMatch(r -> r.getRoomNumber().equals(roomNumber))) {
        System.out.println("Room with this number has been already added!");
      } else {
        int roomType = InputUtils.inputInt("Please enter room type (1 - Single, 2 - Double)", 1, 2);
        var roomPrice = InputUtils.inputDouble("Please enter room price ($) (enter zero for free room)");
        RoomType parsed = RoomType.parse(roomType);
        rooms.add(roomPrice == 0.0 ? new FreeRoom(roomNumber, parsed) : new Room(roomNumber, roomPrice, parsed));
      }
      var continueInput = InputUtils.inputContinue("Continue to adding new room?");
      if (continueInput == null) {
        System.out.println("Cancelled adding new rooms");
        return;
      }
      isContinue = continueInput;
    }
    adminResource.addRoom(rooms);
  }
}
