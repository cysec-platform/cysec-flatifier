# Cysec flatify tool

This tool takes over the manual labour of copy pasting the base64 encoded content of coach resources such as libraries and media files (images, videos) into the coach xml file.

## Usage

The first argument is an absolute path to the folder containing the target coach and all its required files.
The second argument is the destination of the flattened xml file.

You must provide both arguments, e.g: java -jar flatifier.jar /home/nic/fhnw/ /home/nic/fhnw_flat.xml

Currently it is not possible to flatify a series of coach files, however, one can use a shellscript loop to apply the jar file to a list of directories.

```
PS C:\Users\nicolas.odermatt\IdeaProjects\cysec-flatifier\build\libs> java -jar .\cysec-flatifier-1.0-SNAPSHOT.jar .
Flatifiying directory C:\Users\nicolas.odermatt\IdeaProjects\cysec-flatifier\build\libs\.
## including 344760 bytes (B64 encoded) from file C:\Users\nicolas.odermatt\IdeaProjects\cysec-flatifier\build\libs\.\image.jpg into attribute node [content: null]
## including 344760 bytes (B64 encoded) from file C:\Users\nicolas.odermatt\IdeaProjects\cysec-flatifier\build\libs\.\image.jpg into attribute node [content: null]
## including 4668 bytes (B64 encoded) from file C:\Users\nicolas.odermatt\IdeaProjects\cysec-flatifier\build\libs\.\FirstLibrary.jar into library node [library: null]
```
