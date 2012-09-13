/*
 * mxHero is a platform that intends to provide a single point of development 
 * and single point of distribution for email solutions and enhancements. It does this
 * by providing an extensible framework for rapid development and deployment of
 * email solutions.
 * 
 * Copyright (C) 2012  mxHero Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.mxhero.engine.fsqueues.internal.entity;

/**
 * @author mmarmol
 *
 */
public class FSMail {

	private String file;
	
	private String tmpFile;
	
	private FSMailKey key;
	
	private int readded=0;
	
	private long lastCheck;

	/**
	 * @param key
	 */
	public FSMail(FSMailKey key) {
		super();
		this.key = key;
		lastCheck=System.currentTimeMillis();
	}

	/**
	 * @return
	 */
	public String getFile() {
		return file;
	}

	/**
	 * @param file
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @return
	 */
	public String getTmpFile() {
		return tmpFile;
	}

	/**
	 * @param tmpFile
	 */
	public void setTmpFile(String tmpFile) {
		this.tmpFile = tmpFile;
	}

	/**
	 * @return
	 */
	public FSMailKey getKey() {
		return key;
	}

	/**
	 * @param key
	 */
	public void setKey(FSMailKey key) {
		this.key = key;
	}

	/**
	 * @return
	 */
	public int getReadded() {
		return readded;
	}

	/**
	 * @param readded
	 */
	public void setReadded(int readded) {
		this.readded = readded;
	}

	/**
	 * @return
	 */
	public long getLastCheck() {
		return lastCheck;
	}

	/**
	 * @param lastCheck
	 */
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
