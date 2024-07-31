package models.rooms;

import models.rooms.enums.RoomType;

import java.util.Objects;

public class Room implements IRoom {
  protected final String roomNumber;
  protected final Double price;
  protected final RoomType roomType;

  public Room(String roomNumber, Double price, RoomType roomType) {
    this.roomNumber = roomNumber;
    this.price = price;
    this.roomType = roomType;
  }

  @Override
  public String toString() {
    return String.format("Room information: Room number: %s; Room type:  %s; Price: $%s", roomNumber, roomType, price);
  }

  @Override
  public String getRoomNumber() {
    return roomNumber;
  }

  @Override
  public Double getPrice() {
    return price;
  }

  @Override
  public String getRoomType() {
    return roomType.toString();
  }

  @Override
  public Boolean isFree() {
    return 0.0 == price;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Room r) {
      return roomNumber.equals(r.getRoomNumber());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(roomNumber);
  }
}
