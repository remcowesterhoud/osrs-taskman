package com.westerhoud.osrs.taskman.services;

import com.westerhoud.osrs.taskman.model.CollectionLogMasterCommandData;
import com.westerhoud.osrs.taskman.model.CommandData;
import com.westerhoud.osrs.taskman.model.OverallProgress;
import com.westerhoud.osrs.taskman.model.Task;
import com.westerhoud.osrs.taskman.model.TierProgress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Responsible for storing players current task. This will be read when someone uses the !taskman
 * command.
 */
@Slf4j
@Component
public class CommandDataStore {

  private static final String MISSING_TASK_IMAGE_URL = "https://oldschool.runescape.wiki/images/Cake_of_guidance_detail.png?c3595";
  private final Map<String, CommandData> taskByRsn = new ConcurrentHashMap<>();

  public void addTask(final String rsn, final Task task) {
    final var data = getCommandDataForRsn(rsn);
    data.setTask(task);
  }

  public void addProgress(final String rsn, final OverallProgress progress) {
    final var data = getCommandDataForRsn(rsn);
    data.setTier(progress.getCurrentTier());
    final TierProgress tierProgress = progress.getProgressByTier().get(progress.getCurrentTier());
    final int percentage =
        (int) ((double) tierProgress.getValue() / (double) tierProgress.getMaxValue() * 100);
    data.setProgressPercentage(percentage);
  }

  public CommandData getData(final String rsn) {
    return taskByRsn.get(rsn);
  }

  private CommandData getCommandDataForRsn(final String rsn) {
    CommandData data = taskByRsn.get(rsn);
    if (data == null) {
      data = new CommandData();
      taskByRsn.put(rsn, data);
    }
    return data;
  }

  public void setCollectionLogMasterCommandData(final String rsn, final String name, final String tier,
      final int progressPercentage) {
    final var data = getCommandDataForRsn(rsn);
    data.setTask(Task.builder()
        .name(name)
        .imageUrl(MISSING_TASK_IMAGE_URL) // Not used in the Collection Log Master plugin
        .build());
    data.setTier(tier);
    data.setProgressPercentage(progressPercentage);
  }
}
