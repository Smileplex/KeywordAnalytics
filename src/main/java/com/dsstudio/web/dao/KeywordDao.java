package com.dsstudio.web.dao;

import com.dsstudio.web.model.Keyword;

public interface KeywordDao {
	Keyword findById(int id);
	Keyword findByNameAndAgentId(String name, int agentId);
}
