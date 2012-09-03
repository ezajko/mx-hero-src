package org.mxhero.engine.plugin.body.internal.command;

import org.jsoup.Jsoup;
import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.body.command.ReadCurrentCommand;
import org.mxhero.engine.plugin.body.command.ReadCurrentResult;
import org.mxhero.engine.plugin.body.internal.patterns.PatternsConfig;
import org.mxhero.engine.plugin.body.internal.read.ReadText;
import org.mxhero.engine.plugin.body.internal.search.MailBodyParts;
import org.mxhero.engine.plugin.body.internal.search.SearchMailBodyParts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("readCurrentCommand")
public class ImplReadCurrentCommand implements ReadCurrentCommand{

	private static Logger log = LoggerFactory.getLogger(ImplReadCurrentCommand.class);
	
	@Autowired
	@Qualifier("htmlReadText")
	private ReadText htmlReadText;

	@Autowired
	@Qualifier("plainReadText")
	private ReadText plainReadText;
	
	public Result exec(MimeMail mail, NamedParameters parameters) {
		ReadCurrentResult result = new ReadCurrentResult();
		result.setParameters(parameters);
		
		try {
			String header = null;
				try{header=mail.getMessage().getHeader(PatternsConfig.MAILER_HEADER)[0];}
				catch(Exception e){}

			MailBodyParts parts = SearchMailBodyParts.search(mail.getMessage());
			if(parts.getHtml()!=null ){
				result.setHtmlText(htmlReadText.read(parts.getHtml().getText(), header));
				result.setHtmlAsPlainText(Jsoup.parse(result.getHtmlText()).text());
				result.setConditionTrue(true);
			}
			if(parts.getPlain()!=null ){
				result.setPlainText(plainReadText.read(parts.getPlain().getText(), header));
				result.setConditionTrue(true);
			}
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
