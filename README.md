# CYSEC Flatifier

This tool integrates coach resources (e.g. libraries and media files) as base64-encoded content and generates a flattened XML file.

## Usage

### Flatifier Library
Can be used as dependency to use the flatifying functionality programmatically.

*Example for Maven:*
```xml
<dependency>
    <groupId>eu.smesec.cysec</groupId>
    <artifactId>lib-flatifier</artifactId>
    <version>x.x.x</version>
</dependency>
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

The JAR returns a non-zero exit-code upon failure.

*Example:*
```bash
java -jar flatifier.jar /path/to/input/coach-xy /path/to/output/coach-xy-flat.xml
```


## License
This project is licensed under the Apache 2.0 license, see [LICENSE](LICENSE).
