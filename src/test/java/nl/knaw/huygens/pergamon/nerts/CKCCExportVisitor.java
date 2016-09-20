package nl.knaw.huygens.pergamon.nerts;

import java.util.List;

import nl.knaw.huygens.pergamon.support.tei.Documents;
import nl.knaw.huygens.pergamon.support.tei.TeiException;
import nl.knaw.huygens.pergamon.support.tei.export.AttributeFilter;
import nl.knaw.huygens.pergamon.support.tei.export.ExportElementHandler;
import nl.knaw.huygens.pergamon.support.tei.export.ExportVisitor;
import nl.knaw.huygens.tei.Document;
import nl.knaw.huygens.tei.Element;

public class CKCCExportVisitor extends ExportVisitor {

  private static final String[] META_ATTRS = { "type", "value", "resp", "cert" };
  private static final String[] NAME_ATTRS = { "type", "subtype", "key", "keys", "text", "resp", "cert" };

  public static final ExportElementHandler DEFAULT = new ExportElementHandler() //
      .addAttributeFilter(new AttributeFilter(META_ATTRS), "interp", "meta") //
      .addAttributeFilter(new AttributeFilter(NAME_ATTRS), "name", "geogName", "orgName", "persName", "placeName", "rs") //
      .addAttributeFilter(new AttributeFilter("lang", "base"), "body") //
      .addAttributeFilter(new AttributeFilter("role", "cols"), "cell") //
      .addAttributeFilter(new AttributeFilter("type", "when", "resp"), "date") //
      .addAttributeFilter(new AttributeFilter("type", "subtype", "resp"), "div") //
      .addAttributeFilter(new AttributeFilter("url", "width", "height", "scale"), "graphic") //
      .addAttributeFilter(new AttributeFilter("type", "lang", "rend"), "head") //
      .addAttributeFilter(new AttributeFilter("type", "lang"), "lg") //
      .addAttributeFilter(new AttributeFilter("type", "lang", "rend"), "p") //
      .addAttributeFilter(new AttributeFilter("n", "break"), "pb") //
      .addAttributeFilter(new AttributeFilter("type", "org", "resp", "n"), "seg");

  public static String export(String xml) {
    return Documents.visitXml(xml, new CKCCExportVisitor()).getResult();
  }

  public static String export(Document document) {
    CKCCExportVisitor visitor = new CKCCExportVisitor();
    document.accept(visitor);
    return visitor.getResult();
  }

  public static String export(Element element) {
    CKCCExportVisitor visitor = new CKCCExportVisitor();
    element.accept(visitor);
    return visitor.getResult();
  }

  public static String export(Document document, String rootName) throws TeiException {
    List<Element> elements = document.getElementsByTagName(rootName);
    if (elements.size() == 1) {
      return export(elements.get(0));
    } else {
      throw new TeiException("TEI contains %d '%s' elements", elements.size(), rootName);
    }
  }

  // ---------------------------------------------------------------------------

  public CKCCExportVisitor() {
    super(true);
    setDefaultElementHandler(DEFAULT);
  }

}
