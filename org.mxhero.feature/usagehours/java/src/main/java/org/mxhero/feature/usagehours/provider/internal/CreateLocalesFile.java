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
