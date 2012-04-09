package org.mxhero.feature.replytimeout.provider.internal;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.MailDateFormat;
import javax.mail.internet.ParameterList;

import org.jsoup.Jsoup;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.api.Recipients.RecipientType;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.commons.util.HeaderUtils;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatch;
import org.mxhero.engine.plugin.threadlight.command.AddThreadWatchParameters;
import org.mxhero.feature.replytimeout.provider.internal.config.ReplyTimeoutConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature  {

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	
	public static final String FOLLOWER_ID = "org.mxhero.feature.replytimeout";
	
	private static final String LOCALE_PROPERTY="locale";
	private static final String DATE_FORMAT_PROPERTY="date.format";
	private static final String HEADER = "X-mxHero-Actions";
	private static final String ACTION_VALUE = "replyTimeout";
	private static final String UNIXTIME = "unixtime";
	private static final String LOCALE = "locale";
	public static final String REGEX = "(?i).*\\[\\s*mxreply\\s*.*\\]\\s*.*";
	public static final String REGEX_DEFAULT_DAY = "(?i).*\\[\\s*mxreply\\s*\\]\\s*.*";
	public static final String REGEX_REMOVE = "(?i)\\s*\\[\\s*mxreply\\s*.*\\]\\s*";
	public static final String REGEX_STRICT = "(?i)\\s*\\[\\s*mxreply\\s+(\\d+)\\s*([dhm]|[\\.\\/\\-])\\s*(\\d*)\\s*\\]\\s*";
	
	private ReplyTimeoutConfig config;
	
	@Override
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String locale = "en_US";
		String dateFormat = "dd/mm";
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(LOCALE_PROPERTY)){
				locale=property.getValue();
			} else if(property.getKey().equals(DATE_FORMAT_PROPERTY)){
				dateFormat=property.getValue();
			}
		}
		
		coreRule.addEvaluation(new RTOEval());
		coreRule.addAction(new RTOAction(locale, dateFormat,this.getNoReplyEmail(rule.getDomain())));
		
		return coreRule;
	}
	
	public class RTOEval implements Evaluable{

		@Override
		public boolean eval(Mail mail) {
			Collection<String> values = mail.getHeaders().getHeaderValues(HEADER);
			boolean conditions =  	mail.getStatus().equals(Mail.Status.deliver)
					&& mail.getHeaders()!=null
					&& (mail.getSubject().matches(REGEX) ||
						(values!=null && values.size()>0 && HeaderUtils.getParametersList(values.toArray(new String[0]), ACTION_VALUE)!=null));
			boolean isInTO = false;
			Collection<String> toRecipients = mail.getRecipients().getRecipients(RecipientType.to);
			if(toRecipients!=null){
				for(String recipient : toRecipients){
					if(mail.getRecipient().hasAlias(recipient)){
						isInTO=true;
						break;
					}
				}
			}
			if(log.isDebugEnabled()){
				log.debug("conditions="+conditions);
				log.debug("isInTO="+isInTO);
				log.debug("match subject="+mail.getSubject().matches(REGEX) );
				if(values!=null && values.size()>0){
					log.debug("header value="+HeaderUtils.getParametersList(values.toArray(new String[0]), ACTION_VALUE));
				}
			}
			if(conditions && !isInTO){
				mail.setSubject(mail.getSubject().replaceFirst(REGEX_REMOVE, ""));
			}
			return conditions && isInTO;
		}
		
	}
	
	public class RTOAction implements Actionable{
		
		private String locale = "en_US";
		private String dateFormat = "dd/mm";
		private Calendar replyTimeoutDate = null;
		private String noreplyMail = null;
		private String dateString = null;
		
		
		public RTOAction(String locale, String dateFormat, String noreplyMail) {
			this.locale = locale;
			this.dateFormat = dateFormat;
			this.noreplyMail = noreplyMail;
		}

		@Override
		public void exec(Mail mail) {
			boolean hasError=false;
			Collection<String> values = mail.getHeaders().getHeaderValues(HEADER);
			ParameterList parameterList = null;
			try{
				if(values!=null && values.size()>0){
					parameterList = HeaderUtils.getParametersList(values.toArray(new String[0]), ACTION_VALUE);
					if(parameterList!=null){
						replyTimeoutDate=Calendar.getInstance();
						replyTimeoutDate.setTimeInMillis(Long.parseLong(parameterList.get(UNIXTIME)));
						if(parameterList.get(LOCALE)!=null && !parameterList.get(LOCALE).trim().isEmpty()){
							locale=parameterList.get(LOCALE);
						}
					}
				}else{
					Matcher matcher = Pattern.compile(REGEX_STRICT).matcher(mail.getSubject());
					if(matcher.find()){
						String dateParameters = matcher.group().trim().replaceFirst("(?i)\\[\\s*mxreply\\s*", "").replaceFirst("\\s*\\]", "").trim();
						Calendar calendar =  null;
						if(mail.getHeaders().hasHeader("Date")){
							try {
								dateString=mail.getHeaders().getHeaderValue("Date");
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
						}else if(dateParameters.endsWith("m")){
							int addMinutes = Integer.parseInt(dateParameters.substring(0,dateParameters.length()-1).replaceAll("-", ""));
							calendar=Calendar.getInstance();
							calendar.add(Calendar.MINUTE, addMinutes);							
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
					}else{
						//default day
						if(Pattern.compile(REGEX_DEFAULT_DAY).matcher(mail.getSubject()).find()){
							int addDays = 1;
							Calendar calendar=Calendar.getInstance();
							calendar.add(Calendar.DATE, addDays);
							replyTimeoutDate=calendar;
						}
					}
				}
				if(dateString==null){
					dateString=Calendar.getInstance().getTime().toString();
				}
				if(replyTimeoutDate!=null){
					mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.replytimeout", ""+replyTimeoutDate.getTimeInMillis()));
					mail.cmd(AddThreadWatch.class.getName(), new AddThreadWatchParameters(FOLLOWER_ID,replyTimeoutDate.getTimeInMillis()+";"+locale+";"+noreplyMail+";"+dateString));
				}
			}catch(Exception e){
				log.warn("unhandle error!",e);
				hasError=true;
			}
			if(hasError || replyTimeoutDate==null){
				String text = config.getErrorTemplate(locale);
				log.debug("sending replyText="+text);
				ReplyParameters replyParameters = new ReplyParameters(noreplyMail, Jsoup.parse(text).text(), text);
				replyParameters.setSender(mail.getSender().getMail());
				mail.cmd(Reply.class.getName(), replyParameters);
			}
			mail.setSubject(mail.getSubject().replaceFirst(REGEX_REMOVE, ""));
		}
		
	}

	public ReplyTimeoutConfig getConfig() {
		return config;
	}

	public void setConfig(ReplyTimeoutConfig config) {
		this.config = config;
	}

}
