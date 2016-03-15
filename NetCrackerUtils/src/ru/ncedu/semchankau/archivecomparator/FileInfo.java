package ru.ncedu.semchankau.archivecomparator;

/**
 * This class containg information about file
 * Created by Aliaksei Semchankau on 18.08.2015.
 */
public class FileInfo implements Comparable {

    private String fileName;
    private long fileSize;
    private long fileHash;

    /**
     * collects all information about file: its name, size and hash
     *
     * @param fileName
     * @param fileSize
     * @param fileHash
     */
    public FileInfo(final String fileName, final long fileSize, final long fileHash) {
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.fileHash = fileHash;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getFileHash() {
        return fileHash;
    }

    public void print() {
        System.out.println(fileName + " " + fileSize + " " + fileHash);
    }

    @Override
    public boolean equals(final Object anotherObject) {
        if (anotherObject == null || anotherObject.getClass() != getClass()) {
            return false;
        }

        FileInfo fileInfo = (FileInfo) anotherObject;
        return (this.fileSize == fileInfo.fileSize && this.fileHash == fileInfo.fileHash);
    }

    @Override
    public int compareTo(final Object o) {
        FileInfo anotherFile = (FileInfo) o;
        if (fileSize < anotherFile.getFileSize()) {
            return -1;
        }
        if (fileSize > anotherFile.getFileSize()) {
            return 1;
        }
        if (fileHash < anotherFile.getFileHash()) {
            return -1;
        }
        if (fileHash > anotherFile.getFileHash()) {
            return 1;
        }
        return 0;
    }

}
