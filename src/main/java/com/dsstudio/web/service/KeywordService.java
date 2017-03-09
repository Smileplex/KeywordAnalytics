package com.dsstudio.web.service;

import com.dsstudio.web.model.Keyword;

public interface KeywordService {
	Keyword findById(int id);
	Keyword findKeywordByNameAndAgentId(String name, int agentId);

}
