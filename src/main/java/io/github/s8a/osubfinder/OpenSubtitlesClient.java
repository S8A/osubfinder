package io.github.s8a.osubfinder;

import java.util.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

class OpenSubtitlesClient {

	private static final String useragent = "TemporaryUserAgent";
	private XmlRpcClientConfigImpl config;
	XmlRpcClient client;

	OpenSubtitlesClient() throws XmlRpcException {
		config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL("https://api.opensubtitles.org/xml-rpc"));
		config.setUserAgent(useragent);
		config.setGzipCompressing(true);
		config.setGzipRequesting(true);
		client = new XmlRpcClient();
		client.setConfig(config);
	}

	private String login(String username, String password, String language) {
		Object[] params = {username, password, language, useragent};
		// incomplete
	}
}