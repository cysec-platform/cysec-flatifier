/*-
 * #%L
 * Flatifier Library
 * %%
 * Copyright (C) 2020 - 2022 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)
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

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link Flatifier}.
 *
 * @author Matthias Luppi
 */
public class FlatifierTest {

    private static final Path INPUT_DIR_FULL = Paths.get("src", "test", "resources", "fhnw-full");
    private static final Path INPUT_DIR_BASE_FOR_ALT = Paths.get("src", "test", "resources", "fhnw-base-for-alt");
    private static final Path OUT_FILE_VALID = Paths.get("target", "test-output", "flatifier-test-fhnw-flat.xml");
    private static final Path OUT_FILE_NOEXISTS = Paths.get("target", "test-output", "noexists", "flatifier-test-fhnw-flat.xml");

    @Before
    public void setUp() throws IOException {
        Files.deleteIfExists(OUT_FILE_VALID);
    }

    @Test
    public void testNoArgs() {
        final Flatifier flatifier = new Flatifier(null, null);
        assertThrows(IllegalArgumentException.class, flatifier::flatify);
    }

    @Test
    public void testOutputNotFile() {
        final Flatifier flatifier = new Flatifier(INPUT_DIR_FULL, INPUT_DIR_FULL);
        assertThrows(IllegalArgumentException.class, flatifier::flatify);
    }

    @Test
    public void testOutputDoesntExist() {
        final Flatifier flatifier = new Flatifier(INPUT_DIR_FULL, OUT_FILE_NOEXISTS);
        assertThrows(IllegalArgumentException.class, flatifier::flatify);
    }

    @Test
    public void testFlatification() throws Exception {
        try {
            Files.createDirectories(OUT_FILE_VALID.getParent());
        } catch (IOException e) {
            throw new IllegalStateException("Could not prepare test", e);
        }
        final Flatifier flatifier = new Flatifier(INPUT_DIR_FULL, OUT_FILE_VALID);
        flatifier.flatify();

        assertTrue(Files.exists(OUT_FILE_VALID));
    }

    @Test
    public void testFlatificationAlternativeInputDirectory() throws Exception {
        try {
            Files.createDirectories(OUT_FILE_VALID.getParent());
        } catch (IOException e) {
            throw new IllegalStateException("Could not prepare test", e);
        }
        final Flatifier flatifier = new Flatifier(INPUT_DIR_BASE_FOR_ALT, OUT_FILE_VALID, INPUT_DIR_FULL);
        flatifier.flatify();

        assertTrue(Files.exists(OUT_FILE_VALID));
    }

}
