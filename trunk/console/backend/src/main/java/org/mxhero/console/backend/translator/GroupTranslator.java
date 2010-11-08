package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.Group;
import org.mxhero.console.backend.vo.GroupVO;
import org.springframework.stereotype.Repository;

@Repository
public class GroupTranslator extends AbstractTranslator<GroupVO, Group>{

	@Override
	protected GroupVO doTranslate(Group entity) {
		GroupVO groupVO = new GroupVO();

		groupVO.setDescription(entity.getDescription());
		groupVO.setId(entity.getId());
		groupVO.setName(entity.getName());
		groupVO.setCreatedDate(entity.getCreatedDate());
		groupVO.setUpdatedDate(entity.getUpdatedDate());
		
		return groupVO;
	}

}
