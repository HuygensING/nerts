package nl.knaw.huygens.pergamon.support.file;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;

public class Files {

  /**
   * Write a standard header to CSV file.
   */
  public static void writeCsvHeader(Writer out, String description, String lineSeparator) throws IOException {
    StringBuilder builder = new StringBuilder();
    builder.append("--").append(lineSeparator);
    builder.append("-- ").append(description).append(lineSeparator);
    builder.append("-- ").append(new Date()).append(lineSeparator);
    builder.append("--").append(lineSeparator);
    out.write(builder.toString());
  }

  private Files() {
    throw new AssertionError("Non-instantiable class");
  }

}
