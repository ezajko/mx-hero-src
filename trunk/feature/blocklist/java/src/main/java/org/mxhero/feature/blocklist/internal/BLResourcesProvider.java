package org.mxhero.feature.blocklist.internal;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.mxhero.engine.domain.feature.Rule;
import org.mxhero.engine.domain.feature.RuleProperty;
import org.mxhero.engine.domain.provider.StreamDRLProvider;

public class BLResourcesProvider extends StreamDRLProvider{

	private static final String HEADER_FILE = "header.drl";
	private static final String RULE_FILE = "rule.drl";
	
	private static final String PACKAGE_NAME_TAG = "$#PACKAGE_NAME#$";
	private static final String RULE_NAME = "$#RULE_NAME#$";
	private static final String AGENDA_GROUP = "$#AGENDA_GROUP#$";
	private static final String SALIENCE = "$#SALIENCE#$";
	
	private static final String CHECK_DOMAINS = "$#CHECK_DOMAINS#$";
	private static final String CHECK_EMAILS = "$#CHECK_EMAILS#$";

	private static final String NOREPLAY_MAIL = "$#NOREPLAY_MAIL#$";
	private static final String REPLAY_COMMAND_TEXT = "Result $replayResult = $mail.cmd(\"org.mxhero.engine.plugin.basecommands.command.Replay\",new String[]{\"$#NOREPLAY_MAIL#$\",\"$#REPLAY_TEXT#$\",RulePhase.SEND,$initialData.getSender().getMail()} );";
	private static final String REPLAY_COMMAND = "$#REPLAY_COMMAND#$";
	private static final String REPLAY_TEXT = "$#REPLAY_TEXT#$";
	
	private static final String EMAIL_LIST = "email.list";
	private static final String ACTION_SELECTION = "action.selection";
	private static final String ACTION_DISCARD = "discard";
	private static final String ACTION_RETURN = "return";
	private static final String RETURN_TEXT = "return.text";

	
	protected StringBuilder processHeader(String domain) throws IOException {
		StringBuilder sb = streamToBuilder(BLResourcesProvider.class.getClassLoader().getResourceAsStream(HEADER_FILE));
		
		sb.replace(sb.indexOf(PACKAGE_NAME_TAG), sb.indexOf(PACKAGE_NAME_TAG)+PACKAGE_NAME_TAG.length(), getPackageName(domain));
		
		return sb;
	}

	protected StringBuilder processRule(String domain, Rule rule) throws IOException{
		
	StringBuilder sb = streamToBuilder(BLResourcesProvider.class.getClassLoader().getResourceAsStream(RULE_FILE));
	String action = "";
	String returnText = "";
	Set<String> accounts = new HashSet<String>();
	Set<String> domains = new HashSet<String>();
	
	for(RuleProperty property : rule.getProperties()){
		if(property.getPropertyKey().equals(ACTION_SELECTION)){
			action=property.getPropertyValue();
		} else if (property.getPropertyKey().equals(EMAIL_LIST)){
			String value =  StringEscapeUtils.escapeJava(property.getPropertyValue().trim());
			if(value.startsWith("@")){
				domains.add(value.replace("@", ""));
			}else{
				accounts.add(value);
			}
		} else if (property.getPropertyKey().equals(RETURN_TEXT)){
			returnText = StringEscapeUtils.escapeJava(property.getPropertyValue());
		} 
	}
	
	sb.replace(sb.indexOf(RULE_NAME), sb.indexOf(RULE_NAME)+RULE_NAME.length(), rule.getId().toString());		
	sb.replace(sb.indexOf(RULE_NAME), sb.indexOf(RULE_NAME)+RULE_NAME.length(), rule.getId().toString());
	sb.replace(sb.indexOf(RULE_NAME), sb.indexOf(RULE_NAME)+RULE_NAME.length(), rule.getId().toString());
	
	sb.replace(sb.indexOf(AGENDA_GROUP), sb.indexOf(AGENDA_GROUP)+AGENDA_GROUP.length(), getAgendaGroup(domain, rule));
	sb.replace(sb.indexOf(SALIENCE), sb.indexOf(SALIENCE)+SALIENCE.length(), ""+(getFeature().getBasePriority()+ getFromToConditions(rule.getFromDirection(), rule.getToDirection(), sb)));

	
	if(domains.size()>0){
		String domainsExceptions = "eval( $initialData.getSender().getDomain().hasAlias(new String[]{";
		for(String domainException : domains){
			domainsExceptions = domainsExceptions+"\""+domainException+"\",";
		}
		domainsExceptions = domainsExceptions.substring(0,domainsExceptions.length()-1);
		domainsExceptions = domainsExceptions+" } ) == true )";
		sb.replace(sb.indexOf(CHECK_DOMAINS), sb.indexOf(CHECK_DOMAINS)+CHECK_DOMAINS.length(), domainsExceptions);
	} else {
		sb.replace(sb.indexOf(CHECK_DOMAINS), sb.indexOf(CHECK_DOMAINS)+CHECK_DOMAINS.length(), "");
	}

	if(accounts.size()>0){
		String accountExceptions = "eval( $initialData.getSender().hasAlias(new String[]{";
		for(String accountException : accounts){
			accountExceptions = accountExceptions+"\""+accountException+"\",";
		}
		accountExceptions = accountExceptions.substring(0,accountExceptions.length()-1);
		accountExceptions = accountExceptions+" } ) == true) ";
		sb.replace(sb.indexOf(CHECK_EMAILS), sb.indexOf(CHECK_EMAILS)+CHECK_EMAILS.length(), accountExceptions);
		
	} else {
		sb.replace(sb.indexOf(CHECK_EMAILS), sb.indexOf(CHECK_EMAILS)+CHECK_EMAILS.length(), "");
	}
	
	if(action.equals(ACTION_DISCARD)){
		sb.replace(sb.indexOf(REPLAY_COMMAND), sb.indexOf(REPLAY_COMMAND)+REPLAY_COMMAND.length(), "");
	} else if(action.equals(ACTION_RETURN)){
		String noreplayMail = (rule.getDomain()!=null)?"noreplay@"+rule.getDomain():"noreplay@mxhero.com";
		sb.replace(sb.indexOf(REPLAY_COMMAND), sb.indexOf(REPLAY_COMMAND)+REPLAY_COMMAND.length(), REPLAY_COMMAND_TEXT);
		sb.replace(sb.indexOf(NOREPLAY_MAIL), sb.indexOf(NOREPLAY_MAIL)+NOREPLAY_MAIL.length(), noreplayMail);
		sb.replace(sb.indexOf(REPLAY_TEXT), sb.indexOf(REPLAY_TEXT)+REPLAY_TEXT.length(), returnText);
	}

	return sb;
	}

}
