package org.alfresco.extension.corenlp;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;

public interface NLPService {

	public void annotateDocument(NodeRef doc);
	public void setServiceRegistry(ServiceRegistry registry);
	public void setMaxPages(int maxPages);
	public void setMaxSize(int maxSize);
}