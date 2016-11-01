package nl.knaw.huygens.pergamon.nerts.text;

import java.util.function.Function;

/**
 * Encodes characters '<', '>', and '&'.
 */
public class XmlTextFilter implements Function<String, String> {

  @Override
  public String apply(String text) {
    int n = text.length();
    StringBuilder builder = new StringBuilder((int) (n * 1.1));
    for (int i = 0; i < n; i++) {
      char c = text.charAt(i);
      switch (c) {
      case '<':
        builder.append("&lt;");
        break;
      case '>':
        builder.append("&gt;");
        break;
      case '&':
        builder.append("&amp;");
        break;
      default:
        builder.append(c);
        break;
      }
    }
    return builder.toString();
  }

}
