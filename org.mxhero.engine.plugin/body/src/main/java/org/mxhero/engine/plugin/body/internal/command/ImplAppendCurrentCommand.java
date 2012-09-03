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
