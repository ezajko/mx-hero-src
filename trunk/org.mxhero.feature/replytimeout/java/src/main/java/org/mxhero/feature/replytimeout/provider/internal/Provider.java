package org.mxhero.feature.replytimeout.provider.internal;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MailDateFormat;

import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.business.Mail;
import org.mxhero.engine.commons.mail.business.MailState;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.commons.util.HeaderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature  {

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	
	private static final String LOCALE_PROPERTY="locale";
	private static final String DATE_FORMAT_PROPERTY="date.format";
	private static final String HEADER = "X-mxHero-Actions";
	private static final String HEADER_VALUE = "replyTimeout";
	private static final String REGEX = "(?i).*\\[\\s*mxreply\\s*.*\\]\\s*.*";
	private static final String REGEX_REMOVE = "(?i)\\s*\\[\\s*mxreply\\s*.*\\]\\s*";
	private static final String REGEX_STRICT = "(?i)\\s*\\[\\s*mxreply\\s+(\\d+)\\s*([dh]|[\\.\\/\\-])\\s*(\\d*)\\s*\\]\\s*";
	
	private ReplyTimeoutConfig config;
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String locale = "en_US";
		String dateFormat = "dd/mm";
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getPropertyKey().equals(LOCALE_PROPERTY)){
				locale=property.getPropertyValue();
			} else if(property.getPropertyKey().equals(DATE_FORMAT_PROPERTY)){
				dateFormat=property.getPropertyValue();
			}
		}
		
		coreRule.addEvaluation(new RTOEval());
		coreRule.addAction(new RTOAction(locale, dateFormat,this.getNoReplyEmail(rule.getDomain())));
		
		return coreRule;
	}
	
	public class RTOEval implements Evaluable{

		@Override
		public boolean eval(Mail mail) {
			boolean result =  	mail.getState().equalsIgnoreCase(MailState.DELIVER)
					&& mail.getHeaders()!=null
					&& (mail.getSubject().getSubject().matches(REGEX) ||
					HeaderUtils.parseParameters(mail.getHeaders().getHeaderValue(HEADER), HEADER_VALUE)!=null);
			if(log.isDebugEnabled()){
				log.debug("eval="+result);
				log.debug("match subject="+mail.getSubject().getSubject().matches(REGEX) );
				log.debug("header value="+HeaderUtils.parseParameters(mail.getHeaders().getHeaderValue(HEADER), HEADER_VALUE));
			}
			return result;
		}
		
	}
	
	public class RTOAction implements Actionable{
		
		private String locale = "en_US";
		private String dateFormat = "dd/mm";
		private Calendar replyTimeoutDate = null;
		private String noreplyMail = null;
		
		
		public RTOAction(String locale, String dateFormat, String noreplyMail) {
			this.locale = locale;
			this.dateFormat = dateFormat;
			this.noreplyMail = noreplyMail;
		}

		@Override
		public void exec(Mail mail) {
			boolean hasError=false;
			String[] headerParameters=HeaderUtils.parseParameters(mail.getHeaders().getHeaderValue(HEADER), HEADER_VALUE);
			try{
				if(headerParameters!=null && headerParameters.length>0){
					replyTimeoutDate=Calendar.getInstance();
					replyTimeoutDate.setTimeInMillis(Long.parseLong(headerParameters[0].trim()));
					if(headerParameters.length>1 && headerParameters[1].trim().length()>0){
						locale=headerParameters[2].trim();
					}
				}else{
					Matcher matcher = Pattern.compile(REGEX_STRICT).matcher(mail.getSubject().getSubject());
					if(matcher.find()){
						String dateParameters = matcher.group().trim().replaceFirst("\\[\\s*mxreply\\s*", "").replaceFirst("\\s*\\]", "").trim();
						Calendar calendar =  null;
						if(mail.getHeaders().hasHeader("Date")){
							try {
								calendar=Calendar.getInstance();
								Date date =new MailDateFormat().parse(mail.getHeaders().getHeaderValue("Date"));
								calendar.setTime(date);
							} catch (ParseException e) {}
						}
						if(dateParameters.endsWith("d")){
							int addDays = Integer.parseInt(dateParameters.substring(0,dateParameters.length()-1).replaceAll("-", ""));
							calendar=Calendar.getInstance();
							calendar.add(Calendar.DATE, addDays);
						}else if(dateParameters.endsWith("h")){
							int addHours = Integer.parseInt(dateParameters.substring(0,dateParameters.length()-1).replaceAll("-", ""));
							calendar=Calendar.getInstance();
							calendar.add(Calendar.HOUR_OF_DAY, addHours);
						}else{
							calendar=Calendar.getInstance();
							int day=calendar.get(Calendar.DAY_OF_MONTH);
							int month=calendar.get(Calendar.MONTH);
							int newDay=0;
							int newMonth=0;
	
							if(dateFormat.equalsIgnoreCase("dd/mm")){
								newDay=Integer.parseInt(dateParameters.split("/")[0]);
								newMonth=Integer.parseInt(dateParameters.split("/")[1]);
							}else{
								newDay=Integer.parseInt(dateParameters.split("/")[1]);
								newMonth=Integer.parseInt(dateParameters.split("/")[0]);
							}
							if(newMonth<month || (newMonth==month && newDay<day)){
								calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR)+1);
							}
							calendar.set(Calendar.MONTH, newMonth);
							calendar.set(Calendar.DAY_OF_MONTH, newDay);
						}
						if(calendar!=null){
							replyTimeoutDate=calendar;
						}
					}
				}
				mail.getSubject().setSubject(mail.getSubject().getSubject().replaceFirst(REGEX_REMOVE, ""));
				if(replyTimeoutDate!=null){
					mail.cmd("org.mxhero.engine.plugin.threadlight.command.AddThreadWatch","org.mxhero.feature.replytimeout",replyTimeoutDate.getTimeInMillis()+";"+locale);
				}
			}catch(Exception e){
				log.warn("unhandle error!",e);
				hasError=true;
			}
			if(hasError || replyTimeoutDate==null){
				String text = config.getErrorTemplate(locale);
				log.debug("sending replyText="+text);
				mail.cmd("org.mxhero.engine.plugin.basecommands.command.Reply",new String[]{noreplyMail,mail.getInitialData().getSender().getMail(),text,text} );
			}
		}
		
	}

	public ReplyTimeoutConfig getConfig() {
		return config;
	}

	public void setConfig(ReplyTimeoutConfig config) {
		this.config = config;
	}

}
