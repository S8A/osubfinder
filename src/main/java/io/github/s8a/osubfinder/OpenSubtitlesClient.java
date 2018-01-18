package io.github.s8a.osubfinder;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.XmlRpcException;


class BadStatusException extends Exception {
	BadStatusException() {}
	BadStatusException(String msg) { super(msg); }
}


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
	OpenSubtitlesClient() throws MalformedURLException {
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
	 *
	 * @throws BadStatusException If the function call returns
	 *       a status other than 200 OK.
	 * @throws XmlRpcException If the function call fails.
	 */
	String login(String username, String password, String language) 
			throws BadStatusException, XmlRpcException {
		Object[] args = new Object[]{username, password, language, USER_AGENT};

		HashMap<String, Object> exe = 
				(HashMap<String, Object>) client.execute("LogIn", args);
		String token = (String) exe.get("token");
		String status = (String) exe.get("status");

		if (!status.equals("200 OK")) {
			throw new BadStatusException(status);
		}

		return token;
	}

	/**
	 * Implements OpenSubtitles LogOut() function. Should be used 
	 * before exiting program.
	 *
	 * @param token Session token.
	 *
	 * @throws BadStatusException If the function call returns
	 *       a status other than 200 OK.
	 * @throws XmlRpcException If the function call fails.
	 */
	void logout(String token) throws BadStatusException, XmlRpcException {
		Object[] args = new Object[]{token};
		HashMap<String, String> exe = 
				(HashMap<String, String>) client.execute("LogOut", args);
		String status = (String) exe.get("status");

		if (!status.equals("200 OK")) {
			throw new BadStatusException(status);
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
	 *
	 * @throws BadStatusException If the function call returns
	 *       a status other than 200 OK.
	 * @throws XmlRpcException If the function call fails.
	 */
	HashMap<String, String> searchSubtitles(String token, 
			ArrayList<HashMap<String, String>> queries, int limit) 
			throws BadStatusException, XmlRpcException {
		Object[] args = new Object[]{token,	queries, new Object[]{limit}};

		HashMap<String, Object> exe = (HashMap<String, Object>) client.execute(
				"SearchSubtitles", args);
		String status = (String) exe.get("status");

		if (!status.equals("200 OK")) {
			throw new BadStatusException(status);
		}

		ArrayList<HashMap<String, String>> results = 
				(ArrayList<HashMap<String, String>>) exe.get("data");

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
	 *
	 * @throws BadStatusException If the function call returns
	 *       a status other than 200 OK.
	 * @throws XmlRpcException If the function call fails.
	 */
	HashMap<String, String> downloadSubtitles(
			String token, ArrayList<String> subtitles) 
			throws BadStatusException, XmlRpcException {
		Object[] args = new Object[]{token, subtitles};

		HashMap<String, Object> exe = (HashMap<String, Object>) client.execute(
				"DownloadSubtitles", args);
		String status = (String) exe.get("status");

		if (!status.equals("200 OK")) {
			throw new BadStatusException(status);
		}

		ArrayList<HashMap<String, String>> results = 
				(ArrayList<HashMap<String, String>>) exe.get("data");

		HashMap<String, String> subs = new HashMap<>();

		for (HashMap<String, String> result : results) {
			subs.put(result.get("idsubtitlefile"), result.get("data"));
		}

		return subs;
	}
}