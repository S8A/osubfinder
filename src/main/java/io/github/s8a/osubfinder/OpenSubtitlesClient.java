package io.github.s8a.osubfinder;

import java.util.*;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


class BadStatusException extends Exception {}


/**
 * OpenSubtitlesClient
 * Implements some functions of the OpenSubtitles XML-RPC API.
 *
 * @author Samuel Ochoa (S8A)
 */
class OpenSubtitlesClient {

	private static final String USER_AGENT = "TemporaryUserAgent"; // temporary
	private XmlRpcClientConfigImpl config;
	XmlRpcClient client = new XmlRpcClient();

	/**
	 * Configures the XML-RPC client to use the OpenSubtitles API.
	 */
	OpenSubtitlesClient() throws XmlRpcException {
		config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("https://api.opensubtitles.org/xml-rpc"));
		config.setUserAgent(USER_AGENT);
		config.setGzipCompressing(true);
		config.setGzipRequesting(true);
		client.setConfig(config);
	}

	/**
	 * Implements OpenSubtitles LogIn() function. Required before 
	 * calling any other method.
	 *
	 * @param username Username in OpenSubtitles.org. Can be blank.
	 * @param password Password for username. Can be blank.
	 * @param language ISO639 two-letter code.
	 *
	 * @return Session token. Used in later communication.
	 */
	String login(String username, String password, String language) 
			throws BadStatusException {
		Object[] args = new Object[]{username, password, language, USER_AGENT};

		HashMap<String, Object> exe = client.execute("LogIn", args);
		String token = exe.get("token");
		String status = exe.get("status");

		if (!status.equals("200 OK")) {
			throw new BadStatusException();
		}

		return token;
	}

	/**
	 * Implements OpenSubtitles LogOut() function. Should be used 
	 * before exiting program.
	 *
	 * @param token Session token.
	 */
	void logout(String token) throws BadStatusException{
		Object[] args = new Object[]{token};
		HashMap<String, String> exe = client.execute("LogOut", args);

		if (!status.equals("200 OK")) {
			throw new BadStatusException();
		}
	}

	/**
	 * Implements OpenSubtitles SearchSubtitles() function.
	 *
	 * @param token Session token.
	 * @param queries Search queries by moviehash and size, IMDB ID, 
	        language, tag, name, etc. See official documentation.
	 * @param limit Limit of total results (must be less than 500).
	 *
	 * @return HashMap of subtitle file IDs and filenames.
	 */
	HashMap<String, String> searchSubtitles(String token, 
				ArrayList<ArrayList<String>> queries, 
				int limit) throws BadStatusException {
		List args = new ArrayList();
		args.add(token);
		args.add(queries);
		args.add(new Object[]{limit});

		HashMap<String, Object> exe = client.execute("SearchSubtitles", args);
		String status = exe.get("status");

		if (!status.equals("200 OK")) {
			throw new BadStatusException();
		}

		ArrayList<HashMap<String, String>> results = exe.get("data");

		HashMap<String, String> subs = new HashMap<>();

		for (HashMap<String, String> result : results) {
			subs.put(result.get("IDSubtitleFile"), result.get("SubFileName"));
		}

		return subs;
	}

	/**
	 * Implements OpenSubtitles DownloadSubtitles() function.
	 *
	 * @param token Session token.
	 * @param subtitles List of subtitle file IDs to download.
	 *
	 * @return Base64-encoded gzipped subtitle files mapped to IDs.
	 */
	HashMap<String, String> downloadSubtitles(String token, 
			ArrayList<String> subtitles) throws BadStatusException {
		Object[] args = new Object[]{token, subtitles};

		HashMap<String, Object> exe = client.execute("DownloadSubtitles", args);
		String status = exe.get("status");

		ArrayList<HashMap<String, String>> results = exe.get("data");

		HashMap<String, String> subs = new HashMap<>();

		for (HashMap<String, String> result : results) {
			subs.put(result.get("idsubtitlefile"), result.get("data"));
		}

		return subs;
	}
}