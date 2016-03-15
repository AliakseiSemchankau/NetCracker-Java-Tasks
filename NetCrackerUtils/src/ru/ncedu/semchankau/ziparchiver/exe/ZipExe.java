package ru.ncedu.semchankau.ziparchiver.exe;

import ru.ncedu.semchankau.archivecomparator.ZipException;

import java.io.IOException;

/**
 * Created by Aliaksei Semchankau on 05.08.2015.
 * abstract class for all zip commands
 */
public abstract class ZipExe {

    public abstract void exe(final String[] argc) throws IOException, ZipException;

}
