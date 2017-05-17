package org.alfresco.extension.corenlp;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.alfresco.service.cmr.repository.NodeRef;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.Sentiment;


public class GoogleNLPService extends AbstractNLPService {

	private String url = "https://language.googleapis.com/v1/documents:analyzeEntities";
	private String bearer = "";

	
	public void annotateDocument(NodeRef doc) {
	
		try {
			// Instantiates a client
		    /*
		     * Can't use this yet, due to Alfresco shipping with an obsolete Guava version, should
		     * be updated in the product soon.
		     * 
		    LanguageServiceClient language = LanguageServiceClient.create();
		    String text = getText(doc);
		    Document procdoc = Document.newBuilder()
		            .setContent(text).setType(Type.PLAIN_TEXT).build();
		    
		    EncodingType encodingType = EncodingType.UTF8;
		    
		    AnalyzeEntitiesResponse response = language.analyzeEntities(procdoc, encodingType);
		    
		    Sentiment sentiment = language.analyzeSentiment(procdoc).getDocumentSentiment();
		    
		    */
			String text = getText(doc);
			
			HttpClient client = HttpClients.createDefault();
			HttpPost post = new HttpPost(url);
			post.addHeader("Authorization", "Bearer " + bearer);
			post.addHeader("Content-Type", "application/json");
			
			JsonObject entityreq = Json.createObjectBuilder()
					.add("document", Json.createObjectBuilder()
						.add("type", "PLAIN_TEXT")
						.add("content", StringEscapeUtils.escapeJson(text)))
					.add("encodingType", "UTF8")
					.build();
			
			StringEntity entity = new StringEntity(entityreq.toString());
			post.setEntity(entity);
			
			HttpResponse res = client.execute(post);
			
			if(res.getStatusLine().getStatusCode() == 200) {
				HttpEntity e = res.getEntity();
				
				JsonReader resreader = Json.createReader(e.getContent());
				JsonObject nlpdata = resreader.readObject();
				
			}
			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}

	}

	private void decorateNode(NodeRef doc, AnalyzeEntitiesResponse entities, Sentiment sentiment) {
		
	}
}
