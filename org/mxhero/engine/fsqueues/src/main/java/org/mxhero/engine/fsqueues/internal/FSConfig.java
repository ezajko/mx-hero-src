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
	private String storePath;
	private String tmpPath;
	private String loadPath;
	private File storePathFile;
	private File tmpPathFile;
	private File loadPathFile;
	
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

	public void setStorePath(String storePath){
		if(storePath==null || storePath.trim().length()<1){
			throw new IllegalArgumentException("not valid storePath:"+storePath);
		}

		File newStorePath=new File(storePath);

		if(tmpPath!=null && newStorePath.getAbsolutePath().equalsIgnoreCase(tmpPathFile.getAbsolutePath())){
			throw new IllegalArgumentException("storePath and tmpPath are the same");
		}
		
		if(!newStorePath.exists()){
			if(!newStorePath.mkdir()){
				throw new IllegalArgumentException("clould not create storePath:"+storePath);
			}
		}
		this.storePathFile=newStorePath;
		this.storePath=storePath;
	}
	
	public String getStorePath() {
		return storePath;
	}
	
	public File getStorePathFile() {
		return storePathFile;
	}

	public void setTmpPath(String tmpPath) {
		if(tmpPath==null || tmpPath.trim().length()<1){
			throw new IllegalArgumentException("not valid tmpPath:"+tmpPath);
		}
		File newTmpPath=new File(tmpPath);
		
		if(storePathFile!=null && storePathFile.getAbsolutePath().equalsIgnoreCase(newTmpPath.getAbsolutePath())){
			throw new IllegalArgumentException("storePath and tmpPath are the same");
		}
		
		
		if(!newTmpPath.exists()){
			if(!newTmpPath.mkdir()){
				throw new IllegalArgumentException("clould not create tmpPath:"+tmpPath);
			}
		}
		this.tmpPathFile=newTmpPath;
		this.tmpPath=tmpPath;
	}
	
	public String getTmpPath() {
		return tmpPath;
	}

	public File getTmpPathFile() {
		return tmpPathFile;
	}

	
	public void setLoadPath(String loadPath) {
		if(loadPath==null || loadPath.trim().length()<1){
			throw new IllegalArgumentException("not valid loadPath:"+loadPath);
		}
		File newLoadPath=new File(loadPath);
		
		if(tmpPath!=null && tmpPathFile.getAbsolutePath().equalsIgnoreCase(newLoadPath.getAbsolutePath())){
			throw new IllegalArgumentException("loadPath and tmpPath are the same");
		}
		
		
		if(!newLoadPath.exists()){
			if(!newLoadPath.mkdir()){
				throw new IllegalArgumentException("clould not create loadPath:"+loadPath);
			}
		}
		this.loadPathFile=newLoadPath;
		this.loadPath=loadPath;
	}
	
	public String getLoadPath() {
		return loadPath;
	}

	public File getLoadPathFile() {
		return loadPathFile;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FSConfig [capacity=").append(capacity)
				.append(", deferredSize=").append(deferredSize)
				.append(", storePrefix=").append(storePrefix)
				.append(", tmpPrefix=").append(tmpPrefix).append(", suffix=")
				.append(suffix).append(", storePath=").append(storePath)
				.append(", tmpPath=").append(tmpPath).append(", loadPath=")
				.append(loadPath).append("]");
		return builder.toString();
	}
	

}
