package org.mxhero.feature.disclaimercontract.provider.internal;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.mxhero.engine.commons.feature.Rule;
import org.mxhero.engine.commons.feature.RuleProperty;
import org.mxhero.engine.commons.mail.api.Body;
import org.mxhero.engine.commons.mail.api.Mail;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.commons.rules.Actionable;
import org.mxhero.engine.commons.rules.CoreRule;
import org.mxhero.engine.commons.rules.Evaluable;
import org.mxhero.engine.commons.rules.provider.RulesByFeature;
import org.mxhero.engine.plugin.basecommands.command.create.Create;
import org.mxhero.engine.plugin.basecommands.command.create.CreateParameters;
import org.mxhero.engine.plugin.basecommands.command.reply.Reply;
import org.mxhero.engine.plugin.basecommands.command.reply.ReplyParameters;
import org.mxhero.engine.plugin.disclaimercontract.command.RequestContractApproval;
import org.mxhero.engine.plugin.disclaimercontract.command.RequestContractApprovalParameters;
import org.mxhero.engine.plugin.postfixconnector.service.PostFixConnectorOutputService;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Provider extends RulesByFeature{

	private final static String SENDER_KEY = "mxsender";
	private final static String RECIPIENT_KEY = "mxrecipient";
	private static Logger log = LoggerFactory.getLogger(Provider.class);
	private final static String RETURN_MESSAGE = "return.message";
	private final static String RETURN_MESSAGE_PLAIN = "return.message.plain";
	private static final String EMAIL_LIST = "email.list";
	private DisclaimerContractConfig config;
	
	protected CoreRule createRule(Rule rule) {
		CoreRule coreRule = this.getDefault(rule);
		String returnMessage = "";
		String returnMessagePlain = "";
		Set<String> accounts = new HashSet<String>();
		Set<String> domains = new HashSet<String>();
		String locale = "en_US";
		
		for(RuleProperty property : rule.getProperties()){
			if (property.getKey().equals(RETURN_MESSAGE)){
				returnMessage = property.getValue();
			}else if (property.getKey().equals(RETURN_MESSAGE_PLAIN)){
				returnMessagePlain=property.getValue();
			}else if (property.getKey().equals(EMAIL_LIST)){
				String value =  StringEscapeUtils.escapeJava(property.getValue().trim());
				if(value.startsWith("@")){
					domains.add(value.replace("@", "").toLowerCase());
				}else{
					accounts.add(value.toLowerCase());
				}
			}
		}
		
		coreRule.addEvaluation(new DCEvaluation(coreRule.getGroup()));
		log.debug("returnMessagePlain="+returnMessagePlain.trim().isEmpty());
		log.debug("returnMessage="+returnMessage.trim().isEmpty());
		coreRule.addAction(new DCAction(rule.getId(),coreRule.getPriority(),coreRule.getGroup(),returnMessage,returnMessagePlain,locale,this.getNoReplyEmail(rule.getDomain())));
		return coreRule;
	}

	private class DCEvaluation implements Evaluable{

		private String group;

		public DCEvaluation(String group) {
			this.group = group;
		}

		public boolean eval(Mail mail) {
			boolean result = mail.getStatus().equals(Mail.Status.deliver) 
					&& !mail.getProperties().containsKey("org.mxhero.feature.disclaimercontract."+group);
			log.debug("result:"+result);
			return result;
		}
		
	}
	
	private class DCAction implements Actionable{
		private Integer ruleId;
		private String group;
		private String returnMessage = "";
		private String returnMessagePlain = "";
		private Integer priority = null;
		private String locale = null;
		private String noreplyMail = null;

		public DCAction(Integer ruleId, Integer priority, String group, String returnMessage,
				String returnMessagePlain, String locale, String noreplyMail) {
			this.ruleId = ruleId;
			this.group = group;
			this.returnMessage = returnMessage;
			this.returnMessagePlain = returnMessagePlain;
			this.priority = priority;
			this.locale = locale;
			this.noreplyMail = noreplyMail;
		}

		public void exec(Mail mail) {
			try{
				returnMessage=replaceTextVars(mail,returnMessage, Body.AddTextPartType.plain,false,null);
				returnMessagePlain=replaceTextVars(mail,returnMessagePlain, Body.AddTextPartType.plain,false,null);
				mail.getProperties().put("org.mxhero.feature.disclaimercontract."+group, ruleId.toString());
				RequestContractApprovalParameters params = new RequestContractApprovalParameters(ruleId.longValue(), returnMessagePlain, returnMessage);
				params.setRulePriority(priority);
				Result result = mail.cmd(RequestContractApproval.class.getName(), params);
				if(result.isAnError()){
					log.warn("error="+result.getMessage());
					throw new RuntimeException(result.getMessage());
				}else{
					if(!Boolean.parseBoolean(result.getParameters().get(RequestContractApprovalParameters.APPROVED).toString())){
						log.debug("not aprroved, email saved, drop it.");
						mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.disclaimercontract", Boolean.TRUE.toString()));
						returnMessage=replaceTextVars(mail,returnMessage, Body.AddTextPartType.html,true,(Long)(result.getParameters().get(RequestContractApprovalParameters.REQUEST_ID)));
						returnMessagePlain=replaceTextVars(mail,returnMessagePlain, Body.AddTextPartType.plain,true,(Long)(result.getParameters().get(RequestContractApprovalParameters.REQUEST_ID)));
						mail.getHeaders().addHeader("x-mxHero-DisclaimerContract", "ruleId="+ruleId+"; group="+group+"; status=pending");
						CreateParameters createParameters = new CreateParameters(noreplyMail,mail.getRecipient().getMail(), mail.getSubject(), returnMessagePlain, PostFixConnectorOutputService.class.getName());
						createParameters.setTextHtml(returnMessage);
						createParameters.setInReplyMessagId(mail.getId());
						createParameters.setPhase(Mail.Phase.out);
						Result createResult = mail.cmd(Create.class.getName(), createParameters);
						log.debug("contract sent: "+createResult.isConditionTrue());
						mail.redirect("org.mxhero.feature.disclaimercontract");
					}else{
						log.debug("already approved, let it go.");
						mail.getHeaders().addHeader("x-mxHero-DisclaimerContract", "ruleId="+ruleId+"; group="+group+"; status=approved");
					}
				}
			}catch (Exception e){
				log.warn(e.getMessage(),e);
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.disclaimercontract", Boolean.FALSE.toString()));
				mail.cmd(LogStatCommand.class.getName(), new LogStatCommandParameters("org.mxhero.feature.disclaimercontract.error", e.getMessage()));
				mail.drop("org.mxhero.feature.disclaimercontract.error");
				String htmlErrorText = config.getErrorTemplate(locale);
				String plainErrorText = Jsoup.parse(htmlErrorText).text();
				ReplyParameters replyParameters = new ReplyParameters(noreplyMail, plainErrorText, htmlErrorText);
				replyParameters.setRecipient(mail.getSender().getMail());
				mail.cmd(Reply.class.getName(), replyParameters);
			}
		}
		
		private String replaceTextVars(Mail mail, String content, Body.AddTextPartType type, boolean links, Long id) throws UnsupportedEncodingException {
			String tagregex = "\\$\\{[^\\{]*\\}";
			Pattern p2 = Pattern.compile(tagregex);
			StringBuffer sb = new StringBuffer();
			Matcher m2 = p2.matcher(content);
			int lastIndex = 0;
			String encode = null;
			if(id!=null){
				String encrypted = config.getEncryptor().encrypt(id.toString());
				encode = URLEncoder.encode(encrypted,"ASCII");
			}
			while (m2.find()) {
				lastIndex = m2.end();
				String key = content.substring(m2.start() + 2, m2.end() - 1);

				if (key.equalsIgnoreCase(SENDER_KEY)) {
					m2.appendReplacement(sb, mail.getSender().getMail());
				} else if (key.equalsIgnoreCase(RECIPIENT_KEY)) {
					m2.appendReplacement(sb, mail.getRecipient().getMail());
				} else if(key.startsWith("accept_always:") && links) {
					if(type.equals(Body.AddTextPartType.html)){
						m2.appendReplacement(sb, "<a href=\"#URL#\">#TEXT#</a>".replace("#URL#", config.getExternalServiceBaseUrl()+"?id="+encode+"&type=contract").replace("#TEXT#", key.split(":")[1]));
					}else{
						m2.appendReplacement(sb, "#TEXT# #URL#".replace("#TEXT#", key.split(":")[1]).replace("#URL#", config.getExternalServiceBaseUrl()+"?id="+encode+"&type=contract"));
					}
				} else if(key.startsWith("accept:") && links) {
					if(type.equals(Body.AddTextPartType.html)){
						m2.appendReplacement(sb, "<a href=\"#URL#\">#TEXT#</a>".replace("#URL#", config.getExternalServiceBaseUrl()+"?id="+encode+"&type=one").replace("#TEXT#", key.split(":")[1]));
					}else{
						m2.appendReplacement(sb, "#TEXT# #URL#".replace("#TEXT#", key.split(":")[1]).replace("#URL#", config.getExternalServiceBaseUrl()+"?id="+encode+"&type=one"));
					}
				} else if(key.startsWith("reject:") && links) {
					if(type.equals(Body.AddTextPartType.html)){
						m2.appendReplacement(sb, "<a href=\"#URL#\">#TEXT#</a>".replace("#URL#", config.getExternalRejectServiceBaseUrl()+"?id="+encode).replace("#TEXT#", key.split(":")[1]));
					}else{
						m2.appendReplacement(sb, "#TEXT# #URL#".replace("#TEXT#", key.split(":")[1]).replace("#URL#", config.getExternalRejectServiceBaseUrl()+"?id="+encode));
					}
				} else {
					// this should be a header
					String value = mail.getHeaders().getHeaderValue(key);
					if (value != null) {
						m2.appendReplacement(sb, value);
					}else{
						m2.appendReplacement(sb,"\\${"+key+"}");
					}
				}
			}
			sb.append(content.substring(lastIndex));
			return sb.toString();
		}
	}

	public DisclaimerContractConfig getConfig() {
		return config;
	}

	public void setConfig(DisclaimerContractConfig config) {
		this.config = config;
	}

}
