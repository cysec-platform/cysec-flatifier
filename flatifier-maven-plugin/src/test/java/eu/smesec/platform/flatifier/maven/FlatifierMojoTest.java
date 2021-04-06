package eu.smesec.platform.flatifier.maven;

import org.apache.maven.plugin.testing.MojoRule;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test the 'flatify' goal of the {@link FlatifierMojo}.
 *
 * @author Matthias Luppi
 */
public class FlatifierMojoTest {

    @Rule
    public MojoRule rule = new MojoRule();

    @Test
    public void testFlatify() throws Exception {
        Files.createDirectories(Paths.get("target", "mojo-test-output"));

        final String pom = "src/test/resources/testproject/pom.xml";
        final File pomFile= new File(pom);
        assertNotNull(pomFile);
        assertTrue(pomFile.exists());

        final FlatifierMojo flatifierMojo = (FlatifierMojo) rule.lookupMojo("flatify", pom);
        assertNotNull(flatifierMojo);
        flatifierMojo.execute();
    }

}
