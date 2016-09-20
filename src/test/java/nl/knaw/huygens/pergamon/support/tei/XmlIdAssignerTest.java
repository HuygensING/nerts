package nl.knaw.huygens.pergamon.support.tei;

import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import nl.knaw.huygens.pergamon.support.tei.export.ExportVisitor;
import nl.knaw.huygens.tei.Document;

public class XmlIdAssignerTest {

  private static class Tester implements Function<String, String> {
    private final String[] elements;

    public Tester(String... elements) {
      this.elements = elements;
    }

    @Override
    public String apply(String xml) {
      Document document = Documents.newDocument(xml);
      document.accept(new XmlIdAssigner(elements));
      return ExportVisitor.export(document);
    }
  }

  private static final String XML = "<doc><p>x</p><q>y</q><p>z</p></doc>";

  @Test
  public void testElementWithFrequency1() {
    String expected = "<doc><p>x</p><q xml:id=|q-1|>y</q><p>z</p></doc>".replace('|', '"');
    Assert.assertEquals(expected, new Tester("q").apply(XML));
  }

  @Test
  public void testElementWithFrequency2() {
    String expected = "<doc><p xml:id=|p-1|>x</p><q>y</q><p xml:id=|p-2|>z</p></doc>".replace('|', '"');
    Assert.assertEquals(expected, new Tester("p").apply(XML));
  }

  @Test
  public void testMultipleElements() {
    String expected = "<doc><p xml:id=|p-1|>x</p><q xml:id=|q-1|>y</q><p xml:id=|p-2|>z</p></doc>".replace('|', '"');
    Assert.assertEquals(expected, new Tester("p", "q").apply(XML));
  }

}
