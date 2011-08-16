package org.mxhero.engine.core.internal.drools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactoryService;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderConfiguration;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderFactoryService;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactoryService;
import org.drools.util.ServiceRegistry;
import org.mxhero.engine.core.internal.service.Core;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.domain.provider.Resource;
import org.mxhero.engine.domain.provider.ResourcesProvider;
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

	private String backupdirbase;
	
	private ServiceRegistry registry;
	
	@SuppressWarnings("rawtypes")
	private List resourceProviders;

	private KnowledgeBase knowledgeBase = null;

	private PropertiesService properties;
	
	/**
	 * Actually builds the base calling the ServiceRegistry from drools inside
	 * the osgi environment. Uses the file path passed to it to locate the chang
	 * set.
	 */
	public void buildBase() {

		String folder = Long.toString(System.currentTimeMillis());
		folder = this.backupdirbase+"/"+folder;
		
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
		
/*		kbuilder.add(resourceFactoryService.newFileSystemResource(filePath),
				ResourceType.CHANGE_SET);*/

		log.debug("total resourceProviders:"+resourceProviders.size());
		for(Object provider : resourceProviders){
			if(Boolean.parseBoolean(getProperties().getValue(Core.DROOLS_RESOURCES_BACKUP).trim().toLowerCase())){
				new File(folder).mkdir();
			}
			try{
				for (Resource resource : ((ResourcesProvider)provider).getResources()){
					
					log.debug("resource:"+resource.getName());
					if(resource.getResource()!=null){
						InputStream is = null;
						if(Boolean.parseBoolean(getProperties().getValue(Core.DROOLS_RESOURCES_BACKUP).trim().toLowerCase())){
							byte[] buffer = new byte[1024];
							File resourceFile = new File(folder,resource.getName());
							FileOutputStream fos = new FileOutputStream(resourceFile);

							int len = resource.getResource().read(buffer);
							while (len != -1) {
								fos.write(buffer, 0, len);
							    len = resource.getResource().read(buffer);
							}
							
							fos.close();
							is = new FileInputStream(resourceFile);
						}else{
							is = resource.getResource();
						}

						try{
						kbuilder.add(resourceFactoryService.newInputStreamResource(is),
								ResourceType.getResourceType(resource.getType()));
						log.debug("resource loaded:"+resource.getName());
						}catch (Exception e){
							log.error("error while compiling resource "+resource.getName(),e);
						}	
					}					
				}				
			}catch (Exception e) {
				log.error("error while trying to load resource from external provider",e);
			}
		}
		
		if (kbuilder.hasErrors()) {
			log.error("error while building rules data base:", kbuilder
					.getErrors().toString());
			for (KnowledgeBuilderError error : kbuilder.getErrors()){
				log.error("error:"+error.getMessage());
			}
			return;
		}

		KnowledgeBaseConfiguration kbaseConf = knowledgeBaseFactoryService
				.newKnowledgeBaseConfiguration(null, getClass()
						.getClassLoader());

		KnowledgeBase kbase = knowledgeBaseFactoryService
				.newKnowledgeBase(kbaseConf);
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		knowledgeBase = kbase;
		log.info("base:"+knowledgeBase+", packages in base:" + knowledgeBase.getKnowledgePackages().size());
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
	@SuppressWarnings("rawtypes")
	public List getResourceProviders() {
		return resourceProviders;
	}

	/**
	 * @param resourceProviders the resourceProviders to set
	 */
	@SuppressWarnings("rawtypes")
	public void setResourceProviders(List resourceProviders) {
		this.resourceProviders = resourceProviders;
	}

	public String getBackupdirbase() {
		return backupdirbase;
	}

	public void setBackupdirbase(String backupdirbase) {
		this.backupdirbase = backupdirbase;
	}

	public PropertiesService getProperties() {
		return properties;
	}

	public void setProperties(PropertiesService properties) {
		this.properties = properties;
	}

}
