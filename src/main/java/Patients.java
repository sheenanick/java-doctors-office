import org.sql2o.*;
import java.sql.Date;
import java.util.List;
import java.util.ArrayList;

public class Patients {
  private String name;
  private Date birthdate;
  private int id;
  private int doctorsId;

  public Patients(String name, Date birthdate, int doctorsId){
  this.name = name;
  this.birthdate = birthdate;
  this.doctorsId = doctorsId;
  }

  public String getName() {
    return name;
  }
  public Date getBirthdate() {
    return birthdate;
  }

  public int getId(){
    return id;
  }

  public int getDoctorsId(){
    return doctorsId;
  }

  public static List<Patients> all() {
    String sql = "SELECT id, name, birthdate, doctorsId FROM patients";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Patients.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO patients(name, birthdate, doctorsId) VALUES (:name, :birthdate, :doctorsId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("birthdate", this.birthdate)
        .addParameter("doctorsId", this.doctorsId)
        .executeUpdate()
        .getKey();
    }
  }

  @Override
  public boolean equals(Object otherPatient) {
    if(!(otherPatient instanceof Patients)) {
      return false;
    } else {
      Patients newPatient = (Patients) otherPatient;
      return this.getName().equals(newPatient.getName()) && this.getId() == newPatient.getId() && this.getDoctorsId() == newPatient.getDoctorsId();
    }
  }

  public static Patients find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM patients where id=:id";
      Patients patient = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Patients.class);
      return patient;
    }
  }
  public void setName(String name) {
    this.name = name;
  }

  public void setBirthdate(Date birthdate) {
    this.birthdate = birthdate;
  }

  public void update() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE patients SET name = :name, birthdate = :birthdate WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("name", this.name)
      .addParameter("birthdate", this.birthdate)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }
  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM patients WHERE id = :id;";
      con.createQuery(sql)
      .addParameter("id", this.id)
      .executeUpdate();
    }
  }

  public void setDoctor(int id) {
    this.doctorsId = id;
  }
}
