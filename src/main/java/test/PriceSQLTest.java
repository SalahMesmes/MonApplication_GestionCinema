package test;

import classes.Price;
import dataBaseSQL.PriceSQL;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

public class PriceSQLTest {
    @Test
    public void testCRUDPrice() {
        List<Price> prices = PriceSQL.GetPricing();
        assertNotNull(prices);
        assertFalse(prices.isEmpty());
        Price lastPriceInBDD = prices.get(prices.size()-1);

        Price priceToAdd = new Price((lastPriceInBDD.getId()+1), "TestPrice", 10.0f);
        SQLException addException = PriceSQL.AddPrice(priceToAdd);
        assertNull(addException);

        prices = PriceSQL.GetPricing();
        assertNotNull(prices);
        assertFalse(prices.isEmpty());

        Price lastPrice = prices.get(prices.size() - 1);

        lastPrice.setName("UpdatedPriceName");
        lastPrice.setCost(20.0f);
        SQLException updateException = PriceSQL.UpdatePrice(lastPrice);
        assertNull(updateException);

        prices = PriceSQL.GetPricing();
        assertNotNull(prices);
        assertFalse(prices.isEmpty());
        Price updatedPrice = prices.get(prices.size() - 1);
        assertEquals("UpdatedPriceName", updatedPrice.getName());
        assertEquals(20.0f, updatedPrice.getCost(), 0.001f);

        SQLException deleteException = PriceSQL.DeletePrice(updatedPrice);
        assertNull(deleteException);

        prices = PriceSQL.GetPricing();
        assertNotNull(prices);
        assertFalse(prices.isEmpty());
        assertFalse(prices.contains(updatedPrice));
    }
}