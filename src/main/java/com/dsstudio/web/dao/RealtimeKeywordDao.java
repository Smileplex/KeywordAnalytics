package com.dsstudio.web.dao;

import java.util.List;

import com.dsstudio.web.model.RealtimeKeyword;

public interface RealtimeKeywordDao {
	List<RealtimeKeyword> findAllRealtimeKeywords();
}
