package nl.knaw.huygens.pergamon.nerts;

import java.util.List;

public interface Matcher {

  List<Match> match(String text);

}
