import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Doctors {
  private String name;
  private String specialty;
  private int id;

  public Doctors(String name, String specialty){
    this.name = name;
    this.specialty = specialty;
  }

  public String getName() {
    return name;
  }

  public String getSpecialty() {
    return specialty;
  }

  public int getId() {
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO doctors (name, specialty) VALUES (:name, :specialty)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("specialty", this.specialty)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Doctors> all() {
    String sql = "SELECT id, name, specialty FROM doctors";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Doctors.class);
    }
  }

  @Override
  public boolean equals(Object otherDoctor) {
    if (!(otherDoctor instanceof Doctors)) {
      return false;
    } else {
      Doctors newDoctor = (Doctors) otherDoctor;
      return this.getName().equals(newDoctor.getName()) &&
      this.getId() == newDoctor.getId();
    }
  }

  public static Doctors find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors where id=:id";
      Doctors doctor = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Doctors.class);
      return doctor;
    }
  }

  public List<Patients> getPatients() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients where doctorsId=:id";
      return con.createQuery(sql)
        .addParameter("id", this.id)
        .executeAndFetch(Patients.class);
    }
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public void update() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE doctors SET name = :name, specialty = :specialty WHERE id = :id;";
      con.createQuery(sql)
        .addParameter("name", this.name)
        .addParameter("specialty", this.specialty)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM patients WHERE doctorId = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM doctors WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }
}
