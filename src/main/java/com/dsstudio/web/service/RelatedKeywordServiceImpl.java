package com.dsstudio.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsstudio.web.dao.RelatedKeywordDao;
import com.dsstudio.web.model.RelatedKeyword;

@Service("relatedKeywordService")
@Transactional
public class RelatedKeywordServiceImpl implements RelatedKeywordService{
	@Autowired
	RelatedKeywordDao dao;
	
	@Override
	public List<RelatedKeyword> getRelatedKeywordByNameAndAgentId(String name, int agentId) {
		// TODO Auto-generated method stub
		return dao.getRelatedKeywordByNameAndAgentId(name,agentId);
	}

}
