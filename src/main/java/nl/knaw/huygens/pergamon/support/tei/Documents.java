package nl.knaw.huygens.pergamon.support.tei;

import java.util.List;

import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Document;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Node;
import nl.knaw.huygens.tei.Text;
import nl.knaw.huygens.tei.TextHandler;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.Visitor;
import nl.knaw.huygens.tei.XmlContext;

public class Documents {

  public static Document newDocument(String xml) {
    Document document = Document.createFromXml(xml, true);
    document.accept(new ConcatenatingVisitor());
    return document;
  }

  public static <T extends Visitor> T visitXml(String xml, T visitor) {
    newDocument(xml).accept(visitor);
    return visitor;
  }

  /**
   * The current {@code VisiTEI} implementation offers text to be processed in
   * chunks, simply passing them on from the underlying SAX parser.
   * This presents an unnecessary complication to Visitor implementations.
   * Improving {@code VisiTEI} seems out-of-scope for the RSG Pilot project.
   *
   * This visitor implements some simple logic for handling the chunks:
   * it concatenes all text to the first {@code Text} node, making the other
   * ones empty, thus leaving the document tree intact.
   */
  static class ConcatenatingVisitor extends DelegatingVisitor<XmlContext> {
    public ConcatenatingVisitor() {
      super(new XmlContext());
      setTextHandler(new ConcatenatingTextHandler());
    }
  }

  static class ConcatenatingTextHandler implements TextHandler<XmlContext> {
    @Override
    public Traversal visitText(Text text, XmlContext context) {
      Element parent = text.getParent();
      if (parent == null) {
        throw new IllegalStateException("VisiTEI has internal inconsistency");
      }

      List<Node> nodes = parent.getNodes();
      int pos = nodes.indexOf(text);
      if (pos < 0) {
        throw new IllegalStateException("VisiTEI has internal inconsistency");
      }

      if (pos > 0 && nodes.get(pos - 1) instanceof Text) {
        // already handled Text node following another Text node
        text.setText("");
        return Traversal.NEXT;
      }

      // start of sequence Text nodes (possibly 1)
      StringBuilder builder = new StringBuilder();
      for (int index = pos; index < nodes.size(); index++) {
        Node node = nodes.get(index);
        if (node instanceof Text) {
          builder.append(node);
        } else {
          break;
        }
      }
      text.setText(builder.toString());
      return Traversal.NEXT;
    }
  }

}
