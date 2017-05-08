package org.alfresco.extension.corenlp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.content.MimetypeMap;
import org.alfresco.repo.content.transform.ContentTransformer;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentData;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.ContentService;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.TransformationOptions;
import org.alfresco.service.namespace.QName;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;
import edu.stanford.nlp.util.CoreMap;

public class CoreNLPService {

	private Log logger = LogFactory.getLog(CoreNLPService.class);
	private CoreNLPClientFactory clientFactory;
	private ServiceRegistry registry;
	private static final String use = "corenlp";
	private int maxPages = 100;
	private int maxSize = -1;
	
	private static final String nePerson = "PERSON";
	
	public void annotateDocument(NodeRef doc) {
		Annotation an = getAnnotations(doc);
		decorateNode(doc, an);
	}
	
	/**
	 * Extracts data from the annotations and uses it to decorate the node via an aspect
	 * @param an
	 */
	private void decorateNode(NodeRef doc, Annotation an) {
		NodeService ns = registry.getNodeService();
		Map<QName, Serializable> props = new HashMap<QName, Serializable>();
		ArrayList<String> people = new ArrayList<String>();
		
		List<CoreMap> sentences = an.get(SentencesAnnotation.class);

		for(CoreMap sentence: sentences) {
		  // traversing the words in the current sentence
		  // a CoreLabel is a CoreMap with additional token-specific methods
		  for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
		    if(token.get(NamedEntityTagAnnotation.class).equals(nePerson)) {
		    	String ne = token.get(TextAnnotation.class);
		    	System.out.println("Named Entity: " + ne);
		    	people.add(ne);
		    }
		  }
		}
		
		props.put(CoreNLPModel.PROP_PEOPLE, people);
		
		// add aspect to contain the stuff from the pipeline
		ns.addAspect(doc, CoreNLPModel.ASPECT_NAMEDENTITIES, props);
	}
	
	/**
	 * Uses CoreNLP to annotate a document and fetches the results.
	 * 
	 * @param doc
	 * @return
	 */
	private Annotation getAnnotations(NodeRef doc) {

		StanfordCoreNLPClient nlpClient = clientFactory.getClient();

		Annotation an = null;
		String text = getText(doc);
		
		try {
			an = new Annotation(text);
			nlpClient.annotate(an);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return an;
	}
	
	private String getText(NodeRef doc) {
		
		ContentService cs = registry.getContentService();
		NodeService ns = registry.getNodeService();
		
		ContentReader reader = cs.getReader(doc, ContentModel.PROP_CONTENT);
		
		// set up a writer for the transformed output, and a reader to get it back
		ContentWriter writer = cs.getTempWriter();
		
		// if the writer doesn't have a mime type set, it will fail
		writer.setMimetype(MimetypeMap.MIMETYPE_TEXT_PLAIN);
		writer.setEncoding("UTF-8");
		
		TransformationOptions options = new TransformationOptions();
		options.setUse(use);
		options.setSourceNodeRef(doc);
		options.setSourceContentProperty(ContentModel.PROP_CONTENT);
		options.setTargetContentProperty(ContentModel.PROP_CONTENT);
		
		options.setMaxPages(maxPages);
		if(maxSize > 0) {
			options.setMaxSourceSizeKBytes(maxSize);
		}
		
		ContentData contentData = (ContentData) ns.getProperty(doc, ContentModel.PROP_CONTENT);
	    String mimeType = contentData.getMimetype();
		ContentTransformer transformer = cs.getTransformer(mimeType, MimetypeMap.MIMETYPE_TEXT_PLAIN);
		
		// can we transform this thing to text?
		if(transformer != null){
			transformer.transform(reader, writer, options);
		}else {
			
		}
		
		ContentReader tempReader = writer.getReader();
		String text = "Nathan works at Alfresco, and this is his test of Standford CoreNLP";

		text = tempReader.getContentString();
		return text;
	}
	public void setServiceRegistry(ServiceRegistry registry) {
		this.registry = registry;
	}
	
	public void setClientFactory(CoreNLPClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
}
