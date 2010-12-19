package org.mxhero.console.backend.translator;

import org.mxhero.console.backend.entity.FeatureRuleProperty;
import org.mxhero.console.backend.vo.FeatureRulePropertyVO;
import org.springframework.stereotype.Repository;

@Repository
public class FeatureRulePropertyTranslator extends AbstractTranslator<FeatureRulePropertyVO, FeatureRuleProperty>{

	@Override
	protected FeatureRulePropertyVO doTranslate(FeatureRuleProperty entity) {
		FeatureRulePropertyVO propertyVO = new FeatureRulePropertyVO();
		
		propertyVO.setId(entity.getId());
		propertyVO.setPropertyKey(entity.getPropertyKey());
		propertyVO.setPropertyValue(entity.getPropertyValue());
		
		return propertyVO;
	}

}
