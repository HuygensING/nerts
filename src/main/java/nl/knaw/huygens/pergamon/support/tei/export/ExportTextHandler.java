package nl.knaw.huygens.pergamon.support.tei.export;

import nl.knaw.huygens.pergamon.nerts.text.XmlTextFilter;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultTextHandler;

public class ExportTextHandler<T extends XmlContext> extends DefaultTextHandler<T> {

  private final XmlTextFilter filter;

  public ExportTextHandler() {
    filter = new XmlTextFilter();
  }

  @Override
  protected String filterText(String text) {
    return filter.apply(text);
  }

}
