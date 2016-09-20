package nl.knaw.huygens.pergamon.nerts.text;

import java.util.function.Function;

/**
 * Maps characters for named entity recoginition:
 * replaces '-' by ' '
 * replaces ':' by '.'
 * groups characters as follows [bp], [ijy], [uvw]
 * leaves character case as is
 */
public class NERTextNormalizer implements Function<String, String> {

  @Override
  public String apply(String text) {
    char[] chars = text.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      char ch = chars[i];
      switch (ch) {
      case '-':
        chars[i] = ' ';
        break;
      case ':':
        chars[i] = '.';
        break;
      case 'j':
        chars[i] = 'i';
        break;
      case 'J':
        chars[i] = 'I';
        break;
      case 'p': // lowercase mapping only
        chars[i] = 'b';
        break;
      case 'v':
        chars[i] = 'u';
        break;
      case 'V':
        chars[i] = 'U';
        break;
      case 'w':
        chars[i] = 'u';
        break;
      case 'W':
        chars[i] = 'U';
        break;
      case 'y':
        chars[i] = 'i';
        break;
      case 'Y':
        chars[i] = 'I';
        break;
      default:
        break;
      }
    }
    return new String(chars);
  }

}
