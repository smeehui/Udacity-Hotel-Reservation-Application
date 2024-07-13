package utils;

import java.util.Scanner;

public class InputUtils {
  private static final String INPUT_TRY_AGAIN_MESSAGE = "Invalid input, please try again:";

  public static Integer inputInt(String title, Integer min, Integer max) {
    var scanner = new Scanner(System.in);
    while (true) {
      System.out.println(title);
      System.out.print(">>: ");
      var input = scanner.nextLine();
      try {
        var i = Integer.parseInt(input);
        if ((min != null && i < min) || (max != null && i > max)) {
          System.out.println(INPUT_TRY_AGAIN_MESSAGE);
          continue;
        }
        return i;
      } catch (NumberFormatException e) {
        System.out.println(INPUT_TRY_AGAIN_MESSAGE);
      }
    }
  }

  public static Double inputDouble(String title) {
    System.out.println(title);
    System.out.print(">>: ");
    var scanner = new Scanner(System.in);
    while (true) {
      var input = scanner.nextLine();
      try {
        return Double.parseDouble(input);
      } catch (NumberFormatException e) {
        System.out.println(INPUT_TRY_AGAIN_MESSAGE);
      }
      System.out.print(">>: ");
    }
  }

  public static String inputStr(String title) {
    var scanner = new Scanner(System.in);
    while (true) {
      System.out.println(title);
      System.out.print(">>: ");
      var input = scanner.nextLine();
      if (input.trim().isEmpty()) {
        System.out.println(INPUT_TRY_AGAIN_MESSAGE);
        continue;
      }
      return input;
    }
  }

  public static Boolean inputContinue(String title) {
    var scanner = new Scanner(System.in);
    while (true) {
      System.out.println(title);
      System.out.println("(yes/y to continue, no/n to skip and keep changes, cancel/c to cancel operation)");
      System.out.print(">>: ");
      var input = scanner.nextLine().toLowerCase();
      if (input.trim().isEmpty()) {
        System.out.println(INPUT_TRY_AGAIN_MESSAGE);
        continue;
      }
      switch (input) {
        case "yes", "y" -> {
          return true;
        }
        case "no", "n" -> {
          return false;
        }
        case "c", "cancel" -> {
          return null;
        }
      }
      System.out.println(INPUT_TRY_AGAIN_MESSAGE);
    }
  }
}
