package nl.knaw.huygens.pergamon.support.process;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A sequence of tasks.
 */
public class Pipeline implements Task {

  /**
   * Executes the provided tasks.
   */
  public static void execute(Task... tasks) throws Exception {
    Pipeline pipeline = new Pipeline();
    for (Task task : tasks) {
      pipeline.add(task);
    }
    pipeline.call();
  }

  // -------------------------------------------------------------------

  private final List<Task> tasks;

  public Pipeline() {
    tasks = new ArrayList<Task>();
  }

  public void add(Task task) {
    tasks.add(Objects.requireNonNull(task, "Invalid task"));
  }

  @Override
  public String getDescription() {
    return Pipeline.class.getName();
  }

  @Override
  public void call() throws Exception {
    long start = System.currentTimeMillis();
    for (Task task : tasks) {
      System.out.printf("-- %s%n", task.getDescription());
      task.call();
    }
    long time = System.currentTimeMillis() - start;
    System.out.printf("-- Time %s%n", formatTime(time));
  }

  private String formatTime(long time) {
    long msec = time % 1000;
    time /= 1000;
    return String.format("%02d:%02d.%03d", time / 60, time % 60, msec);
  }

}
