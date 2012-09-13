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

package org.mxhero.engine.fsqueues.internal.util;

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
