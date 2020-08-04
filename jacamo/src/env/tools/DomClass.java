package tools;

import org.w3c.dom.*;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;
import java.util.ArrayList;

public class DomClass {
    static ArrayList < String > listaInformacoesEntradaPoco = new ArrayList < String > ();
    static ArrayList < String > listaInformacoesSaidaPoco = new ArrayList < String > ();
    static ArrayList < String > listaInformacoesSaidaGerais = new ArrayList < String > ();
    static String nomeTagEntradaPocos = "wells";
    static String nomeTagEntradaPoco = "well";
    static String nomeTagSaidaGeral = "output";
    static String nomeTagSaidaPocos = "welloutput";

    DomClass() {
        listaInformacoesEntradaPoco.add("Status");
        listaInformacoesEntradaPoco.add("QglFixed");
        listaInformacoesEntradaPoco.add("QglMin");
        listaInformacoesEntradaPoco.add("QglMax");
        listaInformacoesEntradaPoco.add("QglRatesArray");
        listaInformacoesEntradaPoco.add("QglRatesMin");
        listaInformacoesEntradaPoco.add("QglRatesMax");
        listaInformacoesEntradaPoco.add("QglWhpRatesArray");
        listaInformacoesEntradaPoco.add("QglWhpRatesMin");
        listaInformacoesEntradaPoco.add("QglWhpRatesMax");
        listaInformacoesEntradaPoco.add("QglQoRatesMatrix");
        listaInformacoesEntradaPoco.add("PHeadFixed");
        listaInformacoesEntradaPoco.add("PHeadMin");
        listaInformacoesEntradaPoco.add("PHeadMax");
        listaInformacoesEntradaPoco.add("GOR");
        listaInformacoesEntradaPoco.add("WC");
        listaInformacoesEntradaPoco.add("Potential");
        listaInformacoesEntradaPoco.add("AllowsEmerging");
        listaInformacoesEntradaPoco.add("QglReference");
        listaInformacoesEntradaPoco.add("PHeadReference");
        listaInformacoesEntradaPoco.add("QoReference");

        listaInformacoesSaidaPoco.add("ProductionStateOptimal");
        listaInformacoesSaidaPoco.add("QglOptimal");
        listaInformacoesSaidaPoco.add("PHeadOptimal");
        listaInformacoesSaidaPoco.add("QoOptimal");
        listaInformacoesSaidaPoco.add("QgOptimal");
        listaInformacoesSaidaPoco.add("QwOptimal");

        listaInformacoesSaidaGerais.add("TotalOilRateOptimal");
        listaInformacoesSaidaGerais.add("ProducedGasOptimal");
        listaInformacoesSaidaGerais.add("ConsumedGasLiftOptimal");
        listaInformacoesSaidaGerais.add("TotalGasOptimal");
        listaInformacoesSaidaGerais.add("ProducedWaterOptimal");
        listaInformacoesSaidaGerais.add("ProducedLiquidOptimal");
        listaInformacoesSaidaGerais.add("FlareGasOptimal");
        listaInformacoesSaidaGerais.add("ExportGasRateOptimal");
        listaInformacoesSaidaGerais.add("ProductionLossOptimal");
        listaInformacoesSaidaGerais.add("EfficiencyOptimal");
        listaInformacoesSaidaGerais.add("IUGAOptimal");
        listaInformacoesSaidaGerais.add("MaxLiquidCapacity");
        listaInformacoesSaidaGerais.add("IUGAOptimal");
        listaInformacoesSaidaGerais.add("MaxWaterTreatmentCapacity");
        listaInformacoesSaidaGerais.add("CompressionCapacity");
        listaInformacoesSaidaGerais.add("MaxFlareGas");
        listaInformacoesSaidaGerais.add("FuelGas");
        listaInformacoesSaidaGerais.add("MaxExportGasRate");
    }

    public static String checkWell(String well, String file) throws SAXException, IOException, ParserConfigurationException, TransformerException {
        Document documento = criaDocument(file);
        documento.getDocumentElement().normalize();

        NodeList wList = documento.getElementsByTagName(nomeTagEntradaPoco);
        ArrayList < String > wellList = new ArrayList < String > ();
        for (int i = 0; i < wList.getLength(); i++) {
            Element element = (Element) wList.item(i);
            wellList.add(element.getAttribute("name"));
        }
        if (wellList.contains(well)) {
          return ("File " + file + " already contains well " + well + ". Updating its values...");
        } else {
            NodeList nList = documento.getElementsByTagName(nomeTagEntradaPocos);
            for (int temp = 0; temp < nList.getLength(); temp++) {
              Node node = nList.item(temp);
              if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                eElement.insertBefore(criaPoco(documento, well), node.getLastChild());
              }
            }
            geraArquivo(documento, file);
            return ("File " + file + " does not contain well " + well + " yet. Adding its values...");
          }
    }

    private static Node criaPoco(Document doc, String nome) {
        Element well = doc.createElement("well");
        well.setAttribute("name", nome);
        for (String tag: listaInformacoesEntradaPoco) {
            if (tag == "Status") {
                well.appendChild(criaPocoElement(doc, tag, "ENABLED"));
            // } else if (tag == "Potential") {
            //     well.appendChild(criaPocoElement(doc, tag, "0.0"));
            // } else if (tag == "AllowsEmerging") {
            //     well.appendChild(criaPocoElement(doc, tag, "false"));
            } else {
                well.appendChild(criaPocoElement(doc, tag, ""));
            }
        }
        return well;
    }

    private static Node criaPocoElement(Document doc, String name, String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }

    private static Document criaDocument(String arquivo) throws SAXException, IOException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(new EntityResolver() {
            @Override
            public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                if (systemId.contains("gasliftnew-v1.dtd")) {
                    return new InputSource(new StringReader("\n"));
                } else {
                    return null;
                }
            }
        });
        Document document = builder.parse(new File(System.getProperty("user.dir") + "/../marlim/" + arquivo)); // + ".gln"));
        return document;
    }

    private static void geraArquivo(Document doc, String file) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transf = transformerFactory.newTransformer();
        transf.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
        transf.setOutputProperty(OutputKeys.INDENT, "yes");
        transf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        DOMSource source = new DOMSource(doc);

        String userDir = System.getProperty("user.dir");
        File myFile = new File(userDir + "/../marlim/" + file);
        StreamResult output = new StreamResult(myFile);
        transf.transform(source, output);
    }
}
