package org.mxhero.engine.plugin.xmlfinder.internal.xml;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.exolab.castor.mapping.MappingException;
import org.junit.Assert;
import org.junit.Test;
import org.mxhero.engine.domain.properties.PropertiesService;
import org.mxhero.engine.plugin.xmlfinder.internal.service.XMLFinder;
import org.osgi.service.cm.ConfigurationException;

public class XMLTimeLoaderTest {

	@Test
	public void testStartAndStop() throws IOException, MappingException, InterruptedException{
		XMLTimeLoader loader = new XMLTimeLoader();
		loader.setProperties(new XMLFinder());
		loader.start();
		Thread.sleep(3000);
		loader.stop();
	}
	
	@Test
	public void loadRealData() throws IOException, MappingException, InterruptedException, ConfigurationException{
		XMLTimeLoader loader = new XMLTimeLoader();
		PropertiesService service = new XMLFinder();
		loader.setProperties(service);
		Dictionary<String, String> properties = new Hashtable<String,String>();
		properties.put("updatable.domains.file.path", this.getClass().getClassLoader().getResource("in.xml").getFile());
		properties.put("updatable.loader.check.time", "1000");
		properties.put("updatable.loader.load.time", "10001");
		service.updated(properties);
		loader.updated();
		loader.start();
		Thread.sleep(3000);
		Assert.assertNotNull(MapLoader.getDomain("mxhero.com"));
		loader.stop();
	}
	
}
