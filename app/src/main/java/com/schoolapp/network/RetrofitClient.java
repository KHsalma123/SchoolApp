package com.schoolapp.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    // ── REMPLACE PAR TON URL MOCKAPI (étape A du guide) ─────────────────────
    // Exemple : "https://6701a2b3c4d5e6f7.mockapi.io/"
    // ⚠️ Garde le "/" à la fin, sinon les appels échouent.
    private static final String BASE_URL = "https://6a31d9eb7bc5e1c612664560.mockapi.io/";

    private static RetrofitClient instance;
    private final ApiService apiService;
    private final ScheduleApiService scheduleApiService;

    private RetrofitClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
        scheduleApiService = retrofit.create(ScheduleApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) instance = new RetrofitClient();
        return instance;
    }

    public ApiService getApi() { return apiService; }

    // Nouveau : service dédié à l'emploi du temps (MockAPI, démo Retrofit)
    public ScheduleApiService getScheduleApi() { return scheduleApiService; }
}