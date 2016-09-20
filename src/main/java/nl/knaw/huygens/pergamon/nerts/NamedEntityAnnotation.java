package nl.knaw.huygens.pergamon.nerts;

public class NamedEntityAnnotation {

  private String name;
  private String type;
  private String key;
  private String resp;
  private String refXmlId;
  private int beginPos;
  private int endPos;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getResp() {
    return resp;
  }

  public void setResp(String resp) {
    this.resp = resp;
  }

  public String getRefXmlId() {
    return refXmlId;
  }

  public void setRefXmlId(String refXmlId) {
    this.refXmlId = refXmlId;
  }

  public int getBeginPos() {
    return beginPos;
  }

  public void setBeginPos(int beginPos) {
    this.beginPos = beginPos;
  }

  public int getEndPos() {
    return endPos;
  }

  public void setEndPos(int endPos) {
    this.endPos = endPos;
  }

  private static final String FORMAT = "|%s|;|%d|;|%d|;|%s|;|%s|;|%s|;|%s|".replace('|', '"');

  @Override
  public String toString() {
    return String.format(FORMAT, refXmlId, beginPos, endPos, type, key, resp, name);
  }

}
