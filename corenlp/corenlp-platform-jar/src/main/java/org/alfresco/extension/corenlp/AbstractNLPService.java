package org.alfresco.extension.corenlp;

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

public abstract class AbstractNLPService implements NLPService {
	
	protected int maxPages = 100;
	protected int maxSize = -1;
	protected ServiceRegistry registry;
	public static final String use = "corenlp";
	
	protected String getText(NodeRef doc) {
		
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
	public void setMaxPages(int maxPages) {
		this.maxPages = maxPages;
	}
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
}
