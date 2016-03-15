package ru.ncedu.semchankau.filesearcher;

import java.io.File;

/**
 * Created by Aliaksei Semchankau on 04.08.2015.
 */
public class FileSearcherApp {

    public static void main(final String... argc) {

        FileSearcher fileSearcher = new FileSearcher();

        while(!fileSearcher.mustExit()) {
            fileSearcher.readCommand();
        }

    }

}
