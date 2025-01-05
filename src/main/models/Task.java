package main.models;

import java.util.UUID;

public class Task {
  private final String id;
  private final String name;

  public Task(String id, String name) {
    this.id = UUID.randomUUID().toString();
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}

