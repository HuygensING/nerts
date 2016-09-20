package nl.knaw.huygens.pergamon.nerts;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.function.Function;

import nl.knaw.huygens.pergamon.support.process.Task;

/**
 * Handles corpus TEI files, consisting of a concatenation of TEI segments,
 * each starting with a {@code BOI} line and ending with a {@code EOI} line.
 *
 * Upon processing the input file is read in full. Each TEI segment is
 * handled by the {@code processXml} method. The result is written to
 * the output file. Sections between TEI segments are copied verbatim.
 */
public class CorpusFileTask implements Task {

  /** Marks begin of a TEI segment. */
  private static final String BOI = "<!-- BOI -->";
  /** Marks end of a TEI segment. */
  private static final String EOI = "<!-- EOI -->";
  /** Line seperator of output file. */
  private static final String SEPARATOR = "\n";

  private final Function<String, String> tranformer;
  private final File srce;
  private final File dest;

  public CorpusFileTask(Function<String, String> tranformer, File srce, File dest) {
    this.tranformer = tranformer;
    this.srce = srce;
    this.dest = dest;
  }

  @Override
  public String getDescription() {
    return getClass().getName();
  }

  @Override
  public void call() throws Exception {
    try (BufferedReader reader = createReader(srce); BufferedWriter writer = createWriter(dest)) {
      StringBuilder builder = new StringBuilder();
      String line = null;
      boolean inArtifact = false;
      while ((line = reader.readLine()) != null) {
        if (line.equals(BOI)) {
          writer.write(line);
          writeNewLine(writer);
          inArtifact = true;
        } else if (line.equals(EOI)) {
          String xml = builder.toString();
          builder.setLength(0);
          String output = tranformer.apply(xml);
          writer.write(output);
          writeNewLine(writer);
          writer.write(line);
          writeNewLine(writer);
          inArtifact = false;
        } else if (inArtifact) {
          builder.append(line);
          builder.append(SEPARATOR);
        } else {
          writer.write(line);
          writeNewLine(writer);
        }
      }
    }
  }

  private BufferedReader createReader(File file) throws FileNotFoundException, UnsupportedEncodingException {
    FileInputStream fis = new FileInputStream(file);
    InputStreamReader in = new InputStreamReader(fis, "UTF-8");
    return new BufferedReader(in);
  }

  public static BufferedWriter createWriter(File file) throws FileNotFoundException, UnsupportedEncodingException {
    FileOutputStream fos = new FileOutputStream(file);
    OutputStreamWriter out = new OutputStreamWriter(fos, "UTF-8");
    return new BufferedWriter(out);
  }

  private void writeNewLine(Writer writer) throws IOException {
    writer.write(SEPARATOR);
  }

}
