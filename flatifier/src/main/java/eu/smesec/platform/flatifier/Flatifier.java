package eu.smesec.platform.flatifier;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.stream.Stream;

/**
 * Integrates coach resources (e.g. libraries and media files) as base64-encoded content and generates a flattened XML file.
 */
public class Flatifier {

    /**
     * Path to the folder containing the coach and its resources
     */
    private final Path inputDirectory;

    /**
     * Destination of the flattened XML file
     */
    private final Path outputFile;

    /**
     * Path to the folder containing files to use if not found in 'inputDirectory'
     */
    private final Path alternativeInputDirectory;

    public Flatifier(final Path inputDirectory, final Path outputFile) {
        this.inputDirectory = inputDirectory;
        this.outputFile = outputFile;
        this.alternativeInputDirectory = null;
    }

    public Flatifier(final Path inputDirectory, final Path outputFile, final Path overrideInputDirectory) {
        this.inputDirectory = inputDirectory;
        this.outputFile = outputFile;
        this.alternativeInputDirectory = overrideInputDirectory;
    }

    public void flatify() throws IOException, SAXException, ParserConfigurationException, TransformerException {
        if (outputFile == null) {
            throw new IllegalArgumentException("Invalid output file");
        }
        if (Files.isDirectory(outputFile)) {
            throw new IllegalArgumentException("Output file is a directory");
        }
        if (Files.notExists(outputFile.getParent())) {
            // TODO: consider creating output directory
            throw new IllegalArgumentException("Directory for output file does not exist");
        }

        System.out.println("Flatifiying directory " + inputDirectory);
        if (alternativeInputDirectory != null) {
            System.out.println("Alternate directory is " + alternativeInputDirectory);
        }

        final Stream<Path> pathStream = Files.list(inputDirectory);
        final Path xmlFile = pathStream
                .filter(path -> path.getFileName().toString().endsWith(".xml"))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No XML found in input directory"));
        pathStream.close();
        // TODO: Deal with subquestionnaires

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        // TODO: Security -> Disable access to external entities in XML parsing if possible
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile.toFile().getAbsolutePath());
        //doc.getDocumentElement().normalize();

        // Find files of all content nodes and fill them with the base64 encoded content
        NodeList nodeList = doc.getElementsByTagName("content");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            String contentFileName = n.getAttributes().getNamedItem("filename").getNodeValue();
            final String attributeId = n.getParentNode().getAttributes().getNamedItem("id").getTextContent();
            Path content = getFile(contentFileName);
            String b64encoded = encodeFileToBase64(content);
            System.out.println("## Including " + b64encoded.length() + " bytes (encoded) for attribute '" + attributeId + "' from file " + content + " ");
            n.setTextContent(b64encoded);
        }

        // Insert base64 encoded libraries into xml
        nodeList = doc.getElementsByTagName("library");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            final String libraryId = n.getAttributes().getNamedItem("id").getNodeValue();
            String[] libraryFQDN = libraryId.split("\\.");
            String libraryName = libraryFQDN[libraryFQDN.length - 1] + ".jar";
            Path content = getFile(libraryName);
            String b64encoded = encodeFileToBase64(content);
            System.out.println("## Including " + b64encoded.length() + " bytes (encoded) for library '" + libraryId + "' from file " + content + " ");
            n.setTextContent(b64encoded);
        }

        // preparation
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        // TODO: Security -> Disable access to external entities in XML parsing if possible
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        // create the flatified XML
        StreamResult result = new StreamResult(outputFile.toFile());
        transformer.transform(source, result);
        System.out.println("# All files written to output file '" + outputFile + "'");
    }

    private Path getFile(String fileName) throws NoSuchFileException {
        Path path = Paths.get(inputDirectory.toString(), fileName);
        if (Files.notExists(path) && (alternativeInputDirectory != null)) {
            System.out.println("## Using alternative directory for '" + fileName + "'");
            path = Paths.get(alternativeInputDirectory.toString(), fileName);
        }
        if (Files.notExists(path)) {
            throw new NoSuchFileException(fileName);
        }
        return path;
    }

    /**
     * Reads and encodes the content of a file as base64
     */
    private static String encodeFileToBase64(final Path filePath) throws IOException {
        byte[] unencodedContent = Files.readAllBytes(filePath);
        return new String(Base64.getEncoder().encode(unencodedContent), StandardCharsets.UTF_8);
    }
}
