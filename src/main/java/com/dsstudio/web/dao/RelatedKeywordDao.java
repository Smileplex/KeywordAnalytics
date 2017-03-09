package com.dsstudio.web.dao;

import java.util.List;

import com.dsstudio.web.model.RelatedKeyword;

public interface RelatedKeywordDao {
	List<RelatedKeyword> getRelatedKeywordByNameAndAgentId(String name, int agentId);
}
