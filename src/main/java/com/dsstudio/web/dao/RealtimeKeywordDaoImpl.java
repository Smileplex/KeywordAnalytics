package com.dsstudio.web.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;

import com.dsstudio.web.model.RealtimeKeyword;

@Repository("realtimeKeywordDao")
public class RealtimeKeywordDaoImpl extends AbstractDao<Integer, RealtimeKeyword> implements RealtimeKeywordDao {

	@Override
	public List<RealtimeKeyword> findAllRealtimeKeywords() {
		// TODO Auto-generated method stub
		Criteria criteria = createEntityCriteria();
		return (List<RealtimeKeyword>)criteria.list();
	}

}
