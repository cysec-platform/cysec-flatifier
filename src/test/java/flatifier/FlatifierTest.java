package flatifier;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FlatifierTest {

    private Flatifier flatifier;

    @Before
    public void setUp() {
        this.flatifier = new Flatifier();
    }

    @Test
    public void noArgsTest() {
        try {
            flatifier.main(null);
            fail("Didn't throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage() == "Missing arguments");
        } catch (Exception e) {
            fail("Threw another type of exception: " + e.getMessage());
        }
    }

    @Test
    public void testOutputNotFile() {
        String[] args = {"src\\main\\resources\\fhnw", "src\\main\\resources\\fhnw"};
        try {
            flatifier.main(args);
            fail("Didn't throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage() == "Invalid output destination");
        } catch (Exception e) {
            fail("Threw another type of exception: " + e.getMessage());
        }
    }

    @Test
    public void testOutputDoesntExist() {
        String[] args = {"src\\main\\resources\\fhnw", "src\\main\\resources\\doesntexist\\fhnw_flat.xml"};
        try {
            flatifier.main(args);
            fail("Didn't throw an IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage() == "Invalid output directory");
        } catch (Exception e) {
            fail("Threw another type of exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatification() {
        try {
            String[] args = {"src\\main\\resources\\fhnw", "src\\main\\resources\\fhnw\\fhnw_flat.xml"};
            flatifier.main(args);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Threw an exception: " + e.getMessage());
        }
    }

}
