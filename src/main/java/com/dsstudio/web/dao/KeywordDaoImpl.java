package com.dsstudio.web.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.dsstudio.web.model.Keyword;

@Repository("keywordDao")
public class KeywordDaoImpl extends AbstractDao<Integer, Keyword> implements KeywordDao {

	
	@Override
	public Keyword findByNameAndAgentId(String name, int agentId) {
		// TODO Auto-generated method stub
		Criteria criteria = createEntityCriteria();
        criteria.add(Restrictions.eq("name", name));
        criteria.add(Restrictions.eq("agentId", agentId));
        Keyword keyword = (Keyword) criteria.uniqueResult();
        return keyword;
	}

	@Override
	public Keyword findById(int id) {
		// TODO Auto-generated method stub
		return getByKey(id);
	}

}
