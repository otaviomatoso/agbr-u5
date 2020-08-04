package tools;

import cartago.*;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

public class DomArtifact extends Artifact {

    DomClass dom;
    void init() {
        dom = new DomClass();
    }

    @OPERATION
    void checkWell(String well, String file, OpFeedbackParam<String> output) throws SAXException, IOException, ParserConfigurationException, TransformerException {
        output.set(DomClass.checkWell(well, file));
    }
}
