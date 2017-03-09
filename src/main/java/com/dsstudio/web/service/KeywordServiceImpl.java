package com.dsstudio.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsstudio.web.dao.KeywordDao;
import com.dsstudio.web.model.Keyword;

@Service("keywordService")
@Transactional
public class KeywordServiceImpl implements KeywordService {

	@Autowired
	private KeywordDao dao;
	
	@Override
	public Keyword findById(int id) {
		// TODO Auto-generated method stub
		return dao.findById(id);
	}
	
	@Override
	public Keyword findKeywordByNameAndAgentId(String name, int agentId) {
		// TODO Auto-generated method stub
		return dao.findByNameAndAgentId(name,agentId);
	}

	

}
