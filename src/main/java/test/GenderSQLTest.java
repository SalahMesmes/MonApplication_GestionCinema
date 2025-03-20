package test;

import classes.Gender;
import dataBaseSQL.GenderSQL;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;
import java.util.List;

public class GenderSQLTest {
    @Test
    public void testCreateAndGetGenders(){
        List<Gender> genders = GenderSQL.GetGenders();
        Assert.assertNotNull(genders);
        String genderName = "TestGender";
        SQLException exception = GenderSQL.AddGender(genderName);
        Assert.assertNull(exception);
        genders = GenderSQL.GetGenders();
        Assert.assertNotNull(genders);
        int numberOfGender = genders.size();
        Gender lastGenderAdd = genders.get(numberOfGender-1);
        Assert.assertEquals(lastGenderAdd.getName(), genderName);
    }
}