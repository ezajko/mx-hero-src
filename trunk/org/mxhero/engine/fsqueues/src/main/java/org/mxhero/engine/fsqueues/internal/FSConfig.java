package org.mxhero.engine.fsqueues.internal;

import java.io.File;

public class FSConfig {

	private static final String FILE_EXTENSION=".eml";
	private static final String STORE_PREFIX="store";
	private static final String TMP_PREFIX="tmp";
	
	private static final int DEFAULT_CAPACITY=1000;
	private static final int DEFAULT_DEFERRED_SIZE=1*1024*1024;
	
	private int capacity = DEFAULT_CAPACITY;
	private int deferredSize = DEFAULT_DEFERRED_SIZE;
	private String storePrefix=STORE_PREFIX;
	private String tmpPrefix=TMP_PREFIX;
	private String suffix = FILE_EXTENSION;
	private File storePath;
	private File tmpPath;

	public FSConfig(String storePath, String tmpPath){
		if(tmpPath==null || tmpPath.trim().length()<1){
			throw new IllegalArgumentException("not valid tmpPath:"+tmpPath);
		}
		if(storePath==null || storePath.trim().length()<1){
			throw new IllegalArgumentException("not valid storePath:"+storePath);
		}
		if(storePath.trim().equalsIgnoreCase(tmpPath.trim())){
			throw new IllegalArgumentException("storePath and tmpPath are the same");
		}
		File newTmpPath=new File(tmpPath);
		if(!newTmpPath.exists()){
			if(!newTmpPath.mkdir()){
				throw new IllegalArgumentException("clould not create tmpPath:"+tmpPath);
			}
		}
		this.tmpPath=newTmpPath;
		
		File newStorePath=new File(storePath);
		if(!newStorePath.exists()){
			if(!newStorePath.mkdir()){
				throw new IllegalArgumentException("clould not create storePath:"+storePath);
			}
		}
		this.storePath=newStorePath;
	}
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		if(capacity<1){
			throw new IllegalArgumentException("capacity must be higher than 1");
		}
		this.capacity = capacity;
	}

	public int getDeferredSize() {
		return deferredSize;
	}

	public void setDeferredSize(int deferredSize) {
		if(deferredSize<1){
			throw new IllegalArgumentException("deferredSize must be higher than 1");
		}
		this.deferredSize = deferredSize;
	}

	public String getStorePrefix() {
		return storePrefix;
	}

	public void setStorePrefix(String storePrefix) {
		this.storePrefix = storePrefix;
	}

	public String getTmpPrefix() {
		return tmpPrefix;
	}

	public void setTmpPrefix(String tmpPrefix) {
		this.tmpPrefix = tmpPrefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public File getStorePath() {
		return storePath;
	}

	public File getTmpPath() {
		return tmpPath;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FSConfig [capacity=").append(capacity)
				.append(", deferredSize=").append(deferredSize)
				.append(", storePrefix=").append(storePrefix)
				.append(", tmpPrefix=").append(tmpPrefix).append(", suffix=")
				.append(suffix).append(", storePath=").append(storePath)
				.append(", tmpPath=").append(tmpPath).append("]");
		return builder.toString();
	}

}
