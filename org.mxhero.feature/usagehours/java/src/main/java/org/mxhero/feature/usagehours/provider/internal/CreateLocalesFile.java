package org.mxhero.feature.usagehours.provider.internal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TimeZone;

public class CreateLocalesFile {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
				"locales.xml"), true));
		for(String id:TimeZone.getAvailableIDs()) {
			  TimeZone zone = TimeZone.getTimeZone(id);
			  int offset = zone.getRawOffset()/1000;
			  int hour = offset/3600;
			  int minutes = (offset % 3600)/60;
			  bw.write("<locale label=\""+String.format("(GMT%+d:%02d) %s", hour, minutes, id)+"\" id=\""+id+"\" />");
			  bw.newLine();
		}
		bw.close();
	}

}
