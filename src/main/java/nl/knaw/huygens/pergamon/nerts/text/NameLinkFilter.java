package nl.knaw.huygens.pergamon.nerts.text;

import java.util.function.Function;

public class NameLinkFilter implements Function<String, String> {

  @Override
  public String apply(String text) {
    return text //
        .replaceAll("\\bD(e|i|u|el|er|es)\\b", "d$1") //
        .replaceAll("\\bL('|a|e)\\b", "l$1") //
        .replaceAll("\\bAb\\b", "ab") //
        .replaceAll("\\bOp\\b", "op") //
        .replaceAll("\\bU(an|on)\\b", "v$1") //
        .replaceAll("\\bV(an|on)\\b", "v$1");
  }

}
