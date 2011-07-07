package org.mxhero.engine.mqueues.internal.queue.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

/**
 * @author mmarmol
 */
@Entity
@Table(name = "mail_records")
public class Record implements Serializable{

	private static final long serialVersionUID = 5243614810185498098L;

	@EmbeddedId
	private RecordPk id;
	
	@Index(name="nextIndex")
	@Column(name = "phase", length = 10, nullable=false)
	private String phase;

	@Index(name="nextIndex")
	@Column(name = "module", length = 100, nullable=false)
	private String module;

	@Index(name="nextIndex")
	@Column(name = "enqueued", nullable=false)
	private Boolean enqueued;
	
	@Index(name="nextIndex")
	@Column(name = "in_queue_since", nullable=false)
	private Long inQueueSince;
	
	@Lob
	@Column(name = "content", nullable=false)
	private byte[] content; 

	@Column(name = "sender", length = 255)
	private String sender;

	@Column(name = "recipient", length = 255)
	private String recipient;

	@Column(name = "sender_id", length = 255)
	private String senderId;
	
	@Column(name = "recipient_id", length = 255)
	private String recipientId;

	@Column(name = "sender_domain_id", length = 255)
	private String senderDomainId;
	
	@Column(name = "recipient_domain_id", length = 255)
	private String recipientDomainId;

	@Column(name = "record_phase", length = 10, nullable=false)
	private String recordPhase;
	
	@Column(name = "state", length = 20)
	private String state;

	@Column(name = "state_reason")
	private String stateReason;

	@Column(name = "flow", length = 10)
	private String flow;
	
	@Column(name = "output_service", length = 255)
	private String outputService;
	
	@OneToMany(mappedBy="record", cascade={CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REMOVE}, fetch=FetchType.EAGER, orphanRemoval=true)
	private Set<RecordProperty> properties;
	
	public RecordPk getId() {
		return id;
	}

	public void setId(RecordPk id) {
		this.id = id;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public Boolean getEnqueued() {
		return enqueued;
	}

	public void setEnqueued(Boolean enqueued) {
		this.enqueued = enqueued;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateReason() {
		return stateReason;
	}

	public void setStateReason(String stateReason) {
		this.stateReason = stateReason;
	}

	public String getFlow() {
		return flow;
	}

	public void setFlow(String flow) {
		this.flow = flow;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public String getSenderDomainId() {
		return senderDomainId;
	}

	public void setSenderDomainId(String senderDomainId) {
		this.senderDomainId = senderDomainId;
	}

	public String getRecipientDomainId() {
		return recipientDomainId;
	}

	public void setRecipientDomainId(String recipientDomainId) {
		this.recipientDomainId = recipientDomainId;
	}

	public String getOutputService() {
		return outputService;
	}

	public void setOutputService(String outputService) {
		this.outputService = outputService;
	}

	public Set<RecordProperty> getProperties() {
		return properties;
	}

	public void setProperties(Set<RecordProperty> properties) {
		this.properties = properties;
	}

	public String getRecordPhase() {
		return recordPhase;
	}

	public void setRecordPhase(String recordPhase) {
		this.recordPhase = recordPhase;
	}

	public Long getInQueueSince() {
		return inQueueSince;
	}

	public void setInQueueSince(Long inQueueSince) {
		this.inQueueSince = inQueueSince;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((module == null) ? 0 : module.hashCode());
		result = prime * result + ((phase == null) ? 0 : phase.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Record other = (Record) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
