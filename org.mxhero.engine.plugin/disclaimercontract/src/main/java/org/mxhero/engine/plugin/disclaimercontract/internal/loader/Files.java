package org.mxhero.engine.plugin.disclaimercontract.internal.loader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author mmarmol
 *
 */
public class Files {

	/**
	 * @param fromFile
	 * @param toFile
	 * @throws IOException
	 */
	public static void copy(File fromFile, File toFile) throws IOException{
	    FileInputStream from = null;
	    FileOutputStream to = null;
	    try {
	      from = new FileInputStream(fromFile);
	      to = new FileOutputStream(toFile);
	      byte[] buffer = new byte[4096];
	      int bytesRead;

	      while ((bytesRead = from.read(buffer)) != -1)
	        to.write(buffer, 0, bytesRead); // write
	    } finally {
	      if (from != null){
	        try{from.close();}catch(IOException e){}
	      }
	      if (to != null){
	        try{to.close();}catch(IOException e){}
	      }
	    }
	}

	/**
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] fileToArray(File file) throws IOException{
		FileInputStream in = null;
		ByteArrayOutputStream byteOut = null;
		try{
			in = new FileInputStream(file);
			byteOut = new ByteArrayOutputStream((int)file.length());
		    byte[] buffer = new byte[4096];
		    for (int size; (size = in.read(buffer)) != -1; ){
		    	byteOut.write(buffer, 0, size);
		    }
		    return byteOut.toByteArray();
		}finally{
				if(in!=null){
					try {in.close();} catch (IOException e1) {}
				}
				if(byteOut!=null){
					try {byteOut.close();} catch (IOException e1) {}
				}
			}
	}
	
}
