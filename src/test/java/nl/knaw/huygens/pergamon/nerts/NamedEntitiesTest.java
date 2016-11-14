package nl.knaw.huygens.pergamon.nerts;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.Lists;

public class NamedEntitiesTest {

  @Test(expected = NullPointerException.class)
  public void addNullNameNormalizerThrowException() {
    new NamedEntities().withNameNormalizer(null);
  }

  @Test(expected = NullPointerException.class)
  public void addNullNameGeneratorThrowException() {
    new NamedEntities().withNameGenerator(null);
  }

  @Test
  public void testNameGenerator() {
    NamedEntities entities = new NamedEntities();
    // initial size
    Assert.assertEquals(0, entities.size());
    // add name using default name generator
    entities.add("text1", "type1", "id1", 1);
    Assert.assertEquals(1, entities.size());
    // add name using duplicating name generator
    entities.withNameGenerator(name -> Lists.newArrayList(name, name + "x"));
    entities.add("text2", "type2", "id2", 1);
    Assert.assertEquals(3, entities.size());
    // add same name again
    entities.add("text2", "type2", "id2", 1);
    Assert.assertEquals(3, entities.size());
  }

}
