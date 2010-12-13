package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.FeatureRule;
import org.mxhero.console.backend.vo.FeatureRuleVO;
import org.springframework.stereotype.Repository;

@Repository
public class FeatureRuleTranslator extends AbstractTranslator<FeatureRuleVO, FeatureRule>{

	private FeatureRuleDirectionTranslator directionTranslator = new FeatureRuleDirectionTranslator();
	
	@Override
	protected FeatureRuleVO doTranslate(FeatureRule entity) {
		
		FeatureRuleVO ruleVO = new FeatureRuleVO();
		
		ruleVO.setCreated(entity.getCreated());
		ruleVO.setUpdated(entity.getUpdated());
		ruleVO.setId(entity.getId());
		ruleVO.setName(entity.getLabel());
		ruleVO.setEnabled(entity.getEnabled());
		ruleVO.setFromDirection(directionTranslator.translate(entity.getFromDirection()));
		ruleVO.setToDirection(directionTranslator.translate(entity.getToDirection()));
		
		return ruleVO;
	}

}
