package utils;

public class MenuRenderer {
  public static void renderListMenu(String title, String... options) {
    System.out.println(title);
    System.out.println("--------------------------------");
    var count = 1;
    for (var opt : options) {
      System.out.printf("%d. %s%n", count++, opt);
    }
    System.out.println("--------------------------------");
  }
}
