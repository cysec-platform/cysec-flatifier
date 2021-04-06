package eu.smesec.platform.flatifier;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertThrows;

/**
 * Test for {@link Flatifier}.
 *
 * @author Matthias Luppi
 */
public class FlatifierTest {

    public static final Path INPUT_DIR_VALID = Paths.get("src", "test", "resources", "fhnw");
    public static final Path OUT_FILE_VALID = Paths.get("target", "test-output", "flatifier-test-fhnw-flat.xml");
    public static final Path OUT_FILE_NOEXISTS = Paths.get("target", "test-output", "noexists", "flatifier-test-fhnw-flat.xml");

    @Test
    public void testNoArgs() {
        final Flatifier flatifier = new Flatifier(null, null);
        assertThrows(IllegalArgumentException.class, flatifier::flatify);
    }

    @Test
    public void testOutputNotFile() {
        final Flatifier flatifier = new Flatifier(INPUT_DIR_VALID, INPUT_DIR_VALID);
        assertThrows(IllegalArgumentException.class, flatifier::flatify);
    }

    @Test
    public void testOutputDoesntExist() {
        final Flatifier flatifier = new Flatifier(INPUT_DIR_VALID, OUT_FILE_NOEXISTS);
        assertThrows(IllegalArgumentException.class, flatifier::flatify);
    }

    @Test(expected = Test.None.class)
    public void testFlatification() throws Exception {
        try {
            Files.createDirectories(OUT_FILE_VALID.getParent());
        } catch (IOException e) {
            throw new IllegalStateException("Could not prepare test", e);
        }
        final Flatifier flatifier = new Flatifier(INPUT_DIR_VALID, OUT_FILE_VALID);
        flatifier.flatify();
    }

}
