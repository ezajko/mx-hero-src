package org.mxhero.engine.mqueues.internal.queue;

import java.io.Serializable;

public class QueueId implements Serializable{

	private static final long serialVersionUID = -8486839770826877507L;

	private String module;
	
	private String phase;
	
	public QueueId(){};

	public QueueId(String module, String phase) {
		this.module = module;
		this.phase = phase;
	}

	public String getModule() {
		return module;
	}
	
	public void setModule(String module) {
		this.module = module;
	}
	
	public String getPhase() {
		return phase;
	}
	
	public void setPhase(String phase) {
		this.phase = phase;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((module == null) ? 0 : module.hashCode());
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
		QueueId other = (QueueId) obj;
		if (module == null) {
			if (other.module != null)
				return false;
		} else if (!module.equals(other.module))
			return false;
		if (phase == null) {
			if (other.phase != null)
				return false;
		} else if (!phase.equals(other.phase))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QueueId [module=").append(module).append(", phase=")
				.append(phase).append("]");
		return builder.toString();
	}

}
