package com.dsstudio.web.model;

public class RelatedKeyword {
	private int agentId;
	private String name;
	private String relname;
	private String relrelname;
	private StockDetail stockDetail;
	private StockDetail relstock;
	private StockDetail relrelstock;

	public int getAgentId() {
		return agentId;
	}
	public void setAgentId(int agentId) {
		this.agentId = agentId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRelname() {
		return relname;
	}
	public void setRelname(String relname) {
		this.relname = relname;
	}
	public String getRelrelname() {
		return relrelname;
	}
	public void setRelrelname(String relrelname) {
		this.relrelname = relrelname;
	}
	public StockDetail getStockDetail() {
		return stockDetail;
	}
	public void setStockDetail(StockDetail stockDetail) {
		this.stockDetail = stockDetail;
	}
	public StockDetail getRelstock() {
		return relstock;
	}
	public void setRelstock(StockDetail relstock) {
		this.relstock = relstock;
	}
	public StockDetail getRelrelstock() {
		return relrelstock;
	}
	public void setRelrelstock(StockDetail relrelstock) {
		this.relrelstock = relrelstock;
	}

}
