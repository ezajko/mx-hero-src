package org.mxhero.engine.domain.properties;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Test;
import org.osgi.service.cm.ConfigurationException;

public class PropertiesServiceTest {

	private boolean wasCalled = false;

	@SuppressWarnings("unchecked")
	@Test
	public void testService() throws ConfigurationException {
		@SuppressWarnings("rawtypes")
		Dictionary props = new Hashtable<String, String>();

		PropertiesService serviceNull = new PropertiesService(null);
		Assert.assertNull(serviceNull.getValue("nothing"));
		Assert.assertNotNull(serviceNull.getValue("nothing","value"));
		
		props.put("test", "test_value");
		props.put("test_empty", "");
		PropertiesService service = new PropertiesService(props);
		Assert.assertEquals("test_value", service.getValue("test"));
		Assert.assertEquals("test_value", service.getValue("test","default"));
		Assert.assertEquals("default", service
				.getValue("not_exists", "default"));
		Assert.assertEquals("default", service
				.getValue("test_empty", "default"));

		PropertiesListener listener = new PropertiesListener() {

			@Override
			public void updated() {
				wasCalled = true;
			}
		};

		service.addListener(listener);

		service.updated(props);
		service.removeListener(listener);
		Assert.assertTrue(wasCalled);
	}

}
