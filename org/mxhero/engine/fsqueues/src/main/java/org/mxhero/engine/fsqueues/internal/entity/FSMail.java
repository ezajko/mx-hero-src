package org.mxhero.engine.fsqueues.internal.entity;

public class FSMail {

	private String file;
	
	private String tmpFile;
	
	private FSMailKey key;
	
	private int readded=0;
	
	private long lastCheck;

	public FSMail(FSMailKey key) {
		super();
		this.key = key;
		lastCheck=System.currentTimeMillis();
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getTmpFile() {
		return tmpFile;
	}

	public void setTmpFile(String tmpFile) {
		this.tmpFile = tmpFile;
	}

	public FSMailKey getKey() {
		return key;
	}

	public void setKey(FSMailKey key) {
		this.key = key;
	}

	public int getReadded() {
		return readded;
	}

	public void setReadded(int readded) {
		this.readded = readded;
	}

	public long getLastCheck() {
		return lastCheck;
	}

	public void setLastCheck(long lastCheck) {
		this.lastCheck = lastCheck;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
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
		FSMail other = (FSMail) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FSMail [file=").append(file).append(", tmpFile=")
				.append(tmpFile).append(", key=").append(key)
				.append(", readded=").append(readded).append(", lastCheck=")
				.append(lastCheck).append("]");
		return builder.toString();
	}

}
