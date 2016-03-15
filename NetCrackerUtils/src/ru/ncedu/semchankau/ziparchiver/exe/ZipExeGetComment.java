package ru.ncedu.semchankau.ziparchiver.exe;

import ru.ncedu.semchankau.archivecomparator.ZipException;

import java.io.IOException;
import java.util.zip.ZipFile;

/**
 * Created by Aliaksei Semchankau on 30.08.2015.
 * gets comment
 * format: getcomment, archiveName
 */

public class ZipExeGetComment extends ZipExe {
    @Override
    public void exe(String[] argc) throws IOException, ZipException {

        if (argc.length < 2) {
            throw new ZipException("not enough arguments for getcomment");
        }

        String archiveAdress = argc[1];
        ZipFile zipFile = new ZipFile(archiveAdress);
        System.out.println(zipFile.getComment());

    }
}
