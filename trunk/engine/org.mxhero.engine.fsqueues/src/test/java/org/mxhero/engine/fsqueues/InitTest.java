package org.mxhero.engine.fsqueues;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.junit.Assert;
import org.junit.Test;
import org.mxhero.engine.domain.mail.MimeMail;
import org.mxhero.engine.fsqueues.internal.FSConfig;
import org.mxhero.engine.fsqueues.internal.FSQueueService;

public class InitTest {

	@Test
	public void test() throws IOException, InterruptedException {
		int size = 10;
		FSConfig config = new FSConfig("C:\\temp\\store", "C:\\temp\\tmp");
		config.setDeferredSize(1024);
		for(File tmpFile : config.getTmpPath().listFiles()){
			tmpFile.delete();
		}
		for(File storeFile : config.getStorePath().listFiles()){
			storeFile.delete();
		}
		
		FSQueueService srv = new FSQueueService(config);
		srv.init();
		for (int i = 0; i < size; i++) {
			MimeMail mail = null;
			try {
				mail = new MimeMail("sender@example.com",
						"recipient@example.com", createMessage(), "service");
				try {
					srv.store("send", mail, 1000, TimeUnit.MILLISECONDS);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		srv.stop();
		Assert.assertTrue(config.getStorePath().listFiles().length==size);
		Assert.assertTrue(srv.size()==0);
		srv.init();
		Assert.assertTrue(srv.size()==size);
		for(int i=0;i<size;i++){
			Assert.assertNotNull(srv.poll("send", 1000, TimeUnit.MILLISECONDS));
		}
		Assert.assertNull(srv.poll("send", 1000, TimeUnit.MILLISECONDS));
		srv.stop();
	}

	private InputStream createMessage() throws AddressException,
			MessagingException, URISyntaxException, IOException {
		return new FileInputStream(new File(this.getClass().getClassLoader()
				.getResource("pfxc5300455480221297252.eml").toURI()));
	}

}
