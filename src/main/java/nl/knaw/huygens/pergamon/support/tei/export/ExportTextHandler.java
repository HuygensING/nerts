package nl.knaw.huygens.pergamon.support.tei.export;

import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultTextHandler;

public class ExportTextHandler<T extends XmlContext> extends DefaultTextHandler<T> {

  @Override
  protected String filterText(String text) {
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
