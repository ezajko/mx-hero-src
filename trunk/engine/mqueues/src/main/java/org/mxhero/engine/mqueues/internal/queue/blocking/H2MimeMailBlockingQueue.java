package org.mxhero.engine.mqueues.internal.queue.blocking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Map.Entry;

import javax.mail.MessagingException;

import org.mxhero.engine.mqueues.internal.queue.QueueId;
import org.mxhero.engine.mqueues.internal.queue.entity.Record;
import org.mxhero.engine.mqueues.internal.queue.entity.RecordPk;
import org.mxhero.engine.mqueues.internal.queue.entity.RecordProperty;
import org.mxhero.engine.mqueues.internal.queue.repository.RecordRepository;
import org.mxhero.engine.domain.mail.MimeMail;

public class H2MimeMailBlockingQueue extends AbstractMimeMailBlockingQueue<MimeMail>{

	private RecordRepository repository;

	public H2MimeMailBlockingQueue(QueueId id, int capacity, int count, RecordRepository repository) {
		super(id, capacity, count);
		this.repository=repository;
	}
	
	@Override
	protected boolean realReEnqueue(MimeMail o, long time) {
		return repository.reEnqueue(translate(o),time);
	}
	
	@Override
	protected void enqueue(MimeMail x) {
		Record record = translate(x);
		record.setEnqueued(true);
		record.setModule(getId().getModule());
		record.setPhase(getId().getPhase());
		record.setInQueueSince(System.currentTimeMillis());
		repository.save(record);
	}

	@Override
	protected MimeMail dequeue() {
		Record record = repository.findNext(getId().getModule(), getId().getPhase());
		if(record!=null){
			long wait = record.getInQueueSince() - System.currentTimeMillis();
			if(wait>0){
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e) {
				}
			}
		}
		return translate(record);
	}

	@Override
	protected boolean realRemove(MimeMail o) {
		RecordPk pk = new RecordPk();
		pk.setInsertDate(o.getTime());
		pk.setSequence(o.getSequence());
		return repository.delete(pk);
	}

	@Override
	public boolean realRemoveAddTo(MimeMail o, MimeMail z,
			AbstractMimeMailBlockingQueue<MimeMail> queueTo) {
		RecordPk removePk = new RecordPk();
		removePk.setInsertDate(o.getTime());
		removePk.setSequence(o.getSequence());
		Record removeRecord = repository.findById(removePk);
		if(removeRecord!=null && !removeRecord.getEnqueued()){
			Record addRecord = translate(z);
			addRecord.setEnqueued(true);
			addRecord.setModule(queueTo.getId().getModule());
			addRecord.setPhase(queueTo.getId().getPhase());
			addRecord.setInQueueSince(System.currentTimeMillis());
			repository.removeAdd(removeRecord, addRecord);
			return true;
		}
		return false;
	}
	
	private Record translate(MimeMail mail){
		Record record = null;
		if(mail!=null){
			record = new Record();
			RecordPk pk = new RecordPk();
			pk.setInsertDate(new Timestamp(mail.getTime().getTime()));
			pk.setSequence(mail.getSequence());
			record.setId(pk);
			record.setEnqueued(true);
			record.setModule(getId().getModule());
			record.setPhase(getId().getPhase());
			record.setRecordPhase(mail.getPhase());
			record.setFlow(mail.getFlow());
			record.setRecipient(mail.getRecipient());
			record.setRecipientDomainId(mail.getRecipientDomainId());
			record.setRecipientId(mail.getRecipientId());
			record.setSender(mail.getInitialSender());
			record.setSenderDomainId(mail.getSenderDomainId());
			record.setSenderId(mail.getSenderId());
			record.setState(mail.getStatus());
			record.setStateReason(mail.getStatusReason());
			record.setProperties(new HashSet<RecordProperty>());
			for( Entry<String,String> entry : mail.getProperties().entrySet()){
				record.getProperties().add(new RecordProperty(entry.getKey(),record,entry.getValue()));
			}
			
			record.setOutputService(mail.getResponseServiceId());
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			try {
				mail.getMessage().writeTo(os);
			} catch (IOException e) {
				throw new RuntimeException(e);
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
			record.setContent(os.toByteArray());
		}
		return record;
	}
	
	private MimeMail translate(Record record){
		MimeMail mail=null;
		if(record!=null){
			try {
				mail = MimeMail.createCustom(record.getSender(), record.getRecipient(), record.getContent(), record.getOutputService(), record.getId().getSequence(), record.getId().getInsertDate());
				mail.setFlow(record.getFlow());
				mail.setPhase(record.getRecordPhase());
				for(RecordProperty property : record.getProperties()){
					mail.getProperties().put(property.getId().getKey(),property.getValue());
				}
				mail.setRecipientDomainId(record.getRecipientDomainId());
				mail.setRecipientId(record.getRecipientId());
				mail.setSenderDomainId(record.getSenderDomainId());
				mail.setSenderId(record.getSenderId());
				mail.setStatus(record.getState());
				mail.setStatusReason(mail.getStatusReason());
			} catch (MessagingException e) {
				throw new RuntimeException(e);
			}
		}
		return mail;
	}

}
