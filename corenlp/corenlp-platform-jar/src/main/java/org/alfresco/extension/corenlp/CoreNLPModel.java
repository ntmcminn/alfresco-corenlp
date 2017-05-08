package org.alfresco.extension.corenlp;

import org.alfresco.service.namespace.QName;

public interface CoreNLPModel {

	static final String CORENLP_MODEL_1_0_URI = "http://www.alfresco.com/model/corenlp/1.0";
	
	static final QName ASPECT_NAMEDENTITIES = QName.createQName(CORENLP_MODEL_1_0_URI, "namedentities");
	static final QName PROP_PEOPLE = QName.createQName(CORENLP_MODEL_1_0_URI, "people");
	static final QName PROP_ORGANIZATIONS = QName.createQName(CORENLP_MODEL_1_0_URI, "organizations");
	static final QName PROP_MISC = QName.createQName(CORENLP_MODEL_1_0_URI, "misc");
	static final QName PROP_LOCATIONS = QName.createQName(CORENLP_MODEL_1_0_URI, "locations");
	
	static final QName ASPECT_SENTIMENT = QName.createQName(CORENLP_MODEL_1_0_URI, "sentiment");
	
}
