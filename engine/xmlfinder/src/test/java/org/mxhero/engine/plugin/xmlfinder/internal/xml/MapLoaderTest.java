package org.mxhero.engine.plugin.xmlfinder.internal.xml;

import java.io.IOException;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.mxhero.engine.plugin.xmlfinder.internal.model.DomainListXML;

public class MapLoaderTest {

	@Test
	public void testWhenFileExists() throws IOException, MappingException, MarshalException, ValidationException, ClassNotFoundException{
		XMLProcessor processor = new XMLProcessor();
		DomainListXML list = processor.loadXML(this.getClass().getClassLoader().getResources("in.xml").nextElement().getFile());
		MapLoader.createMap(list);
		Assert.assertNotNull(MapLoader.getDomain("mxhero.com"));
		Assert.assertNotNull(MapLoader.getUser("mmarmol@mxhero.com", "mxhero.com"));
	}
	
	@Test
	public void testNullList(){
		MapLoader.createMap(null);
		Assert.assertNull(MapLoader.getDomain("mxhero.com"));
		Assert.assertNull(MapLoader.getUser("mmarmol@mxhero.com", "mxhero.com"));
	}
	
}
