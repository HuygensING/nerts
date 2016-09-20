package nl.knaw.huygens.pergamon.nerts.tei;

import java.util.function.Predicate;

import nl.knaw.huygens.pergamon.support.tei.export.ExportVisitor;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

/**
 * Schedules elements for removal.
 * The actual removal is done by the export visitor.
 */
public class TeiNameRemover extends DelegatingVisitor<XmlContext> {

  public TeiNameRemover(Predicate<Element> predicate) {
    super(new XmlContext());
    setDefaultElementHandler(new RemoveHandler(predicate));
  }

  private static class RemoveHandler extends DefaultElementHandler<XmlContext> {
    Predicate<Element> predicate;

    public RemoveHandler(Predicate<Element> predicate) {
      this.predicate = predicate;
    }

    @Override
    public Traversal leaveElement(Element element, XmlContext context) {
      if (predicate.test(element)) {
        element.setName(ExportVisitor.DEFAULT_VOID_TAG);
      }
      return Traversal.NEXT;
    }
  }

}
