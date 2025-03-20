package test;

import classes.Slot;
import dataBaseSQL.SlotSQL;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class SlotSQLTest {
    @Test
    public void testAddAndGetTodaySlots() {
        int roomId = 1;
        int movieId = 4;
        int startHour = 15;

        SQLException addException = SlotSQL.AddSlot(roomId, movieId, startHour);
        assertNull(addException);

        List<Slot> todaySlots = SlotSQL.GetTodaySlots();
        assertNotNull(todaySlots);
        assertFalse(todaySlots.isEmpty());
    }
}