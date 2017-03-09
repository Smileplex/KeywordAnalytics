package com.dsstudio.web.model;

import javax.persistence.Entity;

public class RelatedKeyword {
	private int agentId;
	private String name;
	private String relname;
	private String relrelname;
	
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
}
