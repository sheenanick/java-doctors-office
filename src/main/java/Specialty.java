import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;

public class Specialty {
  private String type;
  private int id;

  public Specialty(String type){
    this.type = type;
  }

  public String getType() {
    return type;
  }

  public int getId() {
    return id;
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO specialties (type) VALUES (:type)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("type", this.type)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Specialty> all() {
    String sql = "SELECT type FROM specialties";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Specialty.class);
    }
  }
  @Override
  public boolean equals(Object otherSpecialty) {
    if (!(otherSpecialty instanceof Specialty)) {
      return false;
    } else {
      Specialty newSpecialty = (Specialty) otherSpecialty;
      return this.getType().equals(newSpecialty.getType()) &&
      this.getId() == newSpecialty.getId();
    }
  }

  public static Specialty find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM specialties where id=:id";
      Specialty specialty = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Specialty.class);
      return specialty;
    }
  }
  public List<Doctors> getDoctors() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM doctors where specialtyId = :id";
      return con.createQuery(sql)
      .addParameter("id", this.id)
      .executeAndFetch(Doctors.class);
    }
  }

  public void setType(String type) {
    this.type = type;
  }

  public void update() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE specialties SET type = :type WHERE id = :id";
      con.createQuery(sql)
        .addParameter("type", this.type)
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
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM doctors WHERE specialtyId = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM specialties WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }
}
