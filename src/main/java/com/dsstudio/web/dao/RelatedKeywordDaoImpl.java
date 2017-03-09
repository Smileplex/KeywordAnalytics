package com.dsstudio.web.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import com.dsstudio.web.model.RelatedKeyword;

@Repository("relatedKeywordDao")
public class RelatedKeywordDaoImpl extends AbstractDao<Integer, RelatedKeyword> implements RelatedKeywordDao {

	@Override
	public List<RelatedKeyword> getRelatedKeywordByNameAndAgentId(String name, int agentId) {
		// TODO Auto-generated method stub
		String hql = "select a.agentId, name, relname, (select name from Keyword where id = b.RelatedId) as relrelname from "
				+ "(select b.agentId, b.name, (select name from Keyword where id = a.RelatedId) as relname, a.relatedId from RelatedKeywordLink a"
				+ " inner join Keyword b on a.KeywordId = b.id  where b.name=:name and b.agentId=:agentId) a "
				+ "left join RelatedKeywordLink b on a.relatedId = b.KeywordId";
		Query query = getSession().createSQLQuery(hql).addScalar("name", StringType.INSTANCE)
				.addScalar("relname", StringType.INSTANCE).addScalar("relrelname", StringType.INSTANCE)
				.setResultTransformer(Transformers.aliasToBean(RelatedKeyword.class));
		query.setParameter("name", name);
		query.setParameter("agentId", agentId);
		List<RelatedKeyword> list = query.list();
		System.out.println(list.size());
		return list;
	}

}
	