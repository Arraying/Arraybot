package de.arraying.arraybot.core.misc;

import org.slf4j.Logger;

import java.io.*;
import java.util.LinkedList;

/**
 * Copyright 2017 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class Splash {

    private final File file;

    /**
     * Creates a new splash.
     * @param file The file to use.
     */
    public Splash(File file) {
        this.file = file;
    }

    /**
     * Prints the splash.
     * @param logger The logger that should print.
     */
    public void print(Logger logger, String version, String jdaVersion, String developers) {
        try {
            if(file.createNewFile()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                writer.write("SPLASH TEXT - CUSTOMISE IN " + file.getName() + "!");
                writer.close();
            }
            printBlank(logger);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while((line = bufferedReader.readLine()) != null) {
                logger.info(line, version, jdaVersion, developers);
            }
            bufferedReader.close();
            printBlank(logger);
        } catch(IOException exception) {
            logger.info("An error occurred showing the splash.");
            exception.printStackTrace();
        }
    }

    /**
     * Prints a few blank lines.
     * @param logger The logger that should print.
     */
    private void printBlank(Logger logger) {
        for(int i = 0; i < 3; i++) {
            logger.info("");
        }
    }

}
