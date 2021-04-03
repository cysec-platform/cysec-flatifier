# CYSEC Flatifier

This tool integrates coach resources (e.g. libraries and media files) as base64-encoded content and generates a flattened XML file.

## Usage

### Flatifier JAR

```
java -jar flatifier.jar INPUTDIRECTORY OUTPUTFILE
```

| Argument          | Required  | Description  |
|-------------------|-----------|--------------|
| `INPUTDIRECTORY`  | yes       | Path to the folder containing the coach and its resources |
| `OUTPUTFILE`      | yes       | Destination of the flattened XML file |

The JAR returns a non-zero exit-code upon failure.

*Example:*
```
java -jar flatifier.jar /path/to/input/coach-xy /path/to/output/coach-xy-flat.xml
```

### Flatifier Maven Plugin

The plugin provides the `flatify` goal which binds to the `package` phase by default.

| Parameter        | Required  | Description  | Default value  |
|------------------|-----------|--------------|----------------|
| `inputDirectory` | no        | Path to the folder containing the coach and its resources | `${project.build.directory}/flatify-input` |
| `outputFile`     | no        | Destination of the flattened XML file | `${project.build.directory}/${project.artifactId}.xml` |

It is suggested to use a dedicated input directory (see the default above) where all the resources are placed before running the plugin.
This directory can be configured as an output directory for other build stages, e.g. `maven-assembly-plugin` when building a JAR file.
Other resources can be copied there easily with `maven-resources-plugin:copy-resources`.

**Usage in the POM:**
```
<build>
    <plugins>
        <!-- ... -->
        <plugin>
            <groupId>eu.smesec.platform</groupId>
            <artifactId>flatifier-maven-plugin</artifactId>
            <version>0.3.0</version>
            <executions>
                <execution>
                    <id>flatify-coach</id>
                    <goals>
                        <goal>flatify</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <inputDirectory>path/to/input/coach-xy/</inputDirectory>
                <outputFile>path/to/output/coach-xy-flat.xml</outputFile>
            </configuration>
        </plugin>
    </plugins>
</build>
```
