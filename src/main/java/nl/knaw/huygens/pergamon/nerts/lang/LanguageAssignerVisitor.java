package nl.knaw.huygens.pergamon.nerts.lang;

import nl.knaw.huygens.pergamon.nerts.text.BracketFilter;
import nl.knaw.huygens.pergamon.support.tei.ElementTextHandler;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.FilterElementHandler;

import java.util.Objects;

/**
 * Performs language identification for the text in certain TEI-elements.
 * The result is stored in a configurable attribute of the elements,
 * allowing them to be used temporarily if the attribute key differs
 * from {@code Element.LANGUAGE}.
 */
public class LanguageAssignerVisitor extends DelegatingVisitor<XmlContext> {

  // TODO factor these out...
  public static final String[] ANALYZED_ELEMENTS = { "head", "lg", "p", "q", "seg" };
  public static final String[] FILTERED_ELEMENTS = { "date", "formula", "note", "sub", "sup" };

  public LanguageAssignerVisitor(LanguageIdentifier identifier, String langAttr) {
    super(new XmlContext());
    addElementHandler(new LanguageHandler(identifier, langAttr), ANALYZED_ELEMENTS);
    addElementHandler(new FilterElementHandler<XmlContext>(), FILTERED_ELEMENTS);
  }

  public LanguageAssignerVisitor(LanguageIdentifier identifier) {
    this(identifier, Element.LANGUAGE);
  }

  /**
   * Basic language assigner.
   * Filters parentheses and brackets before processing.
   */
  private static class LanguageHandler extends ElementTextHandler<XmlContext> {
    private final LanguageIdentifier langIdentifier;
    private final String langAttribute;

    public LanguageHandler(LanguageIdentifier identifier, String langAttr) {
      super(new BracketFilter());
      this.langIdentifier = Objects.requireNonNull(identifier);
      this.langAttribute = Objects.requireNonNull(langAttr);
    }

    @Override
    public void handleText(Element element, String text) {
      String language = element.getAttribute(Element.LANGUAGE);
      if (!language.isEmpty()) {
        element.setAttribute(langAttribute, language);
      } else if (!text.isEmpty()) {
        element.setAttribute(langAttribute, langIdentifier.apply(text));
      }
    }
  }

}
