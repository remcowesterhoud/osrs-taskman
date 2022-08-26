package com.westerhoud.osrs.taskman.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.westerhoud.osrs.taskman.dto.sheet.SheetTaskDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
public class SheetService {
    private static final int EASY = 0;
    private static final int MEDIUM = 1;
    private static final int HARD = 2;
    private static final int ELITE = 3;
    private static final int START_ROW = 2; // Row at which the tasks start
    private static final int TASK_NAME_COLUMN = 0; // First column on the sheet, index 0 in array
    private static final int TASK_IMAGE_COLUMN = 1; // Second column on the sheet, index 1 in array
    private static final String APPLICATION_NAME = "Taskman Plugin";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private final Sheets service;
    private final Random random;
    @Value("${sheets.api.serviceaccount.email}")
    private String serviceaccountEmail;

    public SheetService(@Value("${sheets.api.credentials}") final String credentialsJson) throws GeneralSecurityException, IOException {
        service = new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, getCredentials(credentialsJson))
                .setApplicationName(APPLICATION_NAME)
                .build();
        random = new Random();
    }

    public SheetTask generateTask(final String spreadsheetId) throws IOException {
        final String tier = getTier(spreadsheetId);
        final var sheetRange = String.format("'%s'!A2:C", tier);
        final var cellNewTask = "'Dashboard'!B15";
        final var cellNewTaskImage = "'Dashboard'!C15";
        final var cellInfoCurrentTier = "'Info'!B13";
        final var cellInfoCurrentTask = "'Info'!B14";

        final List<List<Object>> rows = service.spreadsheets().values()
                .get(spreadsheetId, sheetRange)
                .setValueRenderOption("FORMULA")
                .execute()
                .getValues();
        final List<SheetTask> uncompletedTasksInTier = rows.stream()
                .map(row ->
                        new SheetTask(rows.indexOf(row) + START_ROW,
                                (String) row.get(TASK_NAME_COLUMN),
                                (String) row.get(TASK_IMAGE_COLUMN),
                                row.size() >= 3))
                .filter(task -> !task.completed())
                .toList();

        final SheetTask newTask = uncompletedTasksInTier.get(random.nextInt(uncompletedTasksInTier.size()));

        final List<ValueRange> data = List.of(
                // Update info tab
                new ValueRange().setRange(cellInfoCurrentTier).setValues(List.of(List.of(tier))),
                new ValueRange().setRange(cellInfoCurrentTask).setValues(List.of(List.of("C" + newTask.rowNumber))),
                // Update dashboard
                new ValueRange().setRange(cellNewTask).setValues(List.of(List.of(newTask.name))),
                new ValueRange().setRange(cellNewTaskImage).setValues(List.of(List.of(newTask.image)))
        );
        final var batchBody = new BatchUpdateValuesRequest()
                .setValueInputOption("USER_ENTERED")
                .setData(data);
        service.spreadsheets().values()
                .batchUpdate(spreadsheetId, batchBody)
                .execute();
        return newTask;
    }

    /**
     * Reads the Info tab and decides the current tier of the tasker depending on completion and total counts
     *
     * @return The name of the sheet of the tasker's tier
     */
    private String getTier(final String spreadsheetId) throws IOException {
        final BatchGetValuesResponse tierProgressBatchValues;
        try {
            tierProgressBatchValues = service.spreadsheets().values()
                    .batchGet(spreadsheetId)
                    .setRanges(List.of(
                            "'Info'!B1:B2",
                            "'Info'!B4:B5",
                            "'Info'!B7:B8",
                            "'Info'!B10:B11"))
                    .execute();
        } catch (GoogleJsonResponseException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find spreadsheet with key %s. Please make sure you have set the" +
                            " correct key in the configurations and that you have given editor access to %s.",
                            spreadsheetId, serviceaccountEmail));
        }

        if (!hasCompletedTier(tierProgressBatchValues, EASY)) {
            return "Easy";
        } else if (!hasCompletedTier(tierProgressBatchValues, MEDIUM)) {
            return "Medium";
        } else if (!hasCompletedTier(tierProgressBatchValues, HARD)) {
            return "Hard";
        } else if (!hasCompletedTier(tierProgressBatchValues, ELITE)) {
            return "Elite";
        }
        return "Extra";
    }

    private boolean hasCompletedTier(final BatchGetValuesResponse tierProgressBatchValues, final int tier) {
        var total = tierProgressBatchValues.getValueRanges().get(tier).getValues().get(0).get(0);
        var completed = tierProgressBatchValues.getValueRanges().get(tier).getValues().get(1).get(0);
        return total.equals(completed);
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final String credentialsJson) throws IOException {
        // Load client secrets.
        InputStream in = new ByteArrayInputStream(credentialsJson.getBytes());
        // Build flow and trigger user authorization request.
        return GoogleCredential.fromStream(in)
                .createScoped(SCOPES);
    }

    public record SheetTask(int rowNumber, String name, String image, boolean completed) {
        public String getImageUrl() {
            return image.split("\"")[1];
        }

        public SheetTaskDto toDto() {
            return SheetTaskDto.builder()
                    .name(name)
                    .imageUrl(getImageUrl())
                    .build();
        }
    }
}
