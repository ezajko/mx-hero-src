package org.mxhero.engine.plugin.attachmentlink.alcommand.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.SharedFileInputStream;

import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.attachmentlink.alcommand.ALCommandParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:bundle-context.xml")
public class ALCommandTest {

	@Autowired
	private AlCommandImpl command;

	@Autowired
	private javax.sql.DataSource datasource;

	@Before
	public void setUp() throws Exception {
		DatabaseOperation.DELETE_ALL.execute(getConnection(), new XmlDataSet(
				this.getClass().getResourceAsStream("/dataset.xml")));
	}

	private IDatabaseConnection getConnection() throws Exception {
		// get connection
		Connection con = datasource.getConnection();
		IDatabaseConnection connection = new DatabaseConnection(con);
		DatabaseConfig config = connection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY,
				new MySqlDataTypeFactory());
		return connection;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCommandOneAttach() {
		Result result = testMailOneHTMLAttachment("juanpablo.royo@gmail.com",
				"AAAA", 1);
		assertTrue(result.isConditionTrue());
	}

	@Test
	public void testCommandTwoAttach() {
		Result result = testMailOneHTMLAttachment("juan@juan.com", "AAAA", 2);
		assertTrue(result.isConditionTrue());
	}

	@Test
	public void testCommandThreeAttach() {
		Result result = testMailOneHTMLAttachment("juan@juan.com", "AAAA", 3);
		assertTrue(result.isConditionTrue());
	}

	@Test
	public void testCommandFourAttach() {
		Result result = testMailOneHTMLAttachment("juanpablo.royo@gmail.com", "AAAA", 4);
		assertTrue(result.isConditionTrue());
	}

	@Test
	public void testCommandTwoSameAttachReprocessed() {
		Result result = testMailOneHTMLAttachment("juan@juan.com", "AAAA", 1);
		assertTrue(result.isConditionTrue());
		Result result1 = testMailOneHTMLAttachment("juan@juan.com", "BBBB", 2);
		assertTrue(result1.isConditionTrue());
	}

	@Test
	public void testCommandTwoSameMailReprocessed() {
		Result result = testMailOneHTMLAttachment("juan@juan.com", "AAAA", 1);
		assertTrue(result.isConditionTrue());
		Result result1 = testMailOneHTMLAttachment("juan@juan.com", "AAAA", 1);
		assertTrue(result1.isConditionTrue());
	}

	@Test
	public void testCommandTwoDifferentRecipient() {
		Result result = testMailOneHTMLAttachment("juan@juan.com", "AAAA", 1);
		assertTrue(result.isConditionTrue());
		Result result1 = testMailOneHTMLAttachment("pepe@pepe.com", "AAAA", 1);
		assertTrue(result1.isConditionTrue());
	}

	@Test
	public void testCommandTwoDifferentRecipientMoreThanOneAttachment() {
		Result result = testMailOneHTMLAttachment("juan@juan.com", "AAAA", 3);
		assertTrue(result.isConditionTrue());
		Result result1 = testMailOneHTMLAttachment("pepe@pepe.com", "AAAA", 3);
		assertTrue(result1.isConditionTrue());
	}

//	@Test
	public void testCommandRecipientRequeue() {
		Collection<Callable<ResultWrapper>> colls = new ArrayList<Callable<ResultWrapper>>();
		Callable<ResultWrapper> commandFirst = new Callable<ResultWrapper>() {

			@Override
			public ResultWrapper call() throws Exception {
				ResultWrapper wrapper = new ResultWrapper();
				String recipient = "juan@juan.com";
				String idMail = "AAAA";
				Result result = testMailOneHTMLAttachment(recipient, idMail, 1);
				wrapper.setResult(result);
				wrapper.setEmail(recipient);
				wrapper.setMessageId(idMail);
				return wrapper;
			}
		};
		colls.add(commandFirst);
		Callable<ResultWrapper> commandRequeue = new Callable<ResultWrapper>() {

			@Override
			public ResultWrapper call() throws Exception {
				ResultWrapper wrapper = new ResultWrapper();
				String recipient = "pepe@pepe.com";
				String idMail = "AAAA";
				Result result = testMailOneHTMLAttachment(recipient, idMail, 1);
				wrapper.setResult(result);
				wrapper.setEmail(recipient);
				wrapper.setMessageId(idMail);
				return wrapper;
			}
		};
		colls.add(commandRequeue);
		try {
			ExecutorService executor = Executors.newFixedThreadPool(2);
			List<Future<ResultWrapper>> invokeAll = executor.invokeAll(colls);
			assertTrue(invokeAll.size() == 2);
			int countFails = 0;
			for (Future<ResultWrapper> future : invokeAll) {
				assertTrue(future.isDone());
				ResultWrapper result = future.get();
				if (!result.getResult().isConditionTrue()) {
					countFails++;
					assertEquals(Mail.Status.requeue.toString(), result.getResult().getMessage());
				}
			}
			assertTrue(countFails == 1);

		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testCommandRecipientRequeueAndRetrySuccessfully() {
		testCommandRecipientRequeueAndRetrySuccessMock(2, "AAAA");
	}

//	@Test
	public void testCommandRecipientRequeueAndRetrySuccessfully500Loop() {
		for (int i = 0; i < 500; i++) {
			testCommandRecipientRequeueAndRetrySuccessMock(100, "BBBB" + i);
		}
	}

	private void testCommandRecipientRequeueAndRetrySuccessMock(int threads,
			final String id) {
		Collection<Callable<ResultWrapper>> colls = new ArrayList<Callable<ResultWrapper>>();
		colls.add(new Callable<ResultWrapper>() {

			@Override
			public ResultWrapper call() throws Exception {
				ResultWrapper wrapper = new ResultWrapper();
				String recipient = "juan@juan.com";
				String idMail = id;
				Result result = testMailOneHTMLAttachment(recipient, idMail, 1);
				wrapper.setResult(result);
				wrapper.setEmail(recipient);
				wrapper.setMessageId(idMail);
				return wrapper;
			}
		});
		colls.add(new Callable<ResultWrapper>() {

			@Override
			public ResultWrapper call() throws Exception {
				ResultWrapper wrapper = new ResultWrapper();
				String recipient = "pepe@pepe.com";
				String idMail = id;
				Result result = testMailOneHTMLAttachment(recipient, idMail, 1);
				wrapper.setResult(result);
				wrapper.setEmail(recipient);
				wrapper.setMessageId(idMail);
				return wrapper;
			}
		});
		try {
			ExecutorService executor = Executors.newFixedThreadPool(threads);
			List<Future<ResultWrapper>> invokeAll = executor.invokeAll(colls);
			assertTrue(invokeAll.size() == 2);
			int countFails = 0;
			int countSuccess = 0;
			String emailRequeue = null;
			String messageId = null;
			for (Future<ResultWrapper> future : invokeAll) {
				assertTrue("Id: " + id + " - Future doesnt end the job", future
						.isDone());
				ResultWrapper result = future.get();
				if (!result.getResult().isConditionTrue()) {
					countFails++;
					emailRequeue = result.getEmail();
					messageId = result.getMessageId();
					assertEquals("Id: " + id + " - Message state was "
							+ result.getResult().getMessage()
							+ " and expected REQUEUE", Mail.Status.requeue.toString(), result
							.getResult().getMessage());
				} else {
					countSuccess++;
				}
			}
			if(countFails>0){
				final String recipient = emailRequeue;
				final String idMail = messageId;
				Future<ResultWrapper> submit = executor
						.submit(new Callable<ResultWrapper>() {
	
							@Override
							public ResultWrapper call() throws Exception {
								ResultWrapper wrapper = new ResultWrapper();
								Result result = testMailOneHTMLAttachment(
										recipient, idMail, 1);
								wrapper.setResult(result);
								wrapper.setEmail(recipient);
								wrapper.setMessageId(idMail);
								return wrapper;
							}
						});
				while (!submit.isDone())
					;
				assertTrue("Id: " + id, submit.isDone());
				assertTrue("Id: " + id, submit.get().getResult().isConditionTrue());
			}
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	
//	@Test
	public void testCommandTimes100Concurrents() {
		testMailOneHTMLAttachment("pepe@carlo.com", "BBBB", 1);
		Collection<Callable<ResultWrapper>> colls = new ArrayList<Callable<ResultWrapper>>();
		for (int i = 0; i < 100; i++) {
			final int var = i;
			Callable<ResultWrapper> commandFirst = new Callable<ResultWrapper>() {

				@Override
				public ResultWrapper call() throws Exception {
					ResultWrapper wrapper = new ResultWrapper();
					String recipient = "juan@juan.com"+var;
					String idMail = "AAA"+var;
					Result result = testMailOneHTMLAttachment(recipient, idMail, 1);
					wrapper.setResult(result);
					wrapper.setEmail(recipient);
					wrapper.setMessageId(idMail);
					return wrapper;
				}
			};
			colls.add(commandFirst);
		}
		try {
			ExecutorService executor = Executors.newFixedThreadPool(100);
			long millis = System.currentTimeMillis();
			System.out.println("Time before "+millis);
			List<Future<ResultWrapper>> invokeAll = executor.invokeAll(colls);
			assertTrue(invokeAll.size() == 100);
			for (Future<ResultWrapper> future : invokeAll) {
				assertTrue(future.isDone());
				assertTrue(future.get().getResult().isConditionTrue());
			}
			long millis1 = System.currentTimeMillis();
			System.out.println("Time after "+millis1);
			long millis11 = millis1-millis;
			System.out.println("Diff "+millis11);
		} catch (Exception e) {
			fail();
		}
	}

	
//	@Test
	public void testWithEmail() {
		testStrangeEmail("idEmail1");
	}
	
	private void testStrangeEmail(String idEmail) {
		Result result = null;
		try {
			FileInputStream isf = new FileInputStream("C:\\temp\\templates\\prueba.msg");
			MimeMessage data = new MimeMessage(Session.getDefaultInstance(new Properties()), isf);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			data.writeTo(os);
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
			MimeMail mail = new MimeMail("", "juan@juan.com", is, "AAAAA");
			mail.setSenderId("juan@juan.com");
			mail.setSenderDomainId("juan.com");
			mail.setMessageId(idEmail);
			ALCommandParameters parameters = new ALCommandParameters();
			parameters.setLocale("es");
			parameters.setNotify(true);
			parameters.setNotifyMessage("some message");
			result = command.exec(mail, parameters);
			if (result.isConditionTrue()) {
				Object content = mail.getMessage().getContent();
				if (content instanceof Multipart) {
					Multipart multi = (Multipart) content;
					int cAttachs = 0;
					for (int i = 0; i < multi.getCount(); i++) {
						BodyPart bodyPart = multi.getBodyPart(i);
						if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart
								.getDisposition())) {
							cAttachs++;
							MimeBodyPart part = (MimeBodyPart) bodyPart;
							ReadableByteChannel in = Channels.newChannel(part
									.getDataHandler().getInputStream());
							FileOutputStream stringWriter = new FileOutputStream(new File("C:\\temp\\generated\\"+idEmail+".html"));
							WritableByteChannel out = Channels
									.newChannel(stringWriter);
							ByteBuffer dst = ByteBuffer.allocateDirect(1024);
							while (in.read(dst) != -1) {
								dst.flip();
								out.write(dst);
								dst.compact();
							}
							dst.flip();
							if (dst.hasRemaining()) {
								out.write(dst);
							}
							out.close();
						}
					}
					assertTrue(cAttachs == 1);
				}
			}
		} catch (Exception e1) {
			fail();
		}
	}

	private Result testMailOneHTMLAttachment(String recipient, String idMail,
			int amountInitialAttachs) {
		Result result = null;
		try {
			MimeMessage data = sendMessage(recipient, amountInitialAttachs);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			data.writeTo(os);
			ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
//			sendMail(data);
			MimeMail mail = new MimeMail("", recipient, is, "AAAAA");
			mail.setMessageId(idMail);
			ALCommandParameters parameters = new ALCommandParameters();
			parameters.setLocale("es");
			parameters.setNotify(true);
			parameters.setNotifyMessage("some message");
			parameters.setNotifyMessageHtml("some html message");
			parameters.setStorageId("boxStorage");
			parameters.setSenderStorage("juanpablo.royo@gmail.com");
			parameters.setRecipientStorage("mxhero12test@mxhero.com");
			result = command.exec(mail, parameters);
//			sendMail(mail.getMessage());
			if (result.isConditionTrue()) {
				Object content = mail.getMessage().getContent();
				if (content instanceof Multipart) {
					Multipart multi = (Multipart) content;
					int cAttachs = 0;
					for (int i = 0; i < multi.getCount(); i++) {
						BodyPart bodyPart = multi.getBodyPart(i);
						if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart
								.getDisposition())) {
							cAttachs++;
							MimeBodyPart part = (MimeBodyPart) bodyPart;
							ReadableByteChannel in = Channels.newChannel(part
									.getDataHandler().getInputStream());
							ByteArrayOutputStream stringWriter = new ByteArrayOutputStream();
							WritableByteChannel out = Channels
									.newChannel(stringWriter);
							ByteBuffer dst = ByteBuffer.allocateDirect(1024);
							while (in.read(dst) != -1) {
								dst.flip();
								out.write(dst);
								dst.compact();
							}
							dst.flip();
							if (dst.hasRemaining()) {
								out.write(dst);
							}
						}
					}
					assertTrue(cAttachs == 1);
				}
			}

		} catch (Exception e1) {
			fail();
		}
		return result;
	}


	private MimeMessage sendMessage(String recipient, int amountAttach)
			throws IOException, MessagingException, AddressException {
		MimeMessage message = new MimeMessage(Session
				.getDefaultInstance(new Properties()));
		Multipart mp = new MimeMultipart();
		for (int i = 0; i < amountAttach; i++) {
			StringBuffer file = new StringBuffer("picture");
			file.append(i);
			file.append(".jpg");
			MimeBodyPart part = addAttach(file.toString());
			mp.addBodyPart(part);
		}
		message.setContent(mp);
		message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
		message.setSender(new InternetAddress("juanpablo.royo@gmail.com"));
		message.setSubject("Attachment Links");
		message.setFrom(new InternetAddress("juanpablo.royo@gmail.com"));
		message.saveChanges();
		return message;
	}

	private MimeBodyPart addAttach(String file) throws IOException,
			MessagingException {
		MimeBodyPart part = new MimeBodyPart();
		StringBuffer name = new StringBuffer("/");
		name.append(file);
		URL resource = this.getClass().getResource(name.toString());
		DataSource ds = new URLDataSource(resource);
		part.setFileName(file);
		part.setDisposition(Part.ATTACHMENT);
		part.setDataHandler(new DataHandler(ds));
		return part;
	}


//	@Test
	public void testAllEmailsPlatform(){
		String file = "C:\\$USER\\Personales\\mxhero\\Attachment Links\\emails";
		File dirEmails = new File(file);
		File[] listFiles = dirEmails.listFiles();
		for (int i = 0; i < listFiles.length; i++) {
			File file2 = listFiles[i];
			testPlatformMxHeroMails(file2.getName(), file2.getAbsolutePath());	
		}	
	}
	
//	@Test
	public void testOneEmailsPlatform(){
		String file = "C:\\$USER\\Personales\\mxhero\\Attachment Links\\emails\\subetha208431520334756393.eml";
		testPlatformMxHeroMails("IdMail12", file);	
	}

	private void testPlatformMxHeroMails(String idMail, String file) {
		Result result = null;
		try {
			InputStream is = new SharedFileInputStream(file);

			String recipient = "juanpablo.royo@gmail.com";
			MimeMail mail = new MimeMail("", recipient, is, idMail);
			ALCommandParameters parameters = new ALCommandParameters();
			parameters.setLocale("es");
			parameters.setNotify(true);
			parameters.setNotifyMessage("some message");
			mail.setMessageId(idMail);
			result = command.exec(mail, parameters);
			if (result.isConditionTrue()) {
				Object content = mail.getMessage().getContent();
				if (content instanceof Multipart) {
					Multipart multi = (Multipart) content;
					int cAttachs = 0;
					for (int i = 0; i < multi.getCount(); i++) {
						BodyPart bodyPart = multi.getBodyPart(i);
						if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart
								.getDisposition())) {
							cAttachs++;
							MimeBodyPart part = (MimeBodyPart) bodyPart;
							ReadableByteChannel in = Channels.newChannel(part
									.getDataHandler().getInputStream());
							FileOutputStream stringWriter = new FileOutputStream(new File("C:\\temp\\generated\\"+idMail+".html"));
							WritableByteChannel out = Channels
									.newChannel(stringWriter);
							ByteBuffer dst = ByteBuffer.allocateDirect(1024);
							while (in.read(dst) != -1) {
								dst.flip();
								out.write(dst);
								dst.compact();
							}
							dst.flip();
							if (dst.hasRemaining()) {
								out.write(dst);
							}
							out.close();
						}
					}
					assertTrue(cAttachs == 1);
				}
			}

		} catch (Exception e1) {
			fail();
		}
	}
}
