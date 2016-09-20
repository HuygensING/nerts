package nl.knaw.huygens.pergamon.nerts.tool;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import nl.knaw.huygens.pergamon.nerts.NamedEntityAnnotation;

public class TeiAnnotationListerTest {

  private String decode(String text) {
    return text.replace('|', '"');
  }

  @Test
  public void testSequential() {
    String xml = decode("<doc><p><name key=|k|>text</name>text<name resp=|r|>text</name></p></doc>");
    TeiAnnotationLister lister = new TeiAnnotationLister("p", "name", element -> true);
    List<NamedEntityAnnotation> annotations = lister.apply(xml);
    Assert.assertEquals(2, annotations.size());
    Assert.assertEquals(decode("|p-1|;|0|;|4|;|name|;|k|;|?|;|text|"), annotations.get(0).toString());
    Assert.assertEquals(decode("|p-1|;|8|;|12|;|name|;|?|;|r|;|text|"), annotations.get(1).toString());
  }

  @Test
  public void testNested() {
    String xml = decode("<doc><p>text</p><p><name key=|k1|><name key=|k2| resp=|r|>aaaa</name>bbbb</name></p></doc>");
    TeiAnnotationLister lister = new TeiAnnotationLister("p", "name", element -> true);
    List<NamedEntityAnnotation> annotations = lister.apply(xml);
    Assert.assertEquals(2, annotations.size());
    Assert.assertEquals(decode("|p-2|;|0|;|4|;|name|;|k2|;|r|;|aaaa|"), annotations.get(0).toString());
    Assert.assertEquals(decode("|p-2|;|0|;|8|;|name|;|k1|;|?|;|aaaabbbb|"), annotations.get(1).toString());
  }

}
