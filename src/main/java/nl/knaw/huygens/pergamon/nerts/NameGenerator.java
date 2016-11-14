package nl.knaw.huygens.pergamon.nerts;

import java.util.List;
import java.util.function.Function;

/**
 * Generates a list of names given a specified name.
 * Usually the list will contain the specified name.
 */
public interface NameGenerator extends Function<String, List<String>> {

}
