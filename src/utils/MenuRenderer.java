package utils;

import java.util.Arrays;
import java.util.Collection;

public class MenuRenderer {
  public static void renderListMenu(String title, String... options) {
    renderTitle(title);
    System.out.println("--------------------------------");
    renderList(Arrays.asList(options));
    System.out.println("--------------------------------");
  }

  public static void renderTitle(String title) {
    System.out.printf("---------%s---------%n", title.toUpperCase());
  }

  public static void renderList(Collection<?> list) {
    var count = 0;
    for (var o : list) {
      System.out.printf("%s. %s%n", ++count, o);
    }
  }

  public static void renderList(Collection<?> list, String br) {
    var count = 0;
    for (var o : list) {
      System.out.printf("%s. %s%n", ++count, o);
      System.out.println(br);
    }
    System.out.printf("Total: %s%n", count);
  }
}
