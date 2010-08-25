package org.mxhero.engine.plugin.xmlfinder.internal.service;

import java.io.IOException;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mxhero.engine.plugin.xmlfinder.internal.model.DomainListXML;
import org.mxhero.engine.plugin.xmlfinder.internal.xml.MapLoader;
import org.mxhero.engine.plugin.xmlfinder.internal.xml.XMLProcessor;

public class XMLDomainFinderTest {

	@Before
	public void before() throws IOException, MappingException, MarshalException, ValidationException{
		XMLProcessor processor = new XMLProcessor();
		DomainListXML list = processor.loadXML(this.getClass().getClassLoader().getResources("in.xml").nextElement().getFile());
		MapLoader.createMap(list);
	}
	
	@Test
	public void testNullId(){
		Assert.assertNull(new XMLDomainFinder().getDomain(null));
	}
	
	@Test
	public void testId(){
		Assert.assertNotNull(new XMLDomainFinder().getDomain("mxhero.com"));
		System.out.println(new XMLDomainFinder().getDomain("mxhero.com").toString());
	}
}
