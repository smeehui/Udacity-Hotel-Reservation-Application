package models.rooms.enums;

public enum RoomType {
  SINGLE(1), DOUBLE(2);

  RoomType(int value) {
    this.value = value;
  }

  private final int value;

  public int getValue() {
    return value;
  }

  public static RoomType parse(int number) {
    if (number == 1) {
      return SINGLE;
    }
    if (number == 2) {
      return DOUBLE;
    }
    throw new IllegalArgumentException("Invalid room type");
  }
}
