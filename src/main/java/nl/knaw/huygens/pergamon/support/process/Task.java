package nl.knaw.huygens.pergamon.support.process;

/**
 * A task, to be called individually or as part of a sequence.
 * See the {@link Pipeline} class.
 */
public interface Task {

  String getDescription();

  void call() throws Exception;

}
