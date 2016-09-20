package nl.knaw.huygens.pergamon.nerts;

import java.io.File;
import java.util.function.Function;

import nl.knaw.huygens.pergamon.nerts.tool.TeiAnnotator;
import nl.knaw.huygens.pergamon.support.process.Pipeline;
import nl.knaw.huygens.pergamon.support.process.Task;

/**
 * Annotates volumes 11 and 12 of the Bayle correspondence,
 * using a gazetteer extracted from the correspondence itself.
 * No language identification is used.
 */
public class Benchmark implements Task {

  public static void main(String[] args) throws Exception {
    Pipeline.execute(new Benchmark("persName", "placeName"));
  }

  private static final int MIN_COUNT = 3;

  private final String[] elementNames;
  private final Gazetteer gazetteer;
  private TextAnnotator textAnnotator;

  public Benchmark(String... elementNames) throws Exception {
    this.elementNames = elementNames;
    gazetteer = getGazetteer(MIN_COUNT);
    textAnnotator = getTextAnnotator();
  }

  @Override
  public String getDescription() {
    return "Annotate letters from Bayle corpus";
  }

  @Override
  public void call() throws Exception {
    new CorpusFileTask(getOperator(), new File("data/bayle.txt"), new File("data/bayle_annotated.txt")).call();
  }

  private Gazetteer getGazetteer(int minCount) throws Exception {
    NamedEntities entities = new NamedEntities().withDefaultNameNormalizer();
    entities.readFromFile(getNamedEntitiesFile());
    return entities.buildGazetteer().buildMatcher(minCount);
  }

  private File getNamedEntitiesFile() {
    return new File("data/gazetteer.txt");
  }

  private TextAnnotator getTextAnnotator() {
    return new TextAnnotator().withGeneralExceptions( //
        "Bon", "Clef", "Dauphine", "Esprit", "Francois", "François", "italien", "Italien", //
        "Jamais", "Mad", "Mad.", "Mandés", "Mons", "Peres", "Pont", "prest", "Reine" //
    );
  }

  private Function<String, String> getOperator() {
    return new TeiAnnotator(elementNames) //
        .withGazetteer(gazetteer) //
        .withTeiAnnotatorName("nerts") //
        .withTextAnnotator(textAnnotator)
        // Apply CKCC formatting to the TEI
        .andThen(xml -> CKCCExportVisitor.export(xml));
  }

}
