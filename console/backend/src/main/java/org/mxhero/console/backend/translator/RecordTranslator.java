package org.mxhero.console.backend.translator;

import java.util.Calendar;

import org.mxhero.console.backend.statistics.entity.Record;
import org.mxhero.console.backend.vo.RecordVO;
import org.springframework.stereotype.Repository;

@Repository
public class RecordTranslator extends AbstractTranslator<RecordVO, Record>{

	@Override
	protected RecordVO doTranslate(Record entity) {
		RecordVO recordVO = new RecordVO();
		
		recordVO.setBccRecipients(entity.getBccRecipients());
		recordVO.setBytesSize(entity.getBytesSize());
		recordVO.setCcRecipients(entity.getCcRecipients());
		recordVO.setFlow(entity.getFlow());
		recordVO.setFrom(entity.getFrom());
		Calendar insertDate = Calendar.getInstance();
		insertDate.setTimeInMillis(entity.getId().getInsertDate().getTime());
		recordVO.setInsertDate(insertDate);
		recordVO.setMessageId(entity.getMessageId());
		recordVO.setNgRecipients(entity.getNgRecipients());
		recordVO.setPhase(entity.getPhase());
		recordVO.setRecipient(entity.getRecipient());
		recordVO.setSender(entity.getSender());
		recordVO.setSentDate(entity.getSentDate());
		recordVO.setState(entity.getState());
		recordVO.setStateReason(entity.getStateReason());
		recordVO.setSubject(entity.getSubject());
		recordVO.setToRecipients(entity.getToRecipients());

		return recordVO;
	}

}
