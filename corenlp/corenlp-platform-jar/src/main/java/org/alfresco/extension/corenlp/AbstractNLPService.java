package org.alfresco.extension.corenlp;

import org.alfresco.service.ServiceRegistry;

public abstract class AbstractNLPService implements NLPService {
	
	protected int maxPages = 100;
	protected int maxSize = -1;
	protected ServiceRegistry registry;
		
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
