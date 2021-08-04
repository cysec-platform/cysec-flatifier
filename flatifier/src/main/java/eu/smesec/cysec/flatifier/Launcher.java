/*-
 * #%L
 * Flatifier Executable JAR
 * %%
 * Copyright (C) 2020 - 2021 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package eu.smesec.cysec.flatifier;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides command line access for the {@link Flatifier}.
 *
 * @author Matthias Luppi
 */
public class Launcher {

    public static void main(String[] args) {
        try {
            launch(args);
        } catch (Exception e) {
            System.exit(1); // return a non-zero exit-code upon failure
        }
    }

    static void launch(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("Missing arguments. Usage: java -jar flatifier.jar <inputDirectory> <outputFile>");
            throw new IllegalArgumentException();
        }
        final Path inputDirectory = Paths.get(args[0]);
        final Path outputFile = Paths.get(args[1]);
        final Flatifier flatifier = new Flatifier(inputDirectory, outputFile);
        try {
            flatifier.flatify();
        } catch (Exception e) {
            System.err.println("Exception while flatifying: " + e.getMessage());
            throw new IllegalArgumentException();
        }
    }

}
