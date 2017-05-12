package org.alfresco.extension.corenlp;

import java.util.List;

import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AnnotationAction extends ActionExecuterAbstractBase {

	public static final String name = "nlp-annotation-action";
	
	private Log logger = LogFactory.getLog(AnnotationAction.class);
	private ServiceRegistry registry;
	private NLPService nlps;
	
	@Override
	protected void executeImpl(Action action, NodeRef actionedUponNodeRef) {
		
		NodeService ns = registry.getNodeService();
		
		// sanity check the node first, then annotate if we can.
		if(ns.exists(actionedUponNodeRef)) {
			nlps.annotateDocument(actionedUponNodeRef);
		}else {
			
		}
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> params) {

		//TODO - Allow overrides of pipeline specified in service component config via action params
	}
	
	public void setNLPService(NLPService nlps) {
		this.nlps = nlps;
	}
	public void setServiceRegistry(ServiceRegistry registry) {
		this.registry = registry;
	}
}
