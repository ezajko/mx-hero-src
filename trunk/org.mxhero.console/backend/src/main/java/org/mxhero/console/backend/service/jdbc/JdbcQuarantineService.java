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

package org.mxhero.console.backend.service.jdbc;

import org.mxhero.console.backend.repository.QuarantineRepository;
import org.mxhero.console.backend.service.QuarantineService;
import org.mxhero.console.backend.vo.QuarantineVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("jdbcQuarantineService")
public class JdbcQuarantineService implements QuarantineService{
	
	private QuarantineRepository repository;

	@Autowired
	public JdbcQuarantineService(QuarantineRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(value="mxhero",readOnly=false)
	public void save(QuarantineVO quarantine) {
		if(quarantine!=null){
			if(quarantine.getEmail()==null || quarantine.getEmail().trim().isEmpty()){
				repository.delete(quarantine.getDomain());
			}else{
				repository.save(quarantine);
			}
		}
	}

	@Override
	@Transactional(value="mxhero",readOnly=true)
	public QuarantineVO read(String domain) {
		return repository.read(domain);
	}
	
	

}
