package org.alfresco.extension.corenlp;

import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;

public class CoreNLPClientFactory {

	private Log logger = LogFactory.getLog(CoreNLPClientFactory.class);
	private int port = 9000;
	private String host = "http://localhost";
	private String pipeline = "tokenize, ner";
	private int threads = 2;
	
	public StanfordCoreNLPClient getClient() {
		
		Properties nlpProps = new Properties();
		nlpProps.setProperty("annotators", pipeline);
		
		// add annotation pipeline
		StanfordCoreNLPClient client = new StanfordCoreNLPClient(nlpProps, host, port, threads);
		
		return client;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public void setAnnotationPipeline(String pipeline) {
		this.pipeline = pipeline;
	}
	public void setThreads(int threads) {
		this.threads = threads;
	}
}
