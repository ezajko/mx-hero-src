package org.mxhero.engine.fsqueues;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.MimeMessage;

import org.junit.Assert;
import org.junit.Test;
import org.mxhero.engine.domain.mail.business.RulePhase;
import org.mxhero.engine.fsqueues.internal.FSConfig;
import org.mxhero.engine.fsqueues.internal.FSQueueService;
import org.mxhero.engine.fsqueues.internal.loader.FSLoader;

public class FSLoaderTest {

	private String loadPath = "C:\\temp\\loader";
	
	@Test
	public void testLoad() throws AddressException, MessagingException, URISyntaxException, IOException, InterruptedException {
		
		for(File loadFile : new File(loadPath).listFiles()){
			loadFile.delete();
		}
		
		FSLoader loader = new FSLoader(loadPath,"C:\\temp\\tmp");
		File sendPath = new File(loadPath, RulePhase.SEND.toLowerCase());
		sendPath.mkdir();
		File receivePath = new File(loadPath, RulePhase.RECEIVE.toLowerCase());
		receivePath.mkdir();
		File outPath = new File(loadPath, RulePhase.OUT.toLowerCase());
		outPath.mkdir();
		saveMessage(RulePhase.SEND.toLowerCase());
		saveMessage(RulePhase.RECEIVE.toLowerCase());
		saveMessage(RulePhase.OUT.toLowerCase());
		
		FSConfig config = new FSConfig("C:\\temp\\store", "C:\\temp\\tmp");
		FSQueueService srv = new FSQueueService(config);
		for(File tmpFile : config.getTmpPath().listFiles()){
			tmpFile.delete();
		}
		for(File storeFile : config.getStorePath().listFiles()){
			storeFile.delete();
		}
		
		srv.init();		
		loader.setQueueService(srv);
		loader.init();
		
		Thread.sleep(3500);
		
		Assert.assertNotNull(srv.poll(RulePhase.SEND, 1000, TimeUnit.MILLISECONDS));
		Assert.assertNotNull(srv.poll(RulePhase.RECEIVE, 1000, TimeUnit.MILLISECONDS));
		Assert.assertNotNull(srv.poll(RulePhase.OUT, 1000, TimeUnit.MILLISECONDS));
		
		loader.stop();
		srv.stop();
		
	}

	private void saveMessage(String phase) throws AddressException,
			MessagingException, URISyntaxException, IOException {
		
		MimeMessage msg = new MimeMessage(Session.getDefaultInstance(new Properties()),new FileInputStream(new File(this.getClass().getClassLoader()
				.getResource("pfxc5300455480221297252.eml").toURI())));
		msg.removeHeader(FSQueueService.SENDER_HEADER);
		msg.removeHeader(FSQueueService.RECIPIENT_HEADER);
		msg.removeHeader(FSQueueService.OUTPUT_SERVICE_HEADER);
		msg.addHeader(FSQueueService.SENDER_HEADER, "smith@example.com");
		msg.addHeader(FSQueueService.RECIPIENT_HEADER, "smith@example.com");
		msg.addHeader(FSQueueService.OUTPUT_SERVICE_HEADER, "service");
		msg.saveChanges();
		FileOutputStream os = new FileOutputStream(File.createTempFile("loader", ".eml",new File(loadPath,phase)));
		msg.writeTo(os);
		os.close();
	}

}
