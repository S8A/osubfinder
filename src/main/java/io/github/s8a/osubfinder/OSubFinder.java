package io.github.s8a.osubfinder;


import java.util.zip.GZIPInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.io.InputStream;


public class OSubFinder {

    /**
     * Prints the results of a OpenSubtitles search to the console.
     */
    private static void printFoundSubtitles(HashMap<String, String> results) {
        for (String id : results.keys()) {
            System.out.println(id + "\t" + results.get(id));
        }
    }

    private static void decodeSubtitle(String subtitle, File output) 
            throws IOException {
        byte[] base64decoded = Base64.getDecoder().decode(subtitle);
    }

    private static void downloadSubtitles() {}

    public static void main(String[] args) {
        // WORK IN PROGRESS
    }
}