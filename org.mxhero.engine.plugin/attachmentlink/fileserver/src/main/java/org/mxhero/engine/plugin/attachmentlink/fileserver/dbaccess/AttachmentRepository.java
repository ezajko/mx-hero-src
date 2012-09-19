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

package org.mxhero.engine.plugin.attachmentlink.fileserver.dbaccess;

import java.util.List;

import org.mxhero.engine.plugin.attachmentlink.fileserver.domain.ContentDTO;

public interface AttachmentRepository {
	
	public ContentDTO getContent(Long idMessageAttachment);
	
	public void saveHistory(Long idMessageAttachment, boolean success);

	public boolean isAllowed(Long idMessageAttach);

	public void accessFirstTime(Long idMessageAttach);
	
	public void unsubscribe(Long messageId);
	
	public List<ContentDTO> getContentList(Long messageId, String recipient);

	public String getPulicUrl(Long idMessageAttach, String email);
	
}
