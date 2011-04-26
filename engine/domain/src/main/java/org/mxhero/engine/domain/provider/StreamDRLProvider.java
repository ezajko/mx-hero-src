package org.mxhero.engine.domain.provider;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import org.mxhero.engine.domain.feature.Rule;
import org.mxhero.engine.domain.feature.RuleDirection;
import org.mxhero.engine.domain.provider.FileResource;
import org.mxhero.engine.domain.provider.Resource;
import org.mxhero.engine.domain.provider.ResourcesByDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StreamDRLProvider extends ResourcesByDomain{

	private static Logger log = LoggerFactory
	.getLogger(StreamDRLProvider.class);
	
	private static final String FILE_EXTENSION = ".drl";
	private static final String RESOURCE_TYPE = "DRL";
	
	public static final String DOMAIN = "domain";
	public static final String GROUP = "group";
	public static final String INDIVIDUAL = "individual";
	public static final String ANYONE = "anyone";
	public static final String ANYONEELSE = "anyoneelse";
	public static final String ALLDOMAINS = "alldomains";
	
	public static final String FROM_TO = "$#FROM_TO#$";
	
	@Override
	protected Resource processByDomain(String domain, Collection<Rule> rules) {
		StringBuilder sb;
		FileResource resource = new FileResource();
		resource.setName(getPackageName(domain)+FILE_EXTENSION);
		resource.setType(RESOURCE_TYPE);

		try {
			sb = processHeader(domain);

			for (Rule rule : rules){
				sb.append(processRule(domain, rule));
			}
			
			resource.setResource(new ByteArrayInputStream(sb.toString().getBytes()));

		} catch (IOException e) {
			log.error("error while processing rules:"+e);
			return null;
		}
		
		if(log.isDebugEnabled()){
			log.debug(sb.toString());
		}
		
		return resource;
	}
	
	
	protected abstract StringBuilder processHeader(String domain) throws IOException;
	
	protected abstract StringBuilder processRule(String domain, Rule rule) throws IOException;
		
	
	public Integer getFromToConditions(RuleDirection from, RuleDirection to, Boolean twoWays, StringBuilder sb){

		Integer priority = 0;
		StringBuilder ftsb = new StringBuilder();
		
		ftsb.append("$initialData : InitialData( ");
		
		//if it has to be two ways and direction are not the same
		if(twoWays && !(from.getDirectionType().equals(to.getDirectionType()) && from.getId().equals(to.getId()))){
			ftsb.append("(");
			priority = priority +getFromCondition(from, ftsb);
			if(!from.getDirectionType().equals(ANYONE) 
					&& !to.getDirectionType().equals(ANYONE)){
				ftsb.append(" && ");
			}
			priority = priority + getToCondition(to, ftsb);
			ftsb.append(") || (");
			getFromCondition(to, ftsb);
			if(!from.getDirectionType().equals(ANYONE) 
					&& !to.getDirectionType().equals(ANYONE)){
				ftsb.append(" && ");
			}
			getToCondition(from, ftsb);
			ftsb.append(") ");
		}else{
			priority = priority + getFromCondition(from, ftsb);
			if(!from.getDirectionType().equals(ANYONE) 
					&& !to.getDirectionType().equals(ANYONE)){
				ftsb.append(" && ");
			}
			priority = priority + getToCondition(to, ftsb);
		}

		ftsb.append(" )");
		
		sb.replace(sb.indexOf(FROM_TO), sb.indexOf(FROM_TO)+FROM_TO.length(), ftsb.toString());
		
		return priority;
	}
	
	
	private Integer getFromCondition(RuleDirection from, StringBuilder ftsb){
		Integer priority = 0;
		
		if(from.getDirectionType().equals(ANYONE)){
			/*doNothing, no restriction here*/
			priority=priority+1;
		} else if(from.getDirectionType().equals(ANYONEELSE)){
			/*anyone not in the other side domain*/
			ftsb.append(" (fromSender.domain.id != recipient.domain.id && sender.domain.id != recipient.domain.id) ");
			priority=priority+2;
		}else if(from.getDirectionType().equals(ALLDOMAINS)){
			/*is this side managed?*/
			ftsb.append(" (fromSender.domain.managed == true || sender.domain.managed == true) ");
			priority=priority+4;
		}else if(from.getDirectionType().equals(DOMAIN)){
			/*domain id is equal to*/
			ftsb.append(" (fromSender.domain.id == \"").append(from.getFreeValue()).append("\" || ").append(" sender.domain.id == \"").append(from.getFreeValue()).append("\") ");
			priority=priority+8;
		}else if(from.getDirectionType().equals(GROUP)){
			/*group id ? What the hell is group id !!!*/
			ftsb.append(" (fromSender.group!= null && fromSender.group.name == \"").append(from.getFreeValue()).append("\" || ").append(" sender.group!= null && sender.group.name == \"").append(from.getFreeValue()).append("\") ");
			priority=priority+16;
		}else if(from.getDirectionType().equals(INDIVIDUAL)){
			/* user from this side is equal to*/
			ftsb.append(" (fromSender.mail == \"").append(from.getFreeValue()).append("\" || ").append(" sender.mail == \"").append(from.getFreeValue()).append("\") ");
			priority=priority+32;
		}else{
			log.debug("no FROM");
		}
		
		return priority;
	}
	
	private Integer getToCondition(RuleDirection to, StringBuilder ftsb){
		Integer priority = 0;
		
		if(to.getDirectionType().equals(ANYONE)){
			/*doNothing, no restriction here*/
			priority=priority+1;
		} else if(to.getDirectionType().equals(ANYONEELSE)){
			/*anyone not in the other side domain*/
			ftsb.append(" recipient.domain.id != sender.domain.id ");
			priority=priority+2;
		}else if(to.getDirectionType().equals(ALLDOMAINS)){
			/*is this side managed?*/
			ftsb.append(" recipient.domain.managed == true ");
			priority=priority+4;
		}else if(to.getDirectionType().equals(DOMAIN)){
			/*domain id is equal to*/
			ftsb.append(" recipient.domain.id == \"").append(to.getFreeValue()).append("\" ");
			priority=priority+8;
		}else if(to.getDirectionType().equals(GROUP)){
			/*group id ? What the hell is group id !!!*/
			ftsb.append("  recipient.group!= null && recipient.group.name == \"").append(to.getFreeValue()).append("\"");
			priority=priority+16;
		}else if(to.getDirectionType().equals(INDIVIDUAL)){
			/* user from this side is equal to*/
			ftsb.append(" recipient.mail == \"").append(to.getFreeValue()).append("\"");
			priority=priority+32;
		}else{
			log.debug("no TO");
		}
		
		return priority;
	}
	
	
	public StringBuilder streamToBuilder(InputStream file) throws IOException{
		
		InputStream is = file;
	    StringBuilder buffer = new StringBuilder();
	    byte[] b = new byte[4096];
	    for (int n; (n = is.read(b)) != -1;) {
	      buffer.append(new String(b, 0, n));
	    }
	    is.close();
	    return buffer;
	}
	
	
	public String getAgendaGroup(String domain, Rule rule){
		if(domain==null || domain.trim().isEmpty()){
			return rule.getAdminOrder();
		} else {
			return domain.trim();
		}
	}

	public String getPackageName(String domain){
		String startName = domain;
		if(startName==null || startName.trim().isEmpty()){
			startName = "admin";
		}
		return startName+"."+getFeature().getComponent()+getFeature().getVersion();
	}
	
}
