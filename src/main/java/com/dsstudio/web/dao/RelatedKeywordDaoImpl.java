package com.dsstudio.web.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.dsstudio.web.model.RelatedKeyword;
import com.dsstudio.web.model.StockDetail;
import com.dsstudio.web.model.StockKeyword;

@Repository("relatedKeywordDao")
public class RelatedKeywordDaoImpl extends AbstractDao<Integer, RelatedKeyword> implements RelatedKeywordDao {

	@Override
	public List<RelatedKeyword> getRelatedKeywordByNameAndAgentId(String name, int agentId) {
		// TODO Auto-generated method stub

		String sql = "select {a.*}, {sda.*}, {c.*}, {sdc.*}, {e.*}, {sde.*} from StockKeyword a" +
				" left join StockDetailKeyword sdka on a.Id = sdka.StockKeywordId" +
				" inner join StockDetail sda on sdka.StockDetailId = sda.Id" +
				" inner join RelatedKeywordLink b on a.id = b.KeywordId" +
				" inner join StockKeyword c on b.RelatedId = c.id" +
				" left join StockDetailKeyword sdkc on c.Id = sdkc.StockKeywordId" +
				" inner join StockDetail sdc on sdkc.StockDetailId = sdc.Id" +
				" inner join RelatedKeywordLink d on c.id = d.KeywordId" +
				" inner join StockKeyword e on d.RelatedId = e.id" +
				" left join StockDetailKeyword sdke on e.Id = sdke.StockKeywordId" +
				" inner join StockDetail sde on sdke.StockDetailId = sde.Id" +
				" where a.name=:name and a.agentId=:agentId and sda.Name <> sdc.Name and sdc.Name <> sde.NAME";
		System.out.println(sql);

		Query query = getSession().createSQLQuery(sql).addEntity("a", StockKeyword.class).addEntity("sda", StockDetail.class)
				.addEntity("c", StockKeyword.class).addEntity("sdc", StockDetail.class).addEntity("e", StockKeyword.class)
				.addEntity("sde", StockDetail.class);
		query.setParameter("name", name);
		query.setParameter("agentId", agentId);

		List<RelatedKeyword> list = new ArrayList<RelatedKeyword>();
		List<Object[]> rows = query.list();
		for (Object[] row : rows) {
			RelatedKeyword relatedKeyword = new RelatedKeyword();
			relatedKeyword.setName(((StockKeyword) row[0]).getName());
			relatedKeyword.setStockDetail((StockDetail) row[1]);
			relatedKeyword.setRelname(((StockKeyword) row[2]).getName());
			relatedKeyword.setRelstock((StockDetail) row[3]);
			relatedKeyword.setRelrelname(((StockKeyword) row[4]).getName());
			relatedKeyword.setRelrelstock((StockDetail) row[5]);
			list.add(relatedKeyword);
		}

		return list;
	}

}
