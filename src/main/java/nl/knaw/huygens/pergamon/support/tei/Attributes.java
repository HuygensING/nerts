package nl.knaw.huygens.pergamon.support.tei;

public class Attributes {

  public static final String XML_NS = "xml:";

  public static final String ATTR_ID = "id";
  public static final String ATTR_XML_ID = XML_NS + ATTR_ID;

  public static final String KEY = "key";
  public static final String KEYS = "keys";

  public static final String RESP = "resp";

  private Attributes() {
    throw new AssertionError("Non-instantiable class");
  }

}
