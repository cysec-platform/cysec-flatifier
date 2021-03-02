package flatifier;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.stream.Stream;

/**
 * This class takes over the manual labour of copy pasting the base64 encoded content of coach resources
 * such as libraries and media files (images, videos) into the coach xml file.
 * The first argument is an absolute path to the folder containing the target coach and all its required files.
 * The second argument is the destination of the flattened xml file.
 * You must provide both arguments, e.g: java -jar flatifier.jar /home/nic/fhnw/ /home/nic/fhnw_flat.xml
 * Currently it is not possible to flatify a series of coach files, however, one can use a shellscript loop
 * to apply the jar file to a list of directories.
 */
public class Flatifier {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length != 2) {
            System.err.println("Usage: java Flatifier.jar <directory> <outputname>");
            throw new IllegalArgumentException("Missing arguments");
        }

        Path dirPath = Paths.get(args[0]).toAbsolutePath();
        Path output = Paths.get(args[1]);
        // Make sure parent folder exists
        if(Files.notExists(output.getParent())) {
            System.err.println(String.format("Output directory doesn't exist", output.toString()));
            throw new IllegalArgumentException("Invalid output directory");
        }
        // Make sure destination is file
        if(Files.isDirectory(output)) {
            System.err.println(String.format("Output destination must be a file", output.toString()));
            throw new IllegalArgumentException("Invalid output destination");
        }

        System.out.println("Flatifiying directory " + dirPath.toString());

        File directory = new File(dirPath.toString());
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            System.err.println("Directory is empty or doesn't exist");
            throw new FileNotFoundException("No such directory");
        }

        // Find xml file in given folder
        Stream<File> fileStream = Arrays.stream(files);
        //TODO: Deal with subquestionnaires
        File xmlFile = fileStream.filter(file -> file.getName().endsWith(".xml")).findFirst().orElseThrow(FileNotFoundException::new);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile.getAbsolutePath());
        //doc.getDocumentElement().normalize();

        // Find files of all content nodes and fill them with the base64 encoded content
        NodeList nodeList = doc.getElementsByTagName("content");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            String contentFileName = n.getAttributes().getNamedItem("filename").getNodeValue();
            // throws FileNotFound if referenced file is not within the folder passed via args[0]
            File content = new File(Paths.get(dirPath.toString(), contentFileName).toString());
            byte[] unencodedContent = Files.readAllBytes(content.toPath());
            String b64encoded = new String(Base64.getEncoder().encode(unencodedContent), StandardCharsets.UTF_8);
            // TODO: Why is output [content: null] ?
            System.out.println("## including " + b64encoded.length() + " bytes (B64 encoded) from file " + content.toPath() + " into attribute node " + n.toString());
            n.setTextContent(b64encoded);
        }

        // Insert base64 encoded libraries into xml
        nodeList = doc.getElementsByTagName("library");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node n = nodeList.item(i);
            String[] libraryFQDN = n.getAttributes().getNamedItem("id").getNodeValue().split("\\.");
            String libraryName = libraryFQDN[libraryFQDN.length - 1] + ".jar";
            // throws FileNotFound if referenced file is not within the folder passed via args[0]
            File content = new File(Paths.get(dirPath.toString(), libraryName).toString());
            byte[] unencodedContent = Files.readAllBytes(content.toPath());
            String b64encoded = new String(Base64.getEncoder().encode(unencodedContent), StandardCharsets.UTF_8);
            // TODO: Why is output [content: null] ?
            System.out.println("## including " + b64encoded.length() + " bytes (B64 encoded) from file " + content.toPath() + " into library node " + n.toString());
            n.setTextContent(b64encoded);
        }

        // preparation
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(output.toFile());
        // create the flatified xml
        transformer.transform(source, result);
        System.out.println(String.format("# Wrote all files to output file %s", output.toString()));

    }
}
