package org.mxhero.engine.core.internal.drools;

import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactoryService;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderFactoryService;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactoryService;
import org.drools.util.ServiceRegistry;
import org.mxhero.engine.domain.provider.ResourceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to generate a KnowledgeBase from a change set.
 * 
 * @author mmarmol
 */
public class KnowledgeBaseBuilder {

	private static Logger log = LoggerFactory
			.getLogger(KnowledgeBaseBuilder.class);

	private String filePath;

	private ServiceRegistry registry;
	
	@SuppressWarnings("unchecked")
	private List resourceProviders;

	private KnowledgeBase knowledgeBase = null;

	/**
	 * Actually builds the base calling the ServiceRegistry from drools inside
	 * the osgi environment. Uses the file path passed to it to locate the chang
	 * set.
	 */
	public void buildBase() {

		KnowledgeBuilderFactoryService knowledgeBuilderFactoryService = registry
				.get(KnowledgeBuilderFactoryService.class);

		KnowledgeBaseFactoryService knowledgeBaseFactoryService = registry
				.get(KnowledgeBaseFactoryService.class);
		ResourceFactoryService resourceFactoryService = registry
				.get(ResourceFactoryService.class);

		KnowledgeBuilderConfiguration kbConf = knowledgeBuilderFactoryService
				.newKnowledgeBuilderConfiguration(null, getClass()
						.getClassLoader());

		KnowledgeBuilder kbuilder = knowledgeBuilderFactoryService
				.newKnowledgeBuilder(kbConf);
		
		kbuilder.add(resourceFactoryService.newFileSystemResource(filePath),
				ResourceType.CHANGE_SET);

		for(Object provider : resourceProviders){
			try{
				kbuilder.add(resourceFactoryService.newInputStreamResource(((ResourceProvider)provider).getResource()),
						ResourceType.getResourceType(((ResourceProvider)provider).getResourceType()));
			}catch (Exception e) {
				log.error("error while trying to load resource from external provider",e);
			}
		}
		
		if (kbuilder.hasErrors()) {
			log.error("error while building rules data base:", kbuilder
					.getErrors().toString());
			return;
		}

		KnowledgeBaseConfiguration kbaseConf = knowledgeBaseFactoryService
				.newKnowledgeBaseConfiguration(null, getClass()
						.getClassLoader());

		KnowledgeBase kbase = knowledgeBaseFactoryService
				.newKnowledgeBase(kbaseConf);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		knowledgeBase = kbase;
	}

	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath
	 *            the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * @return the knowledgeBase
	 */
	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}

	/**
	 * @param knowledgeBase
	 *            the knowledgeBase to set
	 */
	public void setKnowledgeBase(KnowledgeBase newBase) {
		this.knowledgeBase = newBase;
	}

	/**
	 * @return the registry
	 */
	public ServiceRegistry getRegistry() {
		return registry;
	}

	/**
	 * @param registry
	 *            the registry to set
	 */
	public void setRegistry(ServiceRegistry registry) {
		this.registry = registry;
	}
	
	/**
	 * @return the resourceProviders
	 */
	@SuppressWarnings("unchecked")
	public List getResourceProviders() {
		return resourceProviders;
	}

	/**
	 * @param resourceProviders the resourceProviders to set
	 */
	@SuppressWarnings("unchecked")
	public void setResourceProviders(List resourceProviders) {
		this.resourceProviders = resourceProviders;
	}

}
