package nl.knaw.huygens.pergamon.nerts.tool;

import nl.knaw.huygens.pergamon.nerts.NamedEntityAnnotation;
import nl.knaw.huygens.pergamon.nerts.tei.NameAnnotationCollector;
import nl.knaw.huygens.pergamon.support.tei.Documents;
import nl.knaw.huygens.pergamon.support.tei.TeiUtils;
import nl.knaw.huygens.pergamon.support.tei.XmlIdAssigner;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Document;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.XmlContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class TeiAnnotationLister implements Function<String, List<NamedEntityAnnotation>> {

  private final String anchorElement;
  private final String annotationElement;
  private final Predicate<Element> predicate;

  public TeiAnnotationLister(String anchorElement, String elementName, Predicate<Element> predicate) {
    this.anchorElement = anchorElement;
    this.annotationElement = elementName;
    this.predicate = predicate;
  }

  @Override
  public List<NamedEntityAnnotation> apply(String xml) {
    try {
      List<NamedEntityAnnotation> result = new ArrayList<>();
      Document document = Documents.newDocument(xml);
      document.accept(new XmlIdAssigner(anchorElement));
      for (Element element : document.getElementsByTagName(anchorElement)) {
        List<NamedEntityAnnotation> annotations = new ArrayList<>();
        DelegatingVisitor<XmlContext> visitor = new NameAnnotationCollector(annotations, annotationElement, predicate);
        element.accept(visitor);
        String text = visitor.getContext().getResult();
        String xmlId = TeiUtils.getIdAttribute(element);
        for (NamedEntityAnnotation annotation : annotations) {
          annotation.setRefXmlId(xmlId);
          annotation.setName(text.substring(annotation.getBeginPos(), annotation.getEndPos()));
          result.add(annotation);
        }
      }
      return result;
    } catch (Exception e) {
      System.out.printf("## %s%n%n%s%n", e.getMessage(), xml);
      throw e;
    }
  }

}
