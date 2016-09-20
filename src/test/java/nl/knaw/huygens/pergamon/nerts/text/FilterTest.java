package nl.knaw.huygens.pergamon.nerts.text;

import java.util.Random;
import java.util.function.Function;

import org.junit.Assert;

public class FilterTest {

  protected Function<String, String> filter;

  protected void doTestLength(Function<String, String> filter) {
    // Use seed for repeatability of the test
    Random random = new Random(43L);
    int bound = Character.MAX_CODE_POINT;
    do {
      StringBuilder builder = new StringBuilder();
      for (int i = 0; i < 32; i++) {
        builder.append((char) random.nextInt(bound));
      }
      String text = builder.toString();
      Assert.assertEquals(text, text.length(), filter.apply(text).length());
      bound = bound / 2;
    } while (bound > 32);
  }

}
