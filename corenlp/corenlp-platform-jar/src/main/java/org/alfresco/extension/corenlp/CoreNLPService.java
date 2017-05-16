package org.alfresco.extension.corenlp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
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

public class CoreNLPService extends AbstractNLPService {

	private Log logger = LogFactory.getLog(CoreNLPService.class);
	private CoreNLPClientFactory clientFactory;
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
		
		props.put(NLPModel.PROP_PEOPLE, people);
		
		// add aspect to contain the stuff from the pipeline
		ns.addAspect(doc, NLPModel.ASPECT_NAMEDENTITIES, props);
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
	

	
	public void setClientFactory(CoreNLPClientFactory clientFactory) {
		this.clientFactory = clientFactory;
	}
}
