package nl.knaw.huygens.pergamon.nerts.tool;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import nl.knaw.huygens.pergamon.nerts.TextAnnotator;
import nl.knaw.huygens.pergamon.nerts.lang.LanguageIdentifier;
import nl.knaw.huygens.pergamon.support.tei.Documents;
import nl.knaw.huygens.pergamon.support.tei.export.AttributeFilter;
import nl.knaw.huygens.pergamon.support.tei.export.ExportElementHandler;
import nl.knaw.huygens.pergamon.support.tei.export.ExportVisitor;

public class TeiAnnotatorTest {

  private LanguageIdentifier identifier;
  private Function<String, String> xmlNormalizer;

  private static final String RESP = "resp=\"#nerts\"";

  @Before
  public void setupFilter() throws Exception {
    identifier = LanguageIdentifier.NO_IDENTIFIER;
    xmlNormalizer = new UnaryOperator<String>() {
      @Override
      public String apply(String xml) {
        // control order of attributes
        ExportElementHandler handler = new ExportElementHandler() //
            .addAttributeFilter(new AttributeFilter("key", "keys", "resp"), "name", "persName", "placeName");
        ExportVisitor visitor = new ExportVisitor();
        visitor.setDefaultElementHandler(handler);
        return Documents.visitXml(xml, visitor).getResult();
      }
    };
  }

  private void doTest(String expected, String text) {
    TextAnnotator textAnnotator = new TextAnnotator().withSpecialExceptions("fr", "Mons");
    Function<String, String> operator = new TeiAnnotator("name", "persName", "placeName")//
        .withLanguageIdentifier(identifier)//
        .withTeiAnnotatorName("nerts")//
        .withTextAnnotator(textAnnotator) //
        .andThen(xmlNormalizer);
    String output = operator.apply(text);
    System.out.println();
    System.out.println(text);
    System.out.println(output);

    Assert.assertEquals(expected, output);
  }

  @Test
  public void test01() {
    doTest( //
        "<p>just a simple text</p>", //
        "<p>just a simple text</p>" //
    );
  }

  // recognition
  @Test
  public void test02() {
    doTest( //
        "<p>Names: <name>name</name>, <NAME>name</NAME>.</p>", //
        "<p>Names: <name>name</name>, name.</p>" //
    );
  }

  // backward recognition
  @Test
  public void test03() {
    doTest( //
        "<p>Names: <NAME>name</NAME>, <name>name</name>.</p>", //
        "<p>Names: name, <name>name</name>.</p>" //
    );
  }

  // simple identification
  @Test
  public void test04() {
    doTest( //
        "<p>Names: <name key=\"x\">name</name>, <name key=\"x\" " + RESP + ">name</name>.</p>", //
        "<p>Names: <name key=\"x\">name</name>, name.</p>" //
    );
  }

  // complex identification
  @Test
  public void testComplexOneType() {
    doTest( //
        "<p>Names: <name key=\"x\">name</name>, <name key=\"y\">name</name>, <name keys=\"x|0.500 y|0.500\" " + RESP + ">name</name>.</p>", //
        "<p>Names: <name key=\"x\">name</name>, <name key=\"y\">name</name>, name.</p>" //
    );
  }

  @Test
  public void testComplexTwoTypes() {
    doTest( //
        "<p>Names: <persName key=\"x\">name</persName>, <placeName key=\"y\">name</placeName>, <NAME keys=\"persName|x|0.500 placeName|y|0.500\">name</NAME>.</p>", //
        "<p>Names: <persName key=\"x\">name</persName>, <placeName key=\"y\">name</placeName>, name.</p>" //
    );
  }

  // diacritics in input
  @Test
  public void test06() {
    doTest( //
        "<p>Names: <name>nàmë</name>, <NAME>name</NAME>.</p>", //
        "<p>Names: <name>nàmë</name>, name.</p>" //
    );
  }

  // diacritics in target
  @Test
  public void test07() {
    doTest( //
        "<p>Names: <name>name</name>, <NAME>nàmë</NAME>.</p>", //
        "<p>Names: <name>name</name>, nàmë.</p>" //
    );
  }

  // simple name link
  @Test
  public void test08() {
    doTest( //
        "<p><name>l'Name</name>, <NAME>l'Name</NAME>.</p>", //
        "<p><name>l'Name</name>, l'Name.</p>" //
    );
  }

  // uppercase in input
  @Test
  public void test09() {
    doTest( //
        "<p><name>L'Name</name>, <NAME>l'Name</NAME>.</p>", //
        "<p><name>L'Name</name>, l'Name.</p>" //
    );
  }

  // uppercase in target
  @Test
  public void test10() {
    doTest( //
        "<p><name>l'Name</name>, <NAME>L'Name</NAME>.</p>", //
        "<p><name>l'Name</name>, L'Name.</p>" //
    );
  }

  @Test
  public void test11() {
    doTest( //
        "<p lang=\"nl\"><name>Mons</name>, <NAME>Mons</NAME>.</p>", //
        "<p lang=\"nl\"><name>Mons</name>, Mons.</p>" //
    );
  }

  @Test
  public void test12() {
    doTest( //
        "<p lang=\"fr\"><name>Mons</name>, Mons.</p>", //
        "<p lang=\"fr\"><name>Mons</name>, Mons.</p>" //
    );
  }

}
