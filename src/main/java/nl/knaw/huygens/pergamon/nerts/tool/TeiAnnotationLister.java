package nl.knaw.huygens.pergamon.nerts.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import nl.knaw.huygens.pergamon.nerts.NamedEntityAnnotation;
import nl.knaw.huygens.pergamon.nerts.tei.NameAnnotationCollector;
import nl.knaw.huygens.pergamon.support.tei.Documents;
import nl.knaw.huygens.pergamon.support.tei.TeiUtils;
import nl.knaw.huygens.pergamon.support.tei.XmlIdAssigner;
import nl.knaw.huygens.tei.Document;
import nl.knaw.huygens.tei.Element;

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
      List<NamedEntityAnnotation> annotations = new ArrayList<>();
      Document document = Documents.newDocument(xml);
      document.accept(new XmlIdAssigner(anchorElement));
      for (Element element : document.getElementsByTagName(anchorElement)) {
        String xmlId = TeiUtils.getIdAttribute(element);
        element.accept(new NameAnnotationCollector(annotations, xmlId, annotationElement, predicate));
      }
      return annotations;
    } catch (Exception e) {
      System.out.printf("## %s%n%n%s%n", e.getMessage(), xml);
      throw e;
    }
  }

}
