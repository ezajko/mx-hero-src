/**
 * 
 */
package org.mxhero.engine.plugin.attachmentlink.fileserver.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @author chelo
 *
 */
public class UnsuscribeServlet extends HttpServlet {

	/**
	 * 
	 */
	public UnsuscribeServlet() {
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
		ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		//table message_attach   
		//column msg.process_ack_download
	}

}
