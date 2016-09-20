package nl.knaw.huygens.pergamon.nerts.tei;

import nl.knaw.huygens.pergamon.nerts.NamedEntityAnnotation;
import nl.knaw.huygens.pergamon.support.tei.Documents;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NameAnnotationCollectorTest {

  private String decode(String text) {
    return text.replace('|', '"');
  }

  @Test
  public void testSequential() {
    String xml = decode("<p xml:id=|p-1|><name key=|k|>text</name>text<name resp=|r|>text</name></p>");
    List<NamedEntityAnnotation> annotations = new ArrayList<>();
    Documents.visitXml(xml, new NameAnnotationCollector(annotations, "name", element -> true));
    Assert.assertEquals(2, annotations.size());
    Assert.assertEquals(decode("|null|;|0|;|4|;|name|;|k|;|?|;|null|"), annotations.get(0).toString());
    Assert.assertEquals(decode("|null|;|8|;|12|;|name|;|?|;|r|;|null|"), annotations.get(1).toString());
  }

  @Test
  public void testNested() {
    String xml = decode("<p xml:id=|p-1|><name key=|k1|><name key=|k2| resp=|r|>text</name>text</name></p>");
    List<NamedEntityAnnotation> annotations = new ArrayList<>();
    Documents.visitXml(xml, new NameAnnotationCollector(annotations, "name", element -> true));
    Assert.assertEquals(2, annotations.size());
    Assert.assertEquals(decode("|null|;|0|;|4|;|name|;|k2|;|r|;|null|"), annotations.get(0).toString());
    Assert.assertEquals(decode("|null|;|0|;|8|;|name|;|k1|;|?|;|null|"), annotations.get(1).toString());
  }

}
