package eu.smesec.platform.flatifier.maven;

import eu.smesec.platform.flatifier.Flatifier;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;

/**
 * Mojo providing the 'flatify' goal.
 *
 * @author Matthias Luppi
 */
@Mojo(name = "flatify", defaultPhase = LifecyclePhase.PACKAGE)
public class FlatifierMojo extends AbstractMojo {

    /**
     * Path to the folder containing the coach and its resources
     */
    @Parameter(property = "inputDirectory", defaultValue = "${project.build.directory}/flatify-input")
    private File inputDirectory;

    /**
     * Destination of the flattened XML file
     */
    @Parameter(property = "outputFile", defaultValue = "${project.build.directory}/${project.artifactId}.xml")
    private File outputFile;

    /**
     * Run flatifier
     *
     * @throws MojoExecutionException if an unexpected problem occurs (results in a "BUILD ERROR")
     */
    @Override
    public void execute() throws MojoExecutionException {
        if (inputDirectory == null) {
            throw new MojoExecutionException("Parameter 'inputDirectory' is not defined");
        }
        if (outputFile == null) {
            throw new MojoExecutionException("Parameter 'outputFile' is not defined");
        }

        final Flatifier flatifier = new Flatifier(inputDirectory.toPath(), outputFile.toPath());
        try {
            flatifier.flatify();
        } catch (Exception e) {
            throw new MojoExecutionException("Problem while flatifying", e);
        }
    }

}
