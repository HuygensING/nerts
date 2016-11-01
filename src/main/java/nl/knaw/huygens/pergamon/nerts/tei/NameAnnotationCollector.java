package nl.knaw.huygens.pergamon.nerts.tei;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Predicate;

import nl.knaw.huygens.pergamon.nerts.NamedEntityAnnotation;
import nl.knaw.huygens.pergamon.support.tei.Attributes;
import nl.knaw.huygens.pergamon.support.tei.export.ExportTextHandler;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

public class NameAnnotationCollector extends DelegatingVisitor<XmlContext> {

  /** Storage for handling nested elements. */
  private final Deque<NamedEntityAnnotation> stack = new ArrayDeque<NamedEntityAnnotation>();

  private final List<NamedEntityAnnotation> annotations;
  private final Predicate<Element> predicate;

  public NameAnnotationCollector(List<NamedEntityAnnotation> annotations, String refXmlId, String elementName, Predicate<Element> predicate) {
    super(new XmlContext());
    setTextHandler(new ExportTextHandler<XmlContext>());
    addElementHandler(new NameHandler(refXmlId), elementName);
    this.annotations = annotations;
    this.predicate = predicate;
  }

  private class NameHandler extends DefaultElementHandler<XmlContext> {
    private final String refXmlId;

    public NameHandler(String refXmlId) {
      this.refXmlId = refXmlId;
    }

    @Override
    public Traversal enterElement(Element element, XmlContext context) {
      if (predicate.test(element)) {
        NamedEntityAnnotation annotation = new NamedEntityAnnotation();
        annotation.setType(element.getName());
        annotation.setKey(element.getAttribute(Attributes.KEY, "?"));
        annotation.setResp(element.getAttribute(Attributes.RESP, "?"));
        annotation.setRefXmlId(refXmlId);
        annotation.setBeginPos(context.getLayerLength());
        stack.push(annotation);
      }
      return Traversal.NEXT;
    }

    @Override
    public Traversal leaveElement(Element element, XmlContext context) {
      if (predicate.test(element)) {
        NamedEntityAnnotation annotation = stack.pop();
        annotation.setEndPos(context.getLayerLength());
        // somewhat inefficient
        annotation.setName(context.getResult().substring(annotation.getBeginPos(), annotation.getEndPos()));
        annotations.add(annotation);
      }
      return Traversal.NEXT;
    }
  }

};
