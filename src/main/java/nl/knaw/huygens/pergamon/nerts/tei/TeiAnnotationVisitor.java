package nl.knaw.huygens.pergamon.nerts.tei;

import java.util.List;
import java.util.function.Predicate;

import nl.knaw.huygens.pergamon.nerts.Match;
import nl.knaw.huygens.pergamon.nerts.Matcher;
import nl.knaw.huygens.pergamon.nerts.TextAnnotator;
import nl.knaw.huygens.pergamon.support.tei.ElementFilter;
import nl.knaw.huygens.pergamon.support.tei.TeiUtils;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Text;
import nl.knaw.huygens.tei.TextHandler;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;

public class TeiAnnotationVisitor extends DelegatingVisitor<XmlContext> {

  public static final String BON = "«+NAME+»";
  public static final String EON = "«-NAME-»";

  public TeiAnnotationVisitor(Predicate<Element> elementFilter, Matcher matcher, TextAnnotator annotator, String langAttribute) {
    super(new XmlContext());
    setDefaultElementHandler(new ElementFilter<XmlContext>(elementFilter));
    setTextHandler(new AnnotationTextHandler(matcher, annotator, langAttribute));
  }

  private static class AnnotationTextHandler implements TextHandler<XmlContext> {
    private final Matcher matcher;
    private final TextAnnotator annotator;
    private final String langAttribute;

    public AnnotationTextHandler(Matcher matcher, TextAnnotator annotator, String langAttribute) {
      this.matcher = matcher;
      this.annotator = annotator;
      this.langAttribute = langAttribute;
    }

    @Override
    public Traversal visitText(Text text, XmlContext context) {
      // The approach is to look for matches in a normalized form of the text
      // and to apply the annotation on the original text.
      // This only works if normalizatin maps characters 1-to-1.
      String source = text.getText();
      if (source.length() > 1) {
        List<Match> matches = matcher.match(source);
        if (!matches.isEmpty()) {
          String lang = TeiUtils.getImpliedAttribute(text.getParent(), langAttribute, "?");
          text.setText(annotator.annotate(source, matches, lang));
        }
      }
      return Traversal.NEXT;
    }
  }

};
