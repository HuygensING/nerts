package nl.knaw.huygens.pergamon.support.tei;

import java.util.function.Function;

import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.ElementHandler;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;

/**
 * A VisiTEI element handler that collects the text in an element
 * and processes it, using the {@code handleText} method.
 */
public abstract class ElementTextHandler<T extends XmlContext> implements ElementHandler<T> {

  private final Function<String, String> textFilter;

  public ElementTextHandler(Function<String, String> textFilter) {
    this.textFilter = textFilter;
  }

  public ElementTextHandler() {
    this(text -> text);
  }

  public abstract void handleText(Element element, String text);

  @Override
  public Traversal enterElement(Element element, T context) {
    context.openLayer();
    return Traversal.NEXT;
  }

  @Override
  public Traversal leaveElement(Element element, T context) {
    String text = textFilter.apply(context.closeLayer());
    handleText(element, text);
    return Traversal.NEXT;
  }

}
