package io.github.s8a.osubfinder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.io.File;
import java.io.InputStream;
import java.net.URL;


/**
 * Unit test for the OpenSubtitles.org hash function.
 *
 * @author Samuel Ochoa (S8A)
 */
public class OpenSubtitlesHasherTest extends TestCase {
    String hash = "8e245d9679d31e12";
    /**
     * Create the test case
     *
     * @param testName Name of the test case
     */
    public OpenSubtitlesHasherTest(String testName) {
        super(testName);
    }

    /**
     * @return The suite of tests being tested.
     */
    public static Test suite() {
        return new TestSuite(OpenSubtitlesHasherTest.class);
    }

    /**
     * Tests the OpenSubtitles hash function for input streams.
     */
    public void testStreamHasher() {
        String vid = "http://www.opensubtitles.org/addons/avi/breakdance.avi";
        long length = 12909756L;
        URL url = new URL(vid);
        InputStream input = url.openStream();
        String hasherResult = OpenSubtitlesHasher.computeHash(input, length);
        assertEquals(hasherResult, hash);
    }

    /**
     * Testes the OpenSubtitles hash function for files.
     */
    public void testFileHasher() {
        URL url = getClass().getClassLoader().getResource("breakdance.avi");
        File video = new File(url.toURI());
        String hasherResult = OpenSubtitlesHasher.computeHash(video);
        assertEquals(hasherResult, hash);
    }
}
