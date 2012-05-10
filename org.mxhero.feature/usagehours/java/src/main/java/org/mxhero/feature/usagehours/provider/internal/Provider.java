package org.mxhero.feature.usagehours.provider.internal;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.basecommands.command.clone.Clone;
import org.mxhero.engine.plugin.basecommands.command.clone.CloneParameters;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private static Logger log = LoggerFactory.getLogger(Provider.class);
	private static final String ACTION = "action";
	private static final String RETURN_ACTION = "return";
	private static final String DISCARD_ACTION = "discard";
	private static final String ALERT_ACTION = "alert";
	private static final String ALERT_EMAIL = "alert.email";
	private static final String HOUR_LIST = "hour.list";
	private static final String RETURN_TEXT = "return.text";
	private static final String RETURN_TEXT_PLAIN = "return.text.plain";
	private static final String LOCALE = "locale";
	
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String action = "";
		String returnMessage = "";
		String returnMessagePlain = "";
		String emailList = "";
		TimeZone timeZone = TimeZone.getDefault();
		Map<Integer, Boolean[]> hours= new HashMap<Integer, Boolean[]>();
		
		for(RuleProperty property : rule.getProperties()){
			if(property.getKey().equals(ACTION)){
				action=property.getValue();
			}else if (property.getKey().equals(RETURN_TEXT)){
				returnMessage = property.getValue();
			}else if (property.getKey().equals(RETURN_TEXT_PLAIN)){
				returnMessagePlain=StringEscapeUtils.escapeJava(property.getValue());
			}else if(property.getKey().equals(ALERT_EMAIL)){
				emailList = property.getValue();
			}else if(property.getKey().equals(HOUR_LIST)){
				decodeHours(property.getValue(),hours);
			}else if(property.getKey().equals(LOCALE)){
				timeZone = TimeZone.getTimeZone(property.getValue());
			}
		}
			coreRule.addEvaluation(new UHEvaluation(coreRule.getGroup(),hours,timeZone));
			coreRule.addAction(new UHAction(coreRule.getId(),coreRule.getGroup(),action,returnMessage,returnMessagePlain,getNoReplyEmail(rule.getDomain()),emailList));
		
		return coreRule;
	}

	private void decodeHours(String hour, Map<Integer, Boolean[]> hours){
		if(hour!=null && !hour.isEmpty()){
			Boolean[] week = new Boolean[]{false,false,false,false,false,false,false};
			String[] hourArray = hour.split(";");
			try{
				Integer hourNumber = Integer.parseInt(hourArray[0]);
				for(int i=1;i<hourArray.length && i<8;i++){
					week[i-1]=Boolean.parseBoolean(hourArray[i]);
				}
				hours.put(hourNumber, week);
			}catch(Exception e){}
		}
	}
	
	private class UHEvaluation implements Evaluable{

		private String group="";
		private Map<Integer, Boolean[]> hours;
		private TimeZone timeZone = TimeZone.getDefault();
		
		public UHEvaluation(String group,Map<Integer, Boolean[]> hours, TimeZone timeZone) {
			this.group = group;
			this.hours = hours;
			this.timeZone = timeZone;
		}

		public boolean eval(Mail mail) {
			if(mail.getProperties().containsKey("org.mxhero.feature.usagehours."+group)){
				return false;
			}
			log.debug("timezone:"+timeZone.getID());
			Calendar sentCalendar = Calendar.getInstance();
			sentCalendar.setTime(mail.getSentDate());
			sentCalendar.setTimeZone(timeZone);
			log.debug("sentDate:"+sentCalendar.toString());
			log.debug("hours:"+hours.toString());
			log.debug("sentDate hour:"+sentCalendar.get(Calendar.HOUR_OF_DAY));
			log.debug("sentDate dayoftheweek:"+(sentCalendar.get(Calendar.DAY_OF_WEEK)-1));
			boolean result = !hours.get(sentCalendar.get(Calendar.HOUR_OF_DAY))[sentCalendar.get(Calendar.DAY_OF_WEEK)-1];
			log.debug("result:"+result);
			return result;
		}
		
	}
	
	private class UHAction implements Actionable{
		private Integer ruleId;
		private String group;
		private String action;
		private String returnText;
		private String returnTextPlain;
		private String replyMail;
		private String emailList;
		
		public UHAction(Integer ruleId, String group, String action,
				String returnText, String returnTextPlain, String replyMail,
				String emailList) {
			super();
			this.ruleId = ruleId;
			this.group = group;
			this.action = action;
			this.returnText = returnText;
			this.returnTextPlain = returnTextPlain;
			this.replyMail = replyMail;
			this.emailList = emailList;
		}

		public void exec(Mail mail) {
			mail.getProperties().put("org.mxhero.feature.usagehours."+group, ruleId.toString());
			if(action.equalsIgnoreCase(RETURN_ACTION)){
				log.debug("return");
				ReplyParameters replyParameter = new ReplyParameters(replyMail,returnTextPlain,returnText);
				replyParameter.setRecipient(mail.getSender().getMail());
				mail.cmd(Reply.class.getName(),replyParameter);
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("email.blocked", "org.mxhero.feature.usagehours"));
				log.debug("dropped:"+mail.drop("org.mxhero.feature.usagehours"));
			}else if(action.equalsIgnoreCase(DISCARD_ACTION)){
				log.debug("dropped:"+mail.drop("org.mxhero.feature.usagehours"));
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("email.blocked", "org.mxhero.feature.usagehours"));	
			}else if(action.equalsIgnoreCase(ALERT_ACTION)){
				for(String individualMail : emailList.split(",")){
					log.debug("sending to:"+individualMail);
					try {
						InternetAddress emailAddress = new InternetAddress(individualMail,false);
						if(!mail.getProperties().containsKey("redirected:"+emailAddress.getAddress())){
							mail.getProperties().put("redirected:"+emailAddress.getAddress(),ruleId.toString());
							mail.cmd(Clone.class.getName(), new CloneParameters(mail.getSender().getMail(), emailAddress.getAddress()));	
						}
					} catch (AddressException e) {
						log.warn("wrong email address",e);
					}
				}
			}
			mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.usagehours", Boolean.TRUE.toString()));
			if(mail.getSender().getDomain().hasAlias(group)){
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.usagehours.email",mail.getSender().getMail()));
			}else if(mail.getRecipient().getDomain().hasAlias(group)){
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.usagehours.email",mail.getRecipient().getMail()));
			}else if(mail.getSender().getDomain().getManaged()){
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.usagehours.email",mail.getSender().getMail()));
			}else if(mail.getRecipient().getDomain().getManaged()){
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.usagehours.email",mail.getRecipient().getMail()));
			}
		}
		
	}
}
