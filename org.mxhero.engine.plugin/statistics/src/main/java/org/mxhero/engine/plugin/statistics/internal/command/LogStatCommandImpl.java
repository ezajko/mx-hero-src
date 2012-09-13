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

package org.mxhero.engine.plugin.statistics.internal.command;

import org.mxhero.engine.commons.mail.MimeMail;
import org.mxhero.engine.commons.mail.command.NamedParameters;
import org.mxhero.engine.commons.mail.command.Result;
import org.mxhero.engine.plugin.statistics.command.LogStatCommand;
import org.mxhero.engine.plugin.statistics.command.LogStatCommandParameters;
import org.mxhero.engine.plugin.statistics.internal.entity.Stat;
import org.mxhero.engine.plugin.statistics.internal.entity.Utils;
import org.mxhero.engine.plugin.statistics.internal.repository.RecordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author mmarmol
 *
 */
public class LogStatCommandImpl implements LogStatCommand{

	private static Logger log = LoggerFactory.getLogger(LogStatCommandImpl.class);
	
	private Utils utils;
	
	private RecordRepository repository;
	
	/**
	 * @see org.mxhero.engine.domain.mail.command.Command#exec(org.mxhero.engine.domain.mail.MimeMail, java.lang.String[])
	 */
	@Override
	public Result exec(MimeMail mail, NamedParameters parameters) {
		Result result = new Result();
		LogStatCommandParameters lsParameters = new LogStatCommandParameters(parameters);
		if (lsParameters.getKey()==null
				|| lsParameters.getKey().trim().isEmpty()
				|| lsParameters.getValue()==null
				|| lsParameters.getValue().trim().isEmpty()){
			log.warn("wrong params");
			result.setAnError(true);
			result.setMessage("wrong params");
			return result;
		}
		Stat stat = utils.createStat(mail, lsParameters.getKey().trim(), lsParameters.getValue().trim());
		try{
			getRepository().saveStat(stat);
			result.setConditionTrue(true);
		} catch(Exception e) {
			log.warn("Error while persisting "+stat,e);
		}
		return result;
	}

	/**
	 * @return
	 */
	public RecordRepository getRepository() {
		return repository;
	}

	/**
	 * @param repository
	 */
	public void setRepository(RecordRepository repository) {
		this.repository = repository;
	}

	/**
	 * @return
	 */
	public Utils getUtils() {
		return utils;
	}

	/**
	 * @param utils
	 */
	public void setUtils(Utils utils) {
		this.utils = utils;
	}

}
