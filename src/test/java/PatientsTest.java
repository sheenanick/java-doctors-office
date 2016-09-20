import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;

public class PatientsTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctors_office_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM patients *;";
      String deleteDoctorsQuery = "DELETE FROM doctors *;";
      con.createQuery(sql).executeUpdate();
      con.createQuery(deleteDoctorsQuery).executeUpdate();
    }
  }

  @Test
  public void patients_instantiatesCorrectly_true() {
    Date birthdate = Date.valueOf("1991-11-05");
    Patients testPatient = new Patients("Joanna", birthdate, 1);
    assertEquals(true, testPatient instanceof Patients);
  }

  @Test
  public void patients_instantiatesWithName_String(){
    Date birthdate = Date.valueOf("1991-11-05");
    Patients testPatient = new Patients("Joanna", birthdate, 1);
    assertEquals("Joanna", testPatient.getName());
  }

  @Test
  public void patients_instantiatesWithBirthdate_birthdate(){
    Date birthdate = Date.valueOf("1991-11-05");
    Patients testPatient = new Patients("Joanna", birthdate, 1);
    assertEquals(birthdate, testPatient.getBirthdate());
  }

  @Test
  public void patients_save() {
    Date birthdate = Date.valueOf("1991-11-05");
    Patients testPatient = new Patients("Joanna", birthdate, 1);
    testPatient.save();
    assertTrue(Patients.all().get(0).equals(testPatient));
  }

  @Test
  public void patients_all_true() {
    Date birthdate1 = Date.valueOf("1991-11-05");
    Patients testPatient = new Patients("Joanna", birthdate1, 1);
    testPatient.save();
    Date birthdate2 = Date.valueOf("1989-11-05");
    Patients testPatient2 = new Patients("Sheena", birthdate2, 1);
    testPatient2.save();
    assertEquals(true, Patients.all().get(0).equals(testPatient));
    assertEquals(true, Patients.all().get(1).equals(testPatient2));
  }

  @Test
  public void patients_find_Patients() {
    Date birthdate1 = Date.valueOf("1991-11-05");
    Patients testPatient = new Patients("Joanna", birthdate1, 1);
    testPatient.save();
    Date birthdate2 = Date.valueOf("1989-11-05");
    Patients testPatient2 = new Patients("Sheena", birthdate2, 1);
    testPatient2.save();
    assertEquals(Patients.find(testPatient2.getId()), testPatient2);
  }

  @Test
  public void patients_setName_String() {
    Date birthdate1 = Date.valueOf("1991-11-05");
    Patients testPatient = new Patients("Joanna", birthdate1, 1);
    testPatient.save();
    testPatient.setName("Sheena");
    testPatient.update();
    assertEquals("Sheena", testPatient.getName());
  }
  @Test
  public void patients_setBirthdate_Date() {
    Date birthdate1 = Date.valueOf("1991-11-05");
    Patients testPatient = new Patients("Joanna", birthdate1, 1);
    testPatient.save();
    testPatient.setBirthdate(Date.valueOf("1989-11-05"));
    testPatient.update();
    assertEquals(Date.valueOf("1989-11-05"), testPatient.getBirthdate());
  }

}
