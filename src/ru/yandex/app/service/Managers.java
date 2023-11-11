package ru.yandex.app.service;

import com.google.gson.*;
import ru.yandex.app.model.*;

public final class Managers {

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new HttpTaskManager();
    }

    public static Gson getDefaultGson() {
        return new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Epic.class, (JsonSerializer<Epic>) (epic, type, jsonSerializationContext) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Type", String.valueOf(TypeTask.EPIC));
                    jsonObject.addProperty("idTask", epic.getIdTask());
                    jsonObject.addProperty("nameTask", epic.getNameTask());
                    jsonObject.addProperty("descriptionTask", epic.getDescriptionTask());
                    jsonObject.addProperty("subtasksId", String.valueOf(epic.getSubtasksId()));
                    jsonObject.addProperty("statusTask", String.valueOf(epic.getStatusTask()));
                    jsonObject.addProperty("startTime", epic.getStartTime());
                    jsonObject.addProperty("duration", epic.getDuration());
                    return jsonObject;
                })
                .registerTypeAdapter(Subtask.class, (JsonSerializer<Subtask>) (subtask, type, jsonSerializationContext) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Type", String.valueOf(TypeTask.SUBTASK));
                    jsonObject.addProperty("idTask", subtask.getIdTask());
                    jsonObject.addProperty("nameTask", subtask.getNameTask());
                    jsonObject.addProperty("descriptionTask", subtask.getDescriptionTask());
                    jsonObject.addProperty("epicId", String.valueOf(subtask.getEpicId()));
                    jsonObject.addProperty("statusTask", String.valueOf(subtask.getStatusTask()));
                    jsonObject.addProperty("startTime", subtask.getStartTime());
                    jsonObject.addProperty("duration", subtask.getDuration());
                    return jsonObject;
                })
                .registerTypeAdapter(CommonTask.class, (JsonSerializer<CommonTask>) (commonTask, type, jsonSerializationContext) -> {
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("Type", String.valueOf(TypeTask.TASK));
                    jsonObject.addProperty("idTask", commonTask.getIdTask());
                    jsonObject.addProperty("nameTask", commonTask.getNameTask());
                    jsonObject.addProperty("descriptionTask", commonTask.getDescriptionTask());
                    jsonObject.addProperty("statusTask", String.valueOf(commonTask.getStatusTask()));
                    jsonObject.addProperty("startTime", commonTask.getStartTime());
                    jsonObject.addProperty("duration", commonTask.getDuration());
                    return jsonObject;
                })
                .registerTypeAdapter(Epic.class, (JsonDeserializer<Epic>) (json, typeOfT, context) -> {
                    JsonObject jsonObject = json.getAsJsonObject();
                    int idTask = jsonObject.get("idTask").getAsInt();
                    String nameTask = jsonObject.get("nameTask").getAsString();
                    String descriptionTask = jsonObject.get("descriptionTask").getAsString();

                    return new Epic(idTask, nameTask, descriptionTask);

                })
                .registerTypeAdapter(CommonTask.class, (JsonDeserializer<CommonTask>) (json, typeOfT, context) -> {
                    JsonObject jsonObject = json.getAsJsonObject();
                    String nameTask = jsonObject.get("nameTask").getAsString();
                    String descriptionTask = jsonObject.get("descriptionTask").getAsString();
                    Status statusTask = Status.valueOf(jsonObject.get("statusTask").getAsString());
                    String startTime = jsonObject.get("startTime").getAsString();
                    String duration = jsonObject.get("duration").getAsString();


                    return new CommonTask(nameTask, descriptionTask, statusTask, startTime, duration);
                })
                .create();
    }


    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }


}
