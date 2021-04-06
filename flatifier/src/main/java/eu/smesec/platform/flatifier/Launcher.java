package eu.smesec.platform.flatifier;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides command line access for the {@link Flatifier}.
 *
 * @author Matthias Luppi
 */
public class Launcher {

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.exit(1); // return a non-zero exit-code upon failure
        }
    }

    static void launch(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Missing arguments. Usage: java -jar flatifier.jar <inputDirectory> <outputFile>");
            throw new IllegalArgumentException();
        }
        final Path inputDirectory = Paths.get(args[0]);
        final Path outputFile = Paths.get(args[1]);
        final Flatifier flatifier = new Flatifier(inputDirectory, outputFile);
        try {
            flatifier.flatify();
        } catch (Exception e) {
            System.err.println("Exception while flatifying: " + e.getMessage());
            throw new IllegalArgumentException();
        }
    }

}
