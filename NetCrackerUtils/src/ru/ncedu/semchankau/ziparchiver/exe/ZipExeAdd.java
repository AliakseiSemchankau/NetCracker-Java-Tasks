package ru.ncedu.semchankau.ziparchiver.exe;

import ru.ncedu.semchankau.archivecomparator.ZipException;
import ru.ncedu.semchankau.ziparchiver.ZipArchiver;
import ru.ncedu.semchankau.ziparchiver.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Aliaksei Semchankau on 25.08.2015.
 * This command adds list of files to existing archive
 * format: add, archiveName, String... fileNames
 */
public class ZipExeAdd extends ZipExe{


    @Override
    public void exe(final String[] argc) throws IOException, ZipException {

        if (argc.length < 3) {
            throw new ZipException("not enough arguments for add");
        }

        String archiveAddress = argc[1];
        String[] files = new String[argc.length - 2];
        for (int i = 2; i < argc.length ; ++i) {
            files[i - 2] = argc[i];
        }

        ZipArchiver zipArchiver =  new ZipArchiver();

        String[] unPackArgc = new String[3];
        unPackArgc[0] = "unpack";
        unPackArgc[1] = archiveAddress;
        unPackArgc[2] = "tmpFile";

        System.out.println(Arrays.toString(unPackArgc));

        zipArchiver.execute(unPackArgc);
       // if (true) {
        //    return;
       // }

        Utils.deleteFile(archiveAddress);

        File temporaryFile = new File("tmpFile");

        String[] packArgc = new String[files.length + temporaryFile.listFiles().length + 2];
        packArgc[0] = "pack";
        packArgc[1] = archiveAddress;
        //packArgc[2] = "tmpFile";
        for (int i = 0; i < files.length; ++i) {
            packArgc[i + 2] = files[i];
        }
        for (int j = 0; j < temporaryFile.listFiles().length; ++j) {
            packArgc[files.length + j + 2] = temporaryFile.listFiles()[j].getAbsolutePath();
        }

        System.out.println(Arrays.toString(packArgc));

        zipArchiver.execute(packArgc);
    }
}
