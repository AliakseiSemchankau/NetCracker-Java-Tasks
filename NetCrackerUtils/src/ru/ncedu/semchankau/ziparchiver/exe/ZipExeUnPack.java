package ru.ncedu.semchankau.ziparchiver.exe;

import ru.ncedu.semchankau.ziparchiver.ZipException;
import ru.ncedu.semchankau.ziparchiver.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Aliaksei Semchankau on 05.08.2015.
 * this command unpacks archive into some directory
 * formant: unpack, archiveName, dirName
 */
public class ZipExeUnPack extends ZipExe {

    @Override
    public void exe(String[] argc) {

        System.out.println(Arrays.toString(argc));

        if (argc.length < 2 || argc.length > 3) {
            throw new IllegalArgumentException("incorrect argument number for unpack");
        }

        String archiveAddress = argc[1];
        File dest = new File(System.getProperty("user.dir"));

        if (argc.length == 3) {
            dest = new File(argc[2]);
        }

        try {
            ZipInputStream zis = new ZipInputStream(new FileInputStream(archiveAddress));
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                String fileName = zipEntry.getName();
                Utils.unZip(zis, fileName, dest);
            }

            zis.close();

        } catch (IOException ioExc) {
            throw new ZipException("can't open zip file " + archiveAddress);
        }

    }


}
