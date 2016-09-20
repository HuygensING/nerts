package nl.knaw.huygens.pergamon.nerts.tei;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import nl.knaw.huygens.pergamon.nerts.Gazetteer;
import nl.knaw.huygens.pergamon.nerts.Ident;
import nl.knaw.huygens.pergamon.support.tei.Attributes;
import nl.knaw.huygens.pergamon.support.tei.ElementTextHandler;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.XmlContext;

public class TeiNameIdentifier extends DelegatingVisitor<XmlContext> {

  private final Gazetteer gazetteer;
  private final Predicate<Element> predicate;

  public TeiNameIdentifier(Gazetteer gazetteer, Predicate<Element> predicate, String... elementNames) {
    super(new XmlContext());
    addElementHandler(new IdentifierHandler(), elementNames);
    this.gazetteer = gazetteer;
    this.predicate = predicate;
  }

  private class IdentifierHandler extends ElementTextHandler<XmlContext> {
    @Override
    public void handleText(Element element, String text) {
      if (predicate.test(element)) {
        List<Ident> list = gazetteer.identify(text);
        if (!list.isEmpty()) {
          String value = list.stream().map(Ident::toString).collect(Collectors.joining(" "));
          element.setAttribute(Attributes.KEYS, value);
        }
      }
    }
  }

}
