package flatifier;

import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FlatifierTest {

    public static final String ARG_DIR_VALID = Paths.get("src", "test", "resources", "fhnw").toString();
    public static final String ARG_OUT_VALID = Paths.get("target", "test-output", "fhnw-flat.xml").toString();
    public static final String ARG_OUT_NOEXISTS = Paths.get("target", "test-output", "noexists", "fhnw-flat.xml").toString();

    @Test
    public void testNoArgs() {
        try {
            Flatifier.main(null);
            fail("Didn't throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Missing arguments", e.getMessage());
        } catch (Exception e) {
            fail("Threw another type of exception: " + e.getMessage());
        }
    }

    @Test
    public void testOutputNotFile() {
        try {
            Flatifier.main(new String[]{ARG_DIR_VALID, ARG_DIR_VALID});
            fail("Didn't throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid output destination", e.getMessage());
        } catch (Exception e) {
            fail("Threw another type of exception: " + e.getMessage());
        }
    }

    @Test
    public void testOutputDoesntExist() {
        try {
            Flatifier.main(new String[]{ARG_DIR_VALID, ARG_OUT_NOEXISTS});
            fail("Didn't throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid output directory", e.getMessage());
        } catch (Exception e) {
            fail("Threw another type of exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatification() {
        try {
            Files.createDirectories(Paths.get(ARG_OUT_VALID).getParent());
            Flatifier.main(new String[]{ARG_DIR_VALID, ARG_OUT_VALID});
        } catch (Exception e) {
            e.printStackTrace();
            fail("Threw an exception: " + e.getMessage());
        }
    }

}
