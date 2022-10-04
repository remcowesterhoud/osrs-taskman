package com.westerhoud.osrs.taskman.services;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.westerhoud.osrs.taskman.model.Credentials;
import com.westerhoud.osrs.taskman.model.JwtWrapper;
import com.westerhoud.osrs.taskman.model.OverallProgress;
import com.westerhoud.osrs.taskman.model.Task;
import com.westerhoud.osrs.taskman.model.Tier;
import com.westerhoud.osrs.taskman.model.TierProgress;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class WebsiteService implements TaskService {

  private static final String LOGIN_URI = "/login";
  private static final String CURRENT_TASK_URI = "/current_task";
  private static final String GENERATE_TASK_URI = "/generate_task";
  private static final String COMPLETE_TASK_URI = "/complete_task";
  private static final String PROGRESS_URI = "/task_progress";
  private static final String ACCESS_TOKEN_HEADER = "x-access-token";
  private final String baseUrl;
  private final OkHttpClient client = new OkHttpClient();
  private final Gson gson = new Gson();

  public WebsiteService(@Value("${site.api.url}") final String baseUrl) {
    this.baseUrl = baseUrl;
  }

  @Override
  public String authenticate(final Credentials credentials) throws IOException {
    final String auth =
        okhttp3.Credentials.basic(credentials.getIdentifier(), credentials.getPassword());
    final Request request =
        new Request.Builder()
            .url(baseUrl + LOGIN_URI)
            .addHeader("Authorization", auth)
            .get()
            .build();

    try (final Response response = client.newCall(request).execute()) {
      if (response.code() == HttpStatus.UNAUTHORIZED.value()) {
        throw new ResponseStatusException(
            HttpStatus.FORBIDDEN, "The credentials configured in the plugin are invalid.");
      } else if (response.isSuccessful()) {
        return gson.fromJson(response.body().string(), JwtWrapper.class).getToken();
      }
    }
    throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Something went wrong during authentication. Please try again later. "
            + "If the problem persists please reach out.");
  }

  @Override
  public Task currentTask(final Credentials credentials) throws IOException {
    final var jwt = authenticate(credentials);
    final Request request =
        new Request.Builder()
            .url(baseUrl + CURRENT_TASK_URI)
            .addHeader(ACCESS_TOKEN_HEADER, jwt)
            .get()
            .build();

    try (final Response response = client.newCall(request).execute()) {
      if (response.isSuccessful()) {
        return gson.fromJson(response.body().string(), WebsiteTaskWrapper.class).toTask();
      }
    }

    throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Something went wrong fetching the current task. Please try again later. "
            + "If the problem persists please reach out.");
  }

  @Override
  public Task generateTask(final Credentials credentials) throws IOException {
    final var jwt = authenticate(credentials);
    final Request request =
        new Request.Builder()
            .url(baseUrl + GENERATE_TASK_URI)
            .addHeader(ACCESS_TOKEN_HEADER, jwt)
            .get()
            .build();

    try (final Response response = client.newCall(request).execute()) {
      if (response.isSuccessful()) {
        try {
          return gson.fromJson(response.body().string(), WebsiteTaskWrapper.class).toTask();
        } catch (final JsonSyntaxException ex) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST, "Complete your current task before generating a new one!");
        }
      }
    }

    throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Something went wrong fetching the current task. Please try again later. "
            + "If the problem persists please reach out.");
  }

  @Override
  public void completeTask(final Credentials credentials) throws IOException {
    final var jwt = authenticate(credentials);
    final Request request =
        new Request.Builder()
            .url(baseUrl + COMPLETE_TASK_URI)
            .addHeader(ACCESS_TOKEN_HEADER, jwt)
            .get()
            .build();

    try (final Response response = client.newCall(request).execute()) {
      if (!response.isSuccessful()) {
        throw new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "Something went wrong completing the current task. Please try again later. "
                + "If the problem persists please reach out.");
      }
    }
  }

  @Override
  public OverallProgress progress(final Credentials credentials) throws IOException {
    final var jwt = authenticate(credentials);
    final Request request =
        new Request.Builder()
            .url(baseUrl + PROGRESS_URI)
            .addHeader(ACCESS_TOKEN_HEADER, jwt)
            .get()
            .build();

    try (final Response response = client.newCall(request).execute()) {
      if (response.isSuccessful()) {
        return gson.fromJson(response.body().string(), WebsiteProgress.class).toOverallProgress();
      }
    }

    throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Something went wrong fetching the current task. Please try again later. "
            + "If the problem persists please reach out.");
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  private class WebsiteTaskWrapper {
    private WebsiteTask message;

    public Task toTask() {
      return Task.builder().name(message.getTaskName()).imageUrl(message.getTaskImage()).build();
    }
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  private class WebsiteTask {
    private String taskName;
    private String taskImage;
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  private class WebsiteProgress {
    private WebsiteOverallProgress message;

    public OverallProgress toOverallProgress() {
      final Map<String, TierProgress> progress = new HashMap<>();
      progress.put(Tier.EASY.getName(), message.easyTierProgress());
      progress.put(Tier.MEDIUM.getName(), message.mediumTierProgress());
      progress.put(Tier.HARD.getName(), message.hardTierProgress());
      progress.put(Tier.ELITE.getName(), message.eliteTierProgress());
      return OverallProgress.builder().progressByTier(progress).build();
    }
  }

  @AllArgsConstructor
  @NoArgsConstructor
  @Data
  private class WebsiteOverallProgress {
    @SerializedName("easy_complete")
    private int easyComplete;

    @SerializedName("easy_progress")
    private int easyProgress;

    @SerializedName("easy_total")
    private int easyTotal;

    @SerializedName("medium_complete")
    private int mediumComplete;

    @SerializedName("medium_progress")
    private int mediumProgress;

    @SerializedName("medium_total")
    private int mediumTotal;

    @SerializedName("hard_complete")
    private int hardComplete;

    @SerializedName("hard_progress")
    private int hardProgress;

    @SerializedName("hard_total")
    private int hardTotal;

    @SerializedName("elite_complete")
    private int eliteComplete;

    @SerializedName("elite_progress")
    private int eliteProgress;

    @SerializedName("elite_total")
    private int eliteTotal;

    public TierProgress easyTierProgress() {
      return TierProgress.builder().maxValue(easyTotal).value(easyComplete).build();
    }

    public TierProgress mediumTierProgress() {
      return TierProgress.builder().maxValue(mediumTotal).value(mediumComplete).build();
    }

    public TierProgress hardTierProgress() {
      return TierProgress.builder().maxValue(hardTotal).value(hardComplete).build();
    }

    public TierProgress eliteTierProgress() {
      return TierProgress.builder().maxValue(eliteTotal).value(eliteComplete).build();
    }
  }
}
