package com.schoolapp.network;

import com.schoolapp.models.*;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {

    // Auth
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);

    // Students
    @GET("students")
    Call<List<Student>> getStudents(@Header("Authorization") String token);

    @GET("students/{id}")
    Call<Student> getStudent(@Header("Authorization") String token, @Path("id") int id);

    // Grades
    @GET("students/{id}/grades")
    Call<List<Grade>> getGrades(@Header("Authorization") String token, @Path("id") int studentId);

    // Schedule
    @GET("students/{id}/schedule")
    Call<List<ScheduleSlot>> getSchedule(@Header("Authorization") String token, @Path("id") int studentId);

    // Absences
    @GET("students/{id}/absences")
    Call<List<Absence>> getAbsences(@Header("Authorization") String token, @Path("id") int studentId);

    @PUT("absences/{id}/justify")
    Call<Absence> justifyAbsence(@Header("Authorization") String token,
                                 @Path("id") int absenceId,
                                 @Body JustifyRequest request);

    // Messages
    @GET("messages")
    Call<List<Message>> getMessages(@Header("Authorization") String token);

    @POST("messages")
    Call<Message> sendMessage(@Header("Authorization") String token, @Body Message message);

    // Teachers
    @GET("teachers")
    Call<List<Teacher>> getTeachers(@Header("Authorization") String token);

    // Field trips
    @GET("fieldtrips")
    Call<List<FieldTrip>> getFieldTrips(@Header("Authorization") String token);

    // Documents
    @GET("students/{id}/documents")
    Call<List<Document>> getDocuments(@Header("Authorization") String token, @Path("id") int studentId);

    // ─── Request/Response DTOs ─────────────────────────────────────────────────

    class LoginRequest {
        public String email;
        public String password;
        public String role;
        public LoginRequest(String email, String password, String role) {
            this.email = email; this.password = password; this.role = role;
        }
    }

    class LoginResponse {
        public String token;
        public int userId;
        public String name;
        public String role;
        public String email;
    }

    class JustifyRequest {
        public String reason;
        public JustifyRequest(String reason) { this.reason = reason; }
    }
}