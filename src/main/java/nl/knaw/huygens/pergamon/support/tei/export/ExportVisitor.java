package nl.knaw.huygens.pergamon.support.tei.export;

import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Document;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

public class ExportVisitor extends DelegatingVisitor<XmlContext> {

  /** Name of element for which tags are not exported. */
  public static final String DEFAULT_VOID_TAG = "VOID_TAG";
  /** Name of element that is not exported (both tags and content). */
  public static final String DEFAULT_VOID_ELEMENT = "VOID_ELEMENT";

  public static String export(Document document) {
    ExportVisitor visitor = new ExportVisitor();
    document.accept(visitor);
    return visitor.getResult();
  }

  // ---------------------------------------------------------------------------

  public ExportVisitor(boolean useDefaultFilter) {
    super(new XmlContext());
    setTextHandler(new ExportTextHandler<XmlContext>());
    setDefaultElementHandler(new ExportElementHandler());
    if (useDefaultFilter) {
      removeTags(DEFAULT_VOID_TAG);
      removeElements(DEFAULT_VOID_ELEMENT);
    }
  }

  public ExportVisitor() {
    this(true);
  }

  public String getResult() {
    return getContext().getResult().replaceAll("\n+", "\n");
  }

  /**
   * Do not export tags of elements with the specified names.
   */
  public void removeTags(String... names) {
    addElementHandler(new DefaultElementHandler<XmlContext>(), names);
  }

  /**
   * Do not export elements with the specified names,
   * i.e., both the tags and the content of elements.
   */
  public void removeElements(String names) {
    addElementHandler(new Blocker(), names);
  }

  private static class Blocker extends DefaultElementHandler<XmlContext> {
    @Override
    public Traversal enterElement(Element element, XmlContext context) {
      return Traversal.STOP;
    }
  }

}
