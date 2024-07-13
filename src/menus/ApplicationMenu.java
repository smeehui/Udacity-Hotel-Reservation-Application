package menus;

import utils.InputUtils;
import utils.MenuRenderer;

public class ApplicationMenu {
  public static void show() {
    while (true) {
      var options = new String[]{"Find and reserve a room", "See my reservations", "Create an Account", "Admin", "Exit"};
      MenuRenderer.renderListMenu("Welcome to the Hotel Reservation Application", options);
      var selected = InputUtils.inputInt("Please select a number from menu options:", 1, options.length);
      switch (selected) {
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
}
