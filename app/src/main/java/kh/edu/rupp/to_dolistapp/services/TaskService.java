package kh.edu.rupp.to_dolistapp.services;

import kh.edu.rupp.to_dolistapp.models.TaskResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface TaskService {
    @GET("kimleang288/MobileApp_API/refs/heads/main/tasks.json")
    Call<TaskResponse> getTasks();
}