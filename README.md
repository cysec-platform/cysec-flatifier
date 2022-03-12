# CYSEC Flatifier

This tool integrates coach resources (e.g. libraries and media files) as base64-encoded content and generates a flattened XML file.

## Usage

### Flatifier Library

Use this library as a Maven dependency like this:

```xml
<dependency>
    <groupId>eu.smesec.cysec</groupId>
    <artifactId>lib-flatifier</artifactId>
    <version>x.x.x</version>
</dependency>
```

Here is an example on how to use the _flatify_ functionality programmatically:

```java
// define variables
Path inputDirectory = Paths.get("path", "to", "input");
Path outputFile = Paths.get("path", "to", "output", "coach-flat.xml");
Path alternativeInputDirectory = Paths.get("path", "to", "alternative-input");

// create flatifier
Flatifier flatifier = new Flatifier(inputDirectory, outputFile, alternativeInputDirectory);

// execute flatify process
flatifier.flatify();
```


### Flatifier Executable JAR

Allows to execute the flatifier independently and directly on the command line.
The use of an alternate source directory (e.g. for multilingual builds) is not supported.

```bash
java -jar flatifier-x.x.x.jar INPUTDIRECTORY OUTPUTFILE
```

| Argument          | Required  | Description  |
|-------------------|-----------|--------------|
| `INPUTDIRECTORY`  | yes       | Path to the folder containing the coach and its resources |
| `OUTPUTFILE`      | yes       | Destination of the flattened XML file |

The launcher returns a non-zero exit-code upon failure.

*Example:*
```bash
java -jar flatifier.jar /path/to/input/coach-xy /path/to/output/coach-xy-flat.xml
```


## License
This project is licensed under the Apache 2.0 license, see [LICENSE](LICENSE).
