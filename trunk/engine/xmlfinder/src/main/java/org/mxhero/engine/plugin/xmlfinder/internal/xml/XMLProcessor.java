package org.mxhero.engine.plugin.xmlfinder.internal.xml;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.exolab.castor.xml.XMLContext;

import org.mxhero.engine.plugin.xmlfinder.internal.model.DomainListXML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to save and load a xml file with domains and users.
 * 
 * @author mmarmol
 */
public class XMLProcessor {

	private static Logger log = LoggerFactory.getLogger(XMLProcessor.class);

	private static final String MAPPING_FILE = "mapping.xml";

	private XMLContext context;

	/**
	 * This constructor load the mapping file from castor and sets the proper
	 * context using the class loader of this class.
	 * 
	 * @throws IOException
	 * @throws MappingException
	 */
	public XMLProcessor() throws IOException, MappingException {
		Mapping mapping = new Mapping();
		mapping.loadMapping(this.getClass().getClassLoader().getResource(
				MAPPING_FILE));
		log.debug("mapping loaded");
		context = new XMLContext();
		context.addMapping(mapping);
		log.debug("XMLProcessor created");
	}

	/**
	 * Save the xml into a file.
	 * 
	 * @param message the domain list
	 * @param fileName the name of the file and path
	 * @throws IOException
	 * @throws MarshalException
	 * @throws ValidationException
	 */
	public void saveXML(DomainListXML message, String fileName)
			throws IOException, MarshalException, ValidationException {
		Writer writer = new FileWriter(fileName);
		log.debug("file writer created for " + fileName);
		try {
			Marshaller marshaller = context.createMarshaller();
			marshaller.setWriter(writer);
			marshaller.marshal(message);
			log.debug("marshalled");
		} finally {
			writer.close();
			log.debug("file closed " + fileName);
		}
	}

	/**
	 * Loads from a file the domain list.
	 * @param fileName name of the file
	 * @return the domain and users loaded
	 * @throws IOException
	 * @throws MarshalException
	 * @throws ValidationException
	 */
	public DomainListXML loadXML(String fileName) throws IOException,
			MarshalException, ValidationException {
		Reader reader = new FileReader(fileName);
		log.debug("file reader created for " + fileName);
		DomainListXML list = null;
		try {
			Unmarshaller unmarshaller = context.createUnmarshaller();
			unmarshaller.setClass(DomainListXML.class);
			list = (DomainListXML) unmarshaller.unmarshal(reader);
			log.debug("unmarshalled");
		} finally {
			reader.close();
			log.debug("file closed " + fileName);
		}
		return list;
	}

}
