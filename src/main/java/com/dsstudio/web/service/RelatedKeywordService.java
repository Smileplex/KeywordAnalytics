package com.dsstudio.web.service;

import java.util.List;

import com.dsstudio.web.model.RelatedKeyword;

public interface RelatedKeywordService {
	List<RelatedKeyword> getRelatedKeywordByNameAndAgentId(String name, int agentId);

}
