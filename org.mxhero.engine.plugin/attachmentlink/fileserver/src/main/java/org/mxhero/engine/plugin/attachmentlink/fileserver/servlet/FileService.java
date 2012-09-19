/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.plugin.attachmentlink.fileserver.servlet;

import java.io.IOException;
import java.io.InputStream;

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
import org.mxhero.engine.plugin.attachmentlink.fileserver.exceptions.NotAllowedToSeeContentException;
import org.mxhero.engine.plugin.attachmentlink.fileserver.service.ContentService;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author royojp
 *
 */
public class FileService extends HttpServlet {
	
	private static Logger log = Logger.getLogger(FileService.class);

	/**
	 * 
	 */
	public FileService() {
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
		InputStream in = null;
		Long idToSearch = null;
		String id = req.getParameter("id");
		String type = req.getParameter("type");
		String email = req.getParameter("email");
		MDC.put("message", id);
		try {
			if(StringUtils.isEmpty(id) || StringUtils.isEmpty(type)){
				log.debug("Error. No params in URL present. Forwarding to error URL page");
				req.getRequestDispatcher("/errorParams.jsp").forward(req, resp);
			}else{
				ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
				StandardPBEStringEncryptor encryptor = (StandardPBEStringEncryptor) context.getBean("encryptor");
				String decrypt = encryptor.decrypt(id);
				idToSearch = Long.valueOf(decrypt);
				log.debug("Trying download to id "+idToSearch);
				service = context.getBean(ContentService.class);
				ContentDTO content = service.getContent(idToSearch, email);
				
				if(content.hasPublicUrl()){
					service.successContent(idToSearch);
					resp.sendRedirect(content.getPublicUrl());
				}else{
					ServletOutputStream outputStream = resp.getOutputStream();
					resp.setContentLength(content.getLength());
					resp.setContentType(content.getContentType(type));
					if(!content.hasToBeOpen(type)){
						resp.setHeader("Content-Type", "application/octet-stream");
						resp.addHeader("Content-Disposition", "attachment; filename=\"" + content.getFileName() + "\"");
					}else{
						resp.addHeader("Content-Disposition", "filename=\"" + content.getFileName() + "\"");
					}
					resp.addHeader("Cache-Control", "no-cache");
					
			        in = content.getInputStream();
			        int BUFF_SIZE = 2048;
			        byte[] buffer = new byte[BUFF_SIZE];
			        int byteCount = 0;
					while ((in != null) && (byteCount = in.read(buffer))!=-1){
				        outputStream.write(buffer, 0, byteCount);
				    } 
					outputStream.flush();
					log.debug("Download completed successfuly");
					service.successContent(idToSearch);
				}
			}
			
		} catch (NotAllowedToSeeContentException e) {
			log.debug("User not more allowed to download content. Forwarding to not allowed page");
			req.getRequestDispatcher("/notAllowed.jsp").forward(req, resp);
		} catch (EmptyResultDataAccessException e){
			log.debug("Content not more available. Forwarding to not available page");
			req.getRequestDispatcher("/contentNotAvailable.jsp").forward(req, resp);
		} catch (Exception e) {
			try {
				service.failDownload(idToSearch);	
			} catch (Exception e2) {
			}
			log.error("Exception: "+e.getClass().getName());
			log.error("Message Exception: "+e.getMessage());
			log.debug("Error General. Forwarding to page error general");
			req.getRequestDispatcher("/error.jsp").forward(req, resp);
		}finally{
			if(in!=null){
				in.close();
			}
			MDC.put("message", id);
		}
	}

}
