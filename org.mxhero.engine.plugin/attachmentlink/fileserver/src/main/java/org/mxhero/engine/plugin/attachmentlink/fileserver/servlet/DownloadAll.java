package org.mxhero.engine.plugin.attachmentlink.fileserver.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;
import org.mxhero.engine.plugin.attachmentlink.fileserver.service.ContentService;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author mmarmol
 */
public class DownloadAll extends HttpServlet {

	private static Logger log = Logger.getLogger(DownloadAll.class);

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doAction(req,resp);
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doAction(req,resp);
	}	
	
	public void doAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ContentService service = null;
		Long idToSearch = null;
		String id = request.getParameter("id");
		String recipient = request.getParameter("recipient");
		MDC.put("message", id);
		List<ContentDTO> contents = null;
		try {
			if(StringUtils.isEmpty(id)){
				log.debug("Error. No params in URL present. Forwarding to error URL page");
				request.getRequestDispatcher("/errorParams.jsp").forward(request, response);
			}else{
				ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
				StandardPBEStringEncryptor encryptor = (StandardPBEStringEncryptor) context.getBean("encryptor");
				String decrypt = encryptor.decrypt(id);
				idToSearch = Long.valueOf(decrypt);
				log.debug("Trying dowload all files for messageId "+idToSearch);
				service = context.getBean(ContentService.class);
				contents =  service.getContentList(idToSearch,recipient);
				if(contents==null || contents.size()<1){
					throw new EmptyResultDataAccessException(1);
				}
				// Set the content type based to zip
				response.setContentType("Content-type: text/zip");
				response.setHeader("Content-Disposition",
						"attachment; filename=allFiles.zip");
		
				ServletOutputStream out = response.getOutputStream();
				ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(out));
				Set<String> addedFiles = new HashSet<String>();
				
				for (ContentDTO content : contents) {
					boolean alreadyThere = false;
					for(String addedFile : addedFiles){
						if(addedFile.equalsIgnoreCase(content.getFileName())){
							alreadyThere=true;
							break;
						}
					}
					if(alreadyThere){
						continue;
					}
					addedFiles.add(content.getFileName());
					log.debug("Adding file " + content.getFileName());
					zos.putNextEntry(new ZipEntry(content.getFileName()));
		
					// Get the file
					InputStream fis = null;
					try {
						fis = content.getInputStream();
					} catch (FileNotFoundException fnfe) {
						// If the file does not exists, write an error entry instead of
						// file
						// contents
						zos.write(("ERROR: Could not find file " + content.getFileName())
								.getBytes());
						zos.closeEntry();
						log.warn("Could not find file "
								+ content.getPath());
						continue;
					}
		
					BufferedInputStream fif = new BufferedInputStream(fis);
		
					// Write the contents of the file
					int data = 0;
					while ((data = fif.read()) != -1) {
						zos.write(data);
					}
					fif.close();
		
					zos.closeEntry();
					log.debug("Finished adding file " + content.getFileName());
				}
		
				zos.close();
				
				for (ContentDTO content : contents) {
					service.successContent(content.getIdMessageAttach());
				}
				
			}
		}catch (EmptyResultDataAccessException e){
				log.debug("Content not more available. Forwarding to not available page");
				request.getRequestDispatcher("/contentNotAvailable.jsp").forward(request, response);
		}catch (Exception e) {
			if(contents!=null){
				for (ContentDTO content : contents) {
					try {service.failDownload(content.getIdMessageAttach());}catch(Exception e2){}
				}
			}
			log.error("Exception: "+e.getClass().getName());
			log.error("Message Exception: "+e.getMessage());
			log.debug("Error General. Forwarding to page error general");
			request.getRequestDispatcher("/error.jsp").forward(request, response);
		}finally{
			MDC.put("message", id);
		}
	}
}
