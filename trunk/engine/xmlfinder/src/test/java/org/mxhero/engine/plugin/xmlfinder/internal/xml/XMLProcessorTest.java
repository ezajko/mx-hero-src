package org.mxhero.engine.plugin.xmlfinder.internal.xml;

import java.io.File;
import java.io.IOException;

import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.junit.Assert;
import org.junit.Test;
import org.mxhero.engine.plugin.xmlfinder.internal.model.DomainListXML;


public class XMLProcessorTest {

	private static DomainListXML list = null;
	
	@Test
	public void testLoad() throws IOException, MappingException, MarshalException, ValidationException, ClassNotFoundException{
		XMLProcessor processor = new XMLProcessor();
		list = processor.loadXML(this.getClass().getClassLoader().getResources("in.xml").nextElement().getFile());
	}
	
	@Test
	public void testSave() throws IOException, MappingException, MarshalException, ValidationException, ClassNotFoundException{
		Assert.assertNotNull(list);
		XMLProcessor processor = new XMLProcessor();
		processor.saveXML(list, "out.xml");
		File file = new File("out.xml");
		file.delete();
	}
	
}
