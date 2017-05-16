package org.alfresco.extension.corenlp;

import java.io.IOException;

import org.alfresco.service.cmr.repository.NodeRef;

import com.google.cloud.language.spi.v1.LanguageServiceClient;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Sentiment;


public class GoogleNLPService extends AbstractNLPService {

	public void annotateDocument(NodeRef doc) {

		try {
			// Instantiates a client
		    LanguageServiceClient language = LanguageServiceClient.create();
		    String text = getText(doc);
		    Document procdoc = Document.newBuilder()
		            .setContent(text).setType(Type.PLAIN_TEXT).build();
		    
		    EncodingType encodingType = EncodingType.UTF8;
		    
		    AnalyzeEntitiesResponse response = language.analyzeEntities(procdoc, encodingType);
		    
		    Sentiment sentiment = language.analyzeSentiment(procdoc).getDocumentSentiment();
		}
		catch(IOException ioex) {
			
		}

	}

	private void decorateNode(NodeRef doc, AnalyzeEntitiesResponse entities, Sentiment sentiment) {
		
	}
}
