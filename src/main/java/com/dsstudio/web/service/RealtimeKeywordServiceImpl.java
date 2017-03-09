package com.dsstudio.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dsstudio.web.dao.RealtimeKeywordDao;
import com.dsstudio.web.model.RealtimeKeyword;

@Service("realtimeKeywordService")
@Transactional
public class RealtimeKeywordServiceImpl implements RealtimeKeywordService {

	@Autowired
	RealtimeKeywordDao dao;
	@Override
	public List<RealtimeKeyword> findAllRealtimeKeywords() {
		// TODO Auto-generated method stub
		return dao.findAllRealtimeKeywords();
	}

}
