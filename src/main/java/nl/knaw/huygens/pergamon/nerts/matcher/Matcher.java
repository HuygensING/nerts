package nl.knaw.huygens.pergamon.nerts.matcher;

import java.util.List;

public interface Matcher {

  List<Match> match(String text);

}
