package com.dsstudio.web.model;

public class StockMapping {
	private String keywordName;
	private StockDetail stockDetail;

	public StockMapping(String keywordName, StockDetail stockDetail) {
		// TODO Auto-generated constructor stub
		this.keywordName = keywordName;
		this.stockDetail = stockDetail;
	}

	public String getKeywordName() {
		return keywordName;
	}

	public void setKeywordName(String keywordName) {
		this.keywordName = keywordName;
	}

	public StockDetail getStockDetail() {
		return stockDetail;
	}

	public void setStockDetail(StockDetail stockDetail) {
		this.stockDetail = stockDetail;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return keywordName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof StockMapping) {
			StockMapping sm = (StockMapping) obj;
			return (sm.keywordName.equals(this.keywordName));
		} else {
			return false;
		}
	}
}
