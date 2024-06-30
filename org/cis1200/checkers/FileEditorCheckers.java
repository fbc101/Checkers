package org.cis1200.checkers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class FileEditorCheckers implements Iterator<String> {

    private final BufferedReader r;
    private String currLine;

    /**
     * <p>
     * If an IOException is thrown by the BufferedReader, then hasNext should
     * return false.
     * <p>
     * @param reader - A reader to be turned to an Iterator
     * @throws IllegalArgumentException if reader is null
     */
    public FileEditorCheckers(BufferedReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException();
        }
        this.r = reader;
        try {
            this.currLine = r.readLine();
        } catch (IOException e) {
            this.currLine = "";
        }
    }

    /**
     * Creates a FileLineIterator from a provided filePath by creating a
     * FileReader and BufferedReader for the file.
     * 
     * @param filePath - a string representing the file
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public FileEditorCheckers(String filePath) {
        this(fileToReader(filePath));
    }

    /**
     * Takes in a filename and creates a BufferedReader.
     * See Java's documentation for BufferedReader to learn how to construct one
     * given a path to a file.
     *
     * @param filePath - the path to the CSV file to be turned to a
     *                 BufferedReader
     * @return a BufferedReader of the provided file contents
     * @throws IllegalArgumentException if filePath is null or if the file
     *                                  doesn't exist
     */
    public static BufferedReader fileToReader(String filePath) {
        if (filePath == null) {
            throw new IllegalArgumentException();
        }
        Reader file;
        try {
            file = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException();
        }
        return new BufferedReader(file);
    }

    /**
     * Returns true if there are lines left to read in the file, and false
     * otherwise.
     * <p>
     * If there are no more lines left, this method should close the
     * BufferedReader.
     *
     * @return a boolean indicating whether the FileLineIterator can produce
     *         another line from the file
     */
    @Override
    public boolean hasNext() {
        try {
            if (this.currLine == null) {
                r.close();
                return false;
            } else {
                return true;
            }
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Returns the next line from the file, or throws a NoSuchElementException
     * if there are no more strings left to return (i.e. hasNext() is false).
     * <p>
     * This method also advances the iterator in preparation for another
     * invocation. If an IOException is thrown during a next() call, your
     * iterator should make note of this such that future calls of hasNext()
     * will return false and future calls of next() will throw a
     * NoSuchElementException
     *
     * @return the next line in the file
     * @throws java.util.NoSuchElementException if there is no more data in the
     *                                          file
     */
    @Override
    public String next() {
        try {
            if (hasNext()) {
                String copy = this.currLine;
                this.currLine = r.readLine();
                return copy;
            } else {
                throw new NoSuchElementException();
            }
        } catch (IOException e) {
            this.currLine = "";
            throw new NoSuchElementException();
        }
    }

    /**
     * Writes the states to the file containing all the checkers game state information
     * @param stringsToWrite - A List of Strings to write to the file
     * @param filePath       - the string containing the path to the file where
     *                       the tweets should be written
     * @param append         - a boolean indicating whether the new tweets
     *                       should be appended to the current file or should
     *                       overwrite its previous contents
     */
    public void writeStateToFile(List<String> stringsToWrite, String filePath,
            boolean append) {
        File file = Paths.get(filePath).toFile();
        BufferedWriter bw;
        FileWriter fw;
        try {
            fw = new FileWriter(file.getPath(), append);
            bw = new BufferedWriter(fw);
            for (String sentence : stringsToWrite) {
                bw.write(sentence + "\n");
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    /*
     * turns a string representing a row of the checkers board
     * into an array of ints representing the row
     */
    static int[] extractBoardRow(String csvLine) {
        if (csvLine == null) {
            return null;
        } 
        String[] a = csvLine.split("\t");
        int[] copy = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            copy[i] = Integer.valueOf(a[i]);
        }
        return copy;
    }

    /*
     * turns an array of integers into a string representing
     * a row of the checkers board
     */
    static String turnRowToString(int[] row) {
        String copy = "";
        for (int i = 0; i < row.length; i++) {
            copy += row[i] + "\t";
        }
        return copy;
    }
}
