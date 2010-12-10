package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.FeatureRuleDirection;
import org.mxhero.console.backend.vo.FeatureRuleDirectionVO;
import org.springframework.stereotype.Repository;

@Repository
public class FeatureRuleDirectionTranslator extends AbstractTranslator<FeatureRuleDirectionVO, FeatureRuleDirection>{

	@Override
	protected FeatureRuleDirectionVO doTranslate(FeatureRuleDirection entity) {
		FeatureRuleDirectionVO directionVO = new FeatureRuleDirectionVO();
		
		directionVO.setDirectionType(entity.getDirectionType());
		directionVO.setFreeValue(entity.getFreeValue());
		directionVO.setId(entity.getId());
		directionVO.setValueId(entity.getValueId());
		
		return directionVO;
	}

}
