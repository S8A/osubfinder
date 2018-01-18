package io.github.s8a.osubfinder;


import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;
import org.apache.xmlrpc.XmlRpcException;


public class OSubFinder {

    /**
     * Prints the results of a OpenSubtitles search to the console.
     */
    private static void printFoundSubtitles(HashMap<String, String> results) {
        for (String id : results.keySet()) {
            System.out.println(id + "\t" + results.get(id));
        }
    }

    private static void decodeSubtitle(String subtitle, File output) 
            throws IOException {
        byte[] base64decoded = Base64.getDecoder().decode(subtitle);
    }

    private static void downloadSubtitles() {}

    /** Temporary test */
    public static void main(String[] args) throws MalformedURLException {
        if (args.length != 1) {
           System.out.println("Pass video file path as argument");
           System.exit(1);
        }
        OpenSubtitlesClient osc = new OpenSubtitlesClient();
        try {
            String tkn = osc.login("", "", "en");
            String username = "";
            String password = "";
            File video = new File(args[0]);
            String hash = OpenSubtitlesHasher.computeHash(video);
            long size = video.length();
            HashMap<String, String> query = new HashMap<>();
            query.put("moviehash", hash);
            query.put("moviebytesize", Long.toString(size));
            ArrayList<HashMap<String, String>> queries = new ArrayList<>();
            queries.add(query);
            HashMap<String, String> search = osc.searchSubtitles(tkn, queries, 10);
            for (String id : search.keySet()) {
                System.out.println(id + "\t" + search.get(id));
            }
        } catch (BadStatusException bse) {
            System.out.println("Bad Status: " + bse.getMessage());
            bse.printStackTrace();
        } catch (XmlRpcException xre) {
            System.out.println("XML-RPC Exception: " + xre.getCause());
            xre.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}