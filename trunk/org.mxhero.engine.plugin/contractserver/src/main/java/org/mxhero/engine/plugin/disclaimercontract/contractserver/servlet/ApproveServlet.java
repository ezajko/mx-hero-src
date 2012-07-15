package org.mxhero.engine.plugin.disclaimercontract.contractserver.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.codehaus.jackson.map.ObjectMapper;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.service.AlreadyApprovedException;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.service.AlreadyRejectedException;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.service.RequestNotAvailableException;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.service.RequestService;
import org.mxhero.engine.plugin.disclaimercontract.contractserver.vo.RequestVO;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ApproveServlet  extends HttpServlet{

	private static Logger log = Logger.getLogger(ApproveServlet.class);
	
	private ObjectMapper mapper = new ObjectMapper();
	
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

		Long idToSearch = null;
		String id = req.getParameter("id");
		String type = req.getParameter("type");
		MDC.put("message", id);

		try {
			if(StringUtils.isEmpty(id) || StringUtils.isEmpty(type)){
				log.debug("Error. No params in URL present. Forwarding to error URL page");
				req.getRequestDispatcher("/wrong_parameters.jsp").forward(req, resp);
			}else{
				ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
				StandardPBEStringEncryptor encryptor = context.getBean(StandardPBEStringEncryptor.class);
				RequestService service = context.getBean(RequestService.class);
				String decrypt = encryptor.decrypt(id);
				idToSearch = Long.valueOf(decrypt);
				
				Enumeration<String> headerNames = req.getHeaderNames();
				Map<String, Object> headerMap = new HashMap<String, Object>();
				while (headerNames.hasMoreElements()) {
					String headerName = headerNames.nextElement();
					Enumeration<String> headers = req.getHeaders(headerName);
					while (headers.hasMoreElements()) {
						String headerValue = headers.nextElement();
						headerMap.put(headerName,headerValue);
					}
				}
				headerMap.put("locale", req.getLocale().toString());
				headerMap.put("method", req.getMethod());
				headerMap.put("queryString", req.getQueryString());
				headerMap.put("remoteAddr", req.getRemoteAddr());
				headerMap.put("remoteHost", req.getRemoteHost());
				headerMap.put("remotePort", req.getRemotePort());
				headerMap.put("requestURL", req.getRequestURL());
				headerMap.put("requestedSessionId", req.getRequestedSessionId());

				service.approve(idToSearch, type, mapper.writeValueAsString(headerMap));
				if(type.equalsIgnoreCase(RequestVO.CONTRACT_TYPE)){
					req.getRequestDispatcher("/accept_always.jsp").forward(req, resp);
				}else{
					req.getRequestDispatcher("/accept_once.jsp").forward(req, resp);
				}
			}
		}catch(RequestNotAvailableException e){
			log.error("Exception: "+e.getClass().getName()+ " id="+idToSearch);
			req.getRequestDispatcher("/request_not_exists.jsp").forward(req, resp);
		}catch(AlreadyApprovedException e){
			log.error("Exception: "+e.getClass().getName()+ " id="+idToSearch);
			req.getRequestDispatcher("/request_already_approved.jsp").forward(req, resp);
		}catch(AlreadyRejectedException e){
			log.error("Exception: "+e.getClass().getName()+ " id="+idToSearch);
			req.getRequestDispatcher("/request_already_rejected.jsp").forward(req, resp);
		}catch (Exception e) {
			log.error("Exception: "+e.getClass().getName());
			log.error("Message Exception: "+e.getMessage());
			log.debug("Error General. Forwarding to page error general");
			req.getRequestDispatcher("/unexpected_error.jsp").forward(req, resp);
		}finally{
			MDC.put("message", id);
		}
	}
}
