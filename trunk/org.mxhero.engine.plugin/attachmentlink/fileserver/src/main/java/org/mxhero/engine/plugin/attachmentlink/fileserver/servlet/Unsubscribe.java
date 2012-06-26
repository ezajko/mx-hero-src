/**
 * 
 */
package org.mxhero.engine.plugin.attachmentlink.fileserver.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.mxhero.engine.plugin.attachmentlink.fileserver.service.ContentService;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author mmarmol
 *
 */
public class Unsubscribe extends HttpServlet {

	private static Logger log = Logger.getLogger(Unsubscribe.class);
	
	/**
	 * 
	 */
	public Unsubscribe() {
		// TODO Auto-generated constructor stub
	}
	
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
	
	private void doAction(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		ContentService service = null;
		Long idToSearch = null;
		String id = req.getParameter("id");
		MDC.put("message", id);
		try {
			if(StringUtils.isEmpty(id)){
				log.debug("Error. No params in URL present. Forwarding to error URL page");
				req.getRequestDispatcher("/errorParams.jsp").forward(req, resp);
			}else{
				ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
				StandardPBEStringEncryptor encryptor = (StandardPBEStringEncryptor) context.getBean("encryptor");
				String decrypt = encryptor.decrypt(id);
				idToSearch = Long.valueOf(decrypt);
				log.debug("Trying update files for messageId "+idToSearch);
				service = context.getBean(ContentService.class);
				service.unsubscribe(idToSearch);
				req.getRequestDispatcher("/unsubscribe.jsp").forward(req, resp);
			}
		}catch (EmptyResultDataAccessException e){
			log.debug("Content not more available. Forwarding to not available page");
			req.getRequestDispatcher("/contentNotAvailable.jsp").forward(req, resp);
		} catch (Exception e) {
			log.error("Exception: "+e.getClass().getName());
			log.error("Message Exception: "+e.getMessage());
			log.debug("Error General. Forwarding to page error general");
			req.getRequestDispatcher("/error.jsp").forward(req, resp);
		}finally{
			MDC.put("message", id);
		}
	}

}
