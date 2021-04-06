package eu.smesec.platform.flatifier;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertThrows;

/**
 * Test for {@link Launcher}.
 *
 * @author Matthias Luppi
 */
public class LauncherTest {

    public static final String ARG_DIR_VALID = Paths.get("src", "test", "resources", "fhnw").toString();
    public static final String ARG_OUT_VALID = Paths.get("target", "test-output", "launcher-test-fhnw-flat.xml").toString();
    public static final String ARG_OUT_NOEXISTS = Paths.get("target", "test-output", "noexists", "launcher-test-fhnw-flat.xml").toString();

    @Test
    public void testNoArgs() {
        assertThrows(IllegalArgumentException.class, () -> Launcher.launch(null));
    }

    @Test
    public void testOutputNotFile() {
        assertThrows(IllegalArgumentException.class, () -> Launcher.launch(new String[]{ARG_DIR_VALID, ARG_DIR_VALID}));
    }

    @Test
    public void testOutputDoesntExist() {
        assertThrows(IllegalArgumentException.class, () -> Launcher.launch(new String[]{ARG_DIR_VALID, ARG_OUT_NOEXISTS}));
    }

    @Test(expected = Test.None.class)
    public void testFlatification() {
        try {
            Files.createDirectories(Paths.get(ARG_OUT_VALID).getParent());
        } catch (IOException e) {
            throw new IllegalStateException("Could not prepare test", e);
        }
        Launcher.launch(new String[]{ARG_DIR_VALID, ARG_OUT_VALID});
    }

}
