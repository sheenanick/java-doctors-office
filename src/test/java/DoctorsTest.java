import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Date;

public class DoctorsTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/doctors_office_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deletePatientsQuery = "DELETE FROM patients *;";
      String deleteDoctorsQuery = "DELETE FROM doctors *;";
      con.createQuery(deletePatientsQuery).executeUpdate();
      con.createQuery(deleteDoctorsQuery).executeUpdate();
    }
  }

  @Test
  public void doctors_instantiatesCorrectly_true() {
    Doctors testDoctor = new Doctors("Joanna Anderson", "Neurology");
    assertEquals(true, testDoctor instanceof Doctors);
  }

  @Test
  public void doctors_getName_String() {
    Doctors testDoctor = new Doctors("Joanna Anderson", "Neurology");
    assertEquals("Joanna Anderson", testDoctor.getName());
  }

  @Test
  public void doctors_getSpecialty_String() {
    Doctors testDoctor = new Doctors("Joanna Anderson", "Neurology");
    assertEquals("Neurology", testDoctor.getSpecialty());
  }

  @Test
  public void doctors_getId_1() {
    Doctors testDoctor = new Doctors("Joanna Anderson", "Neurology");
    testDoctor.save();
    assertTrue(testDoctor.getId() > 0);
  }

  @Test
  public void doctors_getsAll_ArrayList() {
    Doctors testDoctor1 = new Doctors("Joanna Anderson", "Neurology");
    testDoctor1.save();
    Doctors testDoctor2 = new Doctors("Sheena Nickerson", "Neurology");
    testDoctor2.save();
    assertEquals(true, Doctors.all().get(0).equals(testDoctor1));
    assertEquals(true, Doctors.all().get(1).equals(testDoctor2));
  }

  @Test
  public void doctors_find_Doctor() {
    Doctors testDoctor1 = new Doctors("Joanna Anderson", "Neurology");
    testDoctor1.save();
    Doctors testDoctor2 = new Doctors("Sheena Nickerson", "Neurology");
    testDoctor2.save();
    assertEquals(Doctors.find(testDoctor2.getId()), testDoctor2);
  }

  @Test
  public void doctors_getPatients_ArrayList() {
    Doctors testDoctor1 = new Doctors("Joanna Anderson", "Neurology");
    testDoctor1.save();
    Date birthdate1 = Date.valueOf("1991-11-05");
    Patients testPatient = new Patients("Joanna", birthdate1, testDoctor1.getId());
    testPatient.save();
    Date birthdate2 = Date.valueOf("1989-11-05");
    Patients testPatient2 = new Patients("Sheena", birthdate2, testDoctor1.getId());
    testPatient2.save();
    Patients[] patients = new Patients[] { testPatient, testPatient2};
    assertTrue(testDoctor1.getPatients().containsAll(Arrays.asList(patients)));
  }

  @Test
  public void doctors_setName() {
    Doctors testDoctor1 = new Doctors("Joanna Anderson", "Neurology");
    testDoctor1.save();
    testDoctor1.setName("Sheena");
    testDoctor1.update();
    assertEquals("Sheena", testDoctor1.getName());
  }

  @Test
  public void doctors_setSpecialty() {
    Doctors testDoctor1 = new Doctors("Joanna Anderson", "Neurology");
    testDoctor1.save();
    testDoctor1.setSpecialty("Neuro");
    testDoctor1.update();
    assertEquals("Neuro", testDoctor1.getSpecialty());
  }
}
