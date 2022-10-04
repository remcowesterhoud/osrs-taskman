package com.westerhoud.osrs.taskman.services;

import com.westerhoud.osrs.taskman.model.Credentials;
import com.westerhoud.osrs.taskman.model.OverallProgress;
import com.westerhoud.osrs.taskman.model.Task;
import java.io.IOException;

public interface TaskService {

  String authenticate(final Credentials credentials) throws IOException;

  Task currentTask(final Credentials credentials) throws IOException;

  Task generateTask(final Credentials credentials) throws IOException;

  void completeTask(final Credentials credentials) throws IOException;

  OverallProgress progress(final Credentials credentials) throws IOException;
}
