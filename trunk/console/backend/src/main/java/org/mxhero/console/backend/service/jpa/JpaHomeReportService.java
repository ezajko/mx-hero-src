package org.mxhero.console.backend.service.jpa;

import java.math.BigInteger;
import java.sql.Timestamp;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.mxhero.console.backend.service.HomeReportService;
import org.mxhero.console.backend.vo.ActivityDataVO;
import org.mxhero.console.backend.vo.MessagesCompositionVO;
import org.mxhero.console.backend.vo.MxHeroDataVO;
import org.springframework.flex.remoting.RemotingDestination;
import org.springframework.stereotype.Service;

@Service("homeReportService")
@RemotingDestination(channels = { "flex-amf" })
public class JpaHomeReportService implements HomeReportService{

	@PersistenceContext(unitName = "mxheroPer")
	private EntityManager mxem;

	@PersistenceContext(unitName = "statisticsPer")
	private EntityManager stem;
	
	@Override
	public MxHeroDataVO getMxHeroData(String domainId) {
		MxHeroDataVO data = new MxHeroDataVO();
		
		if(domainId==null){
			data.setDomains(mxem.createQuery("SELECT COUNT(*) FROM Domain d", Long.class).getSingleResult());
			data.setGroups(null);
			data.setAccounts(mxem.createQuery("SELECT COUNT(*) FROM EmailAccount ea", Long.class).getSingleResult());
			data.setEnabledRules(mxem.createQuery("SELECT COUNT(*) FROM FeatureRule r WHERE r.enabled = true", Long.class).getSingleResult());
			data.setDisableRules(mxem.createQuery("SELECT COUNT(*) FROM FeatureRule r WHERE r.enabled = false", Long.class).getSingleResult());
		}else{
			data.setDomains(null);
			data.setGroups(mxem.createQuery("SELECT COUNT(*) FROM Group g WHERE g.domain.domain = ? ", Long.class).setParameter(1, domainId).getSingleResult());
			data.setAccounts(mxem.createQuery("SELECT COUNT(*) FROM EmailAccount ea WHERE ea.domain.domain = ? ", Long.class).setParameter(1, domainId).getSingleResult());
			data.setEnabledRules(mxem.createQuery("SELECT COUNT(*) FROM FeatureRule r WHERE r.enabled = true AND r.domain.domain = ? ", Long.class).setParameter(1, domainId).getSingleResult());
			data.setDisableRules(mxem.createQuery("SELECT COUNT(*) FROM FeatureRule r WHERE r.enabled = false AND r.domain.domain = ? ", Long.class).setParameter(1, domainId).getSingleResult());			
		}

		return data;
	}

	@Override
	public MessagesCompositionVO getMessagesCompositionData(long since, String domainId) {
		MessagesCompositionVO messages = new MessagesCompositionVO();
		
		String basicQuery = "SELECT COUNT(*) FROM mail_records r0 " 
			+" WHERE r0.insert_date > ? "
			+" AND EXISTS( SELECT 1 FROM mail_stats s "
									+" WHERE s.insert_date = r0.insert_date " 
									+" AND s.record_sequence = r0.record_sequence " 
									+" AND ${CONDITION}) ";
		
		String cleanQuery = "SELECT COUNT(*) FROM mail_records r0 " 
		+" WHERE r0.insert_date > ? "
		+" AND NOT EXISTS( SELECT 1 FROM mail_stats s "
								+" WHERE s.insert_date = r0.insert_date " 
								+" AND s.record_sequence = r0.record_sequence " 
								+" AND ((s.stat_key='spam.detected' AND s.stat_value='true') OR (s.stat_key='virus.detected' AND s.stat_value='true') OR s.stat_key='email.blocked')) ";
		
		if(domainId==null){
			messages.setSpam(((BigInteger)stem.createNativeQuery(basicQuery.replaceAll("\\$\\{CONDITION\\}", "s.stat_key='spam.detected' AND s.stat_value='true'"))
					.setParameter(1, new Timestamp(since)).getSingleResult()).longValue());
			messages.setVirus(((BigInteger)stem.createNativeQuery(basicQuery.replaceAll("\\$\\{CONDITION\\}", "s.stat_key='virus.detected' AND s.stat_value='true'"))
					.setParameter(1, new Timestamp(since)).getSingleResult()).longValue());
			messages.setBlocked(((BigInteger)stem.createNativeQuery(basicQuery.replaceAll("\\$\\{CONDITION\\}", "s.stat_key='email.blocked'"))
					.setParameter(1, new Timestamp(since)).getSingleResult()).longValue());
			messages.setClean(((BigInteger)stem.createNativeQuery(cleanQuery).setParameter(1, new Timestamp(since)).getSingleResult()).longValue());
		}else{
			messages.setSpam(((BigInteger)stem.createNativeQuery(basicQuery.replaceAll("\\$\\{CONDITION\\}", "s.stat_key='spam.detected' AND s.stat_value='true'").concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) "))
					.setParameter(1, new Timestamp(since)).setParameter(2, domainId).setParameter(3, domainId).getSingleResult()).longValue());
			messages.setVirus(((BigInteger)stem.createNativeQuery(basicQuery.replaceAll("\\$\\{CONDITION\\}", "s.stat_key='virus.detected' AND s.stat_value='true'").concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) "))
					.setParameter(1, new Timestamp(since)).setParameter(2, domainId).setParameter(3, domainId).getSingleResult()).longValue());
			messages.setBlocked(((BigInteger)stem.createNativeQuery(basicQuery.replaceAll("\\$\\{CONDITION\\}", "s.stat_key='email.blocked'").concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) "))
					.setParameter(1, new Timestamp(since)).setParameter(2, domainId).setParameter(3, domainId).getSingleResult()).longValue());
			messages.setClean(((BigInteger)stem.createNativeQuery(cleanQuery.concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ")).setParameter(1, new Timestamp(since)).setParameter(2, domainId).setParameter(3, domainId).getSingleResult()).longValue());			
		}
		
		return messages;
	}
	
	@Override
	public ActivityDataVO getActivity(long since, String domainId) {
		ActivityDataVO data = new ActivityDataVO();
		String threatsQuery = "SELECT COUNT(*), date_format(r0.insert_date,'%Y-%m-%d %H:%i') FROM mail_records r0 " 
				+" WHERE r0.insert_date > ? "
				+" AND EXISTS( SELECT 1 FROM mail_stats s "
										+" WHERE s.insert_date = r0.insert_date " 
										+" AND s.record_sequence = r0.record_sequence " 
										+" AND ${CONDITION}) ";
		String trafficQuery =" SELECT COUNT(*), date_format(r0.insert_date,'%Y-%m-%d %H:%i') FROM mail_records r0 " +
				" WHERE r0.insert_date >= ? ";
		String incomming = " AND (r0.flow = 'both' OR r0.flow = 'in') ";
		String outgoing = " AND (r0.flow = 'both' OR r0.flow = 'out') ";
		String group ="GROUP BY  date_format(r0.insert_date,'%Y-%m-%d %H:%i')";

		if(domainId==null){
			data.setIncomming(stem.createNativeQuery(trafficQuery.concat(incomming).concat(group))
			.setParameter(1, new Timestamp(since)).getResultList());
			data.setOutgoing(stem.createNativeQuery(trafficQuery.concat(outgoing).concat(group))
					.setParameter(1, new Timestamp(since)).getResultList());
			
			data.setSpam(stem.createNativeQuery(threatsQuery.concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='spam.detected' AND s.stat_value='true'"))
					.setParameter(1, new Timestamp(since)).getResultList());
			data.setVirus(stem.createNativeQuery(threatsQuery.concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='virus.detected' AND s.stat_value='true'"))
					.setParameter(1, new Timestamp(since)).getResultList());
			data.setBlocked(stem.createNativeQuery(threatsQuery.concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='email.blocked'"))
					.setParameter(1, new Timestamp(since)).getResultList());
		}else{
			data.setIncomming(stem.createNativeQuery(trafficQuery.concat(incomming).concat(" AND r0.recipient_domain_id = ? ").concat(group))
					.setParameter(1, new Timestamp(since)).setParameter(2, domainId).getResultList());
					data.setOutgoing(stem.createNativeQuery(trafficQuery.concat(outgoing).concat(" AND r0.sender_domain_id = ? ").concat(group))
							.setParameter(1, new Timestamp(since)).setParameter(2, domainId).getResultList());
					
					data.setSpam(stem.createNativeQuery(threatsQuery.concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ").concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='spam.detected' AND s.stat_value='true'"))
							.setParameter(1, new Timestamp(since)).setParameter(2, domainId).setParameter(3, domainId).getResultList());
					data.setVirus(stem.createNativeQuery(threatsQuery.concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ").concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='virus.detected' AND s.stat_value='true'"))
							.setParameter(1, new Timestamp(since)).setParameter(2, domainId).setParameter(3, domainId).getResultList());
					data.setBlocked(stem.createNativeQuery(threatsQuery.concat(" AND (r0.recipient_domain_id = ? OR r0.sender_domain_id = ?) ").concat(group).replaceAll("\\$\\{CONDITION\\}", "s.stat_key='email.blocked'"))
							.setParameter(1, new Timestamp(since)).setParameter(2, domainId).setParameter(3, domainId).getResultList());			
		}
		
		return data;
	}
	
	public EntityManager getMxem() {
		return mxem;
	}

	public void setMxem(EntityManager mxem) {
		this.mxem = mxem;
	}

	public EntityManager getStem() {
		return stem;
	}

	public void setStem(EntityManager stem) {
		this.stem = stem;
	}

}
