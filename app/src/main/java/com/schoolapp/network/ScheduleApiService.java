package com.schoolapp.network;

import com.schoolapp.models.ScheduleSlot;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface Retrofit dédiée à l'emploi du temps, branchée sur MockAPI.io.
 *
 * MockAPI génère automatiquement cet endpoint à partir du nom de la
 * ressource "schedule" créée dans le dashboard mockapi.io :
 *   GET https://TON_PROJECT_TOKEN.mockapi.io/schedule  →  ScheduleSlot[]
 *
 * On récupère TOUT l'emploi du temps en un seul appel, puis on filtre
 * côté Android (par élève et par jour) — plus simple que de configurer
 * des filtres serveur sur MockAPI, et suffisant pour la démo.
 */
public interface ScheduleApiService {

    @GET("schedule")
    Call<List<ScheduleSlot>> getAllSchedule();
}