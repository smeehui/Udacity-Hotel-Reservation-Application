package models.rooms;

import models.rooms.enums.RoomType;

public class FreeRoom extends Room {

  public FreeRoom(String roomNumber, RoomType roomType) {
    super(roomNumber, 0.0, roomType);
  }

  @Override
  public String toString() {
    return String.format("Room information: Room number: %s; Room type:  %s; Price: Free", roomNumber, roomType);
  }
}
