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

package org.mxhero.engine.plugin.spamd.internal.command;

import javax.mail.MessagingException;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.spamd.command.SpamScan;
import org.mxhero.engine.plugin.spamd.command.SpamScanParameters;
import org.mxhero.engine.plugin.spamd.command.SpamScanResult;
import org.mxhero.engine.plugin.spamd.internal.scanner.SpamdScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements the SpamScan interface. This command send the mail to a spam
 * assassin daemon to scan for spam. First paramameter value is a boolean, if
 * true add headers to the mail if false it doesn't. Second parameter is the
 * Flag param name. Third parameter is the Status param name.
 * 
 * @author mmarmol
 */
public class SpamAssassimScan implements SpamScan {

	private static Logger log = LoggerFactory.getLogger(SpamAssassimScan.class);

	private String hostName = "localhost";
	private Integer port = 783;
    private String statusMailAttributeName = "X-Spam-Status";
    private String flagMailAttributeName = "X-Spam-Flag";

	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail,
	 *      java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {
		SpamScanResult result = new SpamScanResult();
		String prefix = "";
		boolean addHeaders = true;
		String statusHeaderName = SpamdScanner.STATUS_MAIL_ATTRIBUTE_NAME;
		String flagHeaderName = SpamdScanner.FLAG_MAIL_ATTRIBUTE_NAME;
		SpamScanParameters ssParameters = new SpamScanParameters(parameters);
		if (getFlagMailAttributeName()!=null && !getFlagMailAttributeName().isEmpty()) {
			flagHeaderName = getFlagMailAttributeName();
		}
		if (getStatusMailAttributeName()!=null && getStatusMailAttributeName().isEmpty()) {
			statusHeaderName = getStatusMailAttributeName();
		}
		if(ssParameters.getPrefix()!=null &&
				ssParameters.getPrefix().trim().length()>0){
			prefix = ssParameters.getPrefix().trim()+" ";
		}
		if(ssParameters.getAddHeaders()!=null){
			addHeaders=ssParameters.getAddHeaders();
		}
		if(ssParameters.getFlagHeader()!=null &&
				!ssParameters.getFlagHeader().trim().isEmpty()){
			flagHeaderName=ssParameters.getFlagHeader();
		}
		if(ssParameters.getStatusHeader()!=null &&
				!ssParameters.getStatusHeader().trim().isEmpty()){
			statusHeaderName=ssParameters.getStatusHeader();
		}


		SpamdScanner scanner = new SpamdScanner(getHostName(), getPort());
		double hits = -1;
		try {
			result.setConditionTrue(scanner.scan(mail.getMessage()));
			try {
				hits = Double.parseDouble(scanner.getHits());
			} catch (NumberFormatException e) {
				log.warn("wrong hits format");
			}
			result.setHits(hits);
			result.setMessage(scanner.getHeadersAsAttribute().get(
					SpamdScanner.STATUS_MAIL_ATTRIBUTE_NAME));
			if (addHeaders) {
				mail.getMessage().setHeader(
						statusHeaderName,
						scanner.getHeadersAsAttribute().get(
								SpamdScanner.STATUS_MAIL_ATTRIBUTE_NAME));
				mail.getMessage().setHeader(
						flagHeaderName,
						scanner.getHeadersAsAttribute().get(
								SpamdScanner.FLAG_MAIL_ATTRIBUTE_NAME));
			}
			if(result.isConditionTrue()){
				mail.getMessage().setSubject(prefix+mail.getMessage().getSubject());
			}
			mail.getMessage().saveChanges();

		} catch (MessagingException e) {
			log.warn("error while scanning for spam");
		}
		return result;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getStatusMailAttributeName() {
		return statusMailAttributeName;
	}

	public void setStatusMailAttributeName(String statusMailAttributeName) {
		this.statusMailAttributeName = statusMailAttributeName;
	}

	public String getFlagMailAttributeName() {
		return flagMailAttributeName;
	}

	public void setFlagMailAttributeName(String flagMailAttributeName) {
		this.flagMailAttributeName = flagMailAttributeName;
	}

}
