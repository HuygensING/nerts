package nl.knaw.huygens.pergamon.nerts;

/**
 * Represents a named entity.
 */
public class NamedEntity implements Comparable<NamedEntity> {

  private final String name;
  private final String type;
  private final String id;

  private final String key;

  public NamedEntity(String name, String type, String id) {
    this.name = name;
    this.type = type;
    this.id = id;
    key = name + "|" + type + "|" + id;
  }

  public String getName() {
    return name;
  }

  public String getType() {
    return type;
  }

  public String getId() {
    return id;
  }

  @Override
  public int compareTo(NamedEntity other) {
    int result = name.compareTo(other.name);
    if (result == 0) {
      result = type.compareTo(other.type);
    }
    if (result == 0) {
      result = id.compareTo(other.id);
    }
    return result;
  }

  @Override
  public boolean equals(Object other) {
    return (other instanceof NamedEntity) ? key.equals(((NamedEntity) other).key) : false;
  }

  @Override
  public int hashCode() {
    return key.hashCode();
  }

}
