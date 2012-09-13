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

package org.mxhero.engine.plugin.body.internal.command;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.body.command.AppendCurrentCommand;
import org.mxhero.engine.plugin.body.command.AppendCurrentParameters;
import org.mxhero.engine.plugin.body.internal.insert.InsertText;
import org.mxhero.engine.plugin.body.internal.patterns.PatternsConfig;
import org.mxhero.engine.plugin.body.internal.search.MailBodyParts;
import org.mxhero.engine.plugin.body.internal.search.SearchMailBodyParts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("appendCurrentCommand")
public class ImplAppendCurrentCommand implements AppendCurrentCommand{

	private static Logger log = LoggerFactory.getLogger(ImplAppendCurrentCommand.class);
	
	@Autowired
	@Qualifier("htmlInsertText")
	private InsertText htmlInsertText;

	@Autowired
	@Qualifier("plainInsertText")
	private InsertText plainInsertText;
	
	public Result exec(MimeMail mail, NamedParameters parameters) {
		Result result = new Result();
		result.setParameters(parameters);
		AppendCurrentParameters appendCurrentParameters = new AppendCurrentParameters(parameters);
		
		if (appendCurrentParameters.getHtmlText()==null
				&& appendCurrentParameters.getPlainText()==null){
			result.setAnError(true);
			result.setMessage("wrong ammount of params");
			return result;
		}
		
		try {
			String header = null;
				try{header=mail.getMessage().getHeader(PatternsConfig.MAILER_HEADER)[0];}
				catch(Exception e){}

			MailBodyParts parts = SearchMailBodyParts.search(mail.getMessage());
			if(parts.getHtml()!=null && appendCurrentParameters.getHtmlText()!=null){
				parts.getHtml().setText(htmlInsertText.insert(parts.getHtml().getText(), appendCurrentParameters.getHtmlText(), header));
			}
			if(parts.getPlain()!=null && appendCurrentParameters.getPlainText()!=null){
				parts.getPlain().setText(plainInsertText.insert(parts.getPlain().getText(), appendCurrentParameters.getPlainText(), header));
			}
			result.setConditionTrue(true);
			result.setAnError(false);
		} catch (Exception e) {
			log.warn("error while processing",e);
			result.setAnError(true);
			result.setMessage("error while processing: "+e.getMessage());
			return result;
		} 
		
		return result;
	}

}
