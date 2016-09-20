import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.sql.Date;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("doctors", Doctors.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors/:id/patients/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctors doctor = Doctors.find(Integer.parseInt(request.params(":id")));
      model.put("doctor", doctor);
      model.put("template", "templates/doctor-patients-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors/:doctorId/patients/:patientId", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctors doctor = Doctors.find(Integer.parseInt(request.params(":doctorId")));
      Patients patient = Patients.find(Integer.parseInt(request.params(":patientId")));
      model.put("doctor", doctor);
      model.put("patient", patient);
      model.put("template", "templates/patient-edit.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors/:doctorId/edit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctors doctor = Doctors.find(Integer.parseInt(request.params(":doctorId")));
      model.put("doctor", doctor);
      model.put("template", "templates/doctor-edit.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors/:id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Integer doctorId = Integer.parseInt(request.queryParams("doctorId"));
      Doctors doctor = Doctors.find(doctorId);
      doctor.delete();
      response.redirect("/doctors");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/doctorsdelete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Integer doctorId = Integer.parseInt(request.queryParams("doctorId"));
      Doctors doctor = Doctors.find(doctorId);
      doctor.delete();
      response.redirect("/doctors");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/doctoredit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Integer doctorId = Integer.parseInt(request.queryParams("doctorId"));
      Doctors doctor = Doctors.find(doctorId);
      doctor.setName(request.queryParams("name"));
      doctor.update();
      response.redirect("/doctors/" + doctorId);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patientedit", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int doctorId = Integer.parseInt(request.queryParams("doctorId"));
      Patients patient = Patients.find(Integer.parseInt(request.queryParams("patientId")));
      patient.setName(request.queryParams("patientName"));
      patient.setBirthdate(Date.valueOf(request.queryParams("patientBirthdate")));
      patient.setDoctor(doctorId);
      patient.update();
      response.redirect("/doctors/" + doctorId);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/doctor-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/doctors", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      String specialty = request.queryParams("specialty");
      Doctors newDoctor = new Doctors(name, specialty);
      newDoctor.save();
      response.redirect("/doctors");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("doctors", Doctors.all());
      model.put("template", "templates/doctors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/doctors/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Doctors doctor = Doctors.find(Integer.parseInt(request.params(":id")));
      model.put("doctor", doctor);
      model.put("template", "templates/doctor.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/patients", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      int doctorId = Integer.parseInt(request.queryParams("doctorId"));
      Doctors doctor = Doctors.find(doctorId);
      Date birthdate = Date.valueOf(request.queryParams("birthdate"));
      Patients patient = new Patients(request.queryParams("name"), birthdate, doctorId);
      patient.save();
      response.redirect("/doctors/" + doctorId);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
