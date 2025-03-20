package test;

import classes.Room;
import dataBaseSQL.RoomSQL;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class RoomSQLTest {
    @Test
    public void testCRUDRoom() {
        List<Room> rooms = RoomSQL.GetRooms();
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());
        Room lastRoomInBDD = rooms.get(rooms.size()-1);

        Room roomToAdd = new Room((lastRoomInBDD.getId()+1), "TestRoom", 100, "TestColor");
        SQLException addException = RoomSQL.AddRoom(roomToAdd);
        assertNull(addException);

        rooms = RoomSQL.GetRooms();
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());

        Room lastRoom = rooms.get(rooms.size() - 1);

        lastRoom.setName("UpdatedRoomName");
        lastRoom.setCapacity(150);
        lastRoom.setColor("UpdatedColor");
        SQLException updateException = RoomSQL.UpdateRoom(lastRoom);
        assertNull(updateException);

        rooms = RoomSQL.GetRooms();
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());
        Room updatedRoom = rooms.get(rooms.size() - 1);
        assertEquals("UpdatedRoomName", updatedRoom.getName());
        assertEquals(150, updatedRoom.getCapacity());
        assertEquals("UpdatedColor", updatedRoom.getColor());

        SQLException deleteException = RoomSQL.DeleteRoom(updatedRoom);
        assertNull(deleteException);

        rooms = RoomSQL.GetRooms();
        assertNotNull(rooms);
        assertFalse(rooms.isEmpty());
        assertFalse(rooms.contains(updatedRoom));
    }
}