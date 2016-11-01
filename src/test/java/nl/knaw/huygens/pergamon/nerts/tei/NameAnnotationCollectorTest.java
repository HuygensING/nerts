package nl.knaw.huygens.pergamon.nerts.tei;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import nl.knaw.huygens.pergamon.nerts.NamedEntityAnnotation;
import nl.knaw.huygens.pergamon.support.tei.Documents;

public class NameAnnotationCollectorTest {

  private String decode(String text) {
    return text.replace('|', '"');
  }

  @Test
  public void testSequential() {
    String xml = decode("<p xml:id=|p-1|><name key=|k|>text</name>text<name resp=|r|>text</name></p>");
    List<NamedEntityAnnotation> annotations = new ArrayList<>();
    Documents.visitXml(xml, new NameAnnotationCollector(annotations, "p-1", "name", element -> true));
    Assert.assertEquals(2, annotations.size());
    Assert.assertEquals(decode("|p-1|;|0|;|4|;|name|;|k|;|?|;|text|"), annotations.get(0).toString());
    Assert.assertEquals(decode("|p-1|;|8|;|12|;|name|;|?|;|r|;|text|"), annotations.get(1).toString());
  }

  @Test
  public void testNested() {
    String xml = decode("<p xml:id=|p-1|><name key=|k1|><name key=|k2| resp=|r|>text</name>text</name></p>");
    List<NamedEntityAnnotation> annotations = new ArrayList<>();
    Documents.visitXml(xml, new NameAnnotationCollector(annotations, "p-1", "name", element -> true));
    Assert.assertEquals(2, annotations.size());
    Assert.assertEquals(decode("|p-1|;|0|;|4|;|name|;|k2|;|r|;|text|"), annotations.get(0).toString());
    Assert.assertEquals(decode("|p-1|;|0|;|8|;|name|;|k1|;|?|;|texttext|"), annotations.get(1).toString());
  }

  @Test
  public void testCharacterEntity() {
    String xml = decode("<p xml:id=|p-1|><name key=|c1|>Constantijn</name> &amp; <name key=|c2|>Christiaan</name></p>");
    List<NamedEntityAnnotation> annotations = new ArrayList<>();
    Documents.visitXml(xml, new NameAnnotationCollector(annotations, "p-1", "name", element -> true));
    Assert.assertEquals(2, annotations.size());
    Assert.assertEquals(decode("|p-1|;|0|;|11|;|name|;|c1|;|?|;|Constantijn|"), annotations.get(0).toString());
    Assert.assertEquals(decode("|p-1|;|18|;|28|;|name|;|c2|;|?|;|Christiaan|"), annotations.get(1).toString());
  }

}
