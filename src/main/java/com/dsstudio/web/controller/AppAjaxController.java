package com.dsstudio.web.controller;

import java.util.*;


import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsstudio.web.model.RealtimeKeyword;
import com.dsstudio.web.model.RelatedKeyword;
import com.dsstudio.web.model.StockMapping;
import com.dsstudio.web.service.RealtimeKeywordService;
import com.dsstudio.web.service.RelatedKeywordService;

@RestController
public class AppAjaxController {

	@Autowired
	RealtimeKeywordService realtimeKeywordService;

	@Autowired
	RelatedKeywordService relatedKeywordService;

	@RequestMapping(value = "/api/getAllRealtimeKeywords")
	public List<RealtimeKeyword> getAllRealtimeKeywords() {
		return realtimeKeywordService.findAllRealtimeKeywords();
	}

	@RequestMapping(value = "/api/getRelatedKeyword", method = RequestMethod.POST)
	public String getRelatedKeyword(@RequestParam("name") String name, @RequestParam("agentId") int agentId) {

		Set<String> nodes = new LinkedHashSet<String>();
		List<RelatedKeyword> relatedKeywords = relatedKeywordService.getRelatedKeywordByNameAndAgentId(name, agentId);
		for (RelatedKeyword relatedKeyword : relatedKeywords) {
			if (relatedKeyword.getName() != null && relatedKeyword.getRelname() != null
					&& relatedKeyword.getRelrelname() != null) {
				nodes.add(relatedKeyword.getName());
				nodes.add(relatedKeyword.getRelname());
				nodes.add(relatedKeyword.getRelrelname());
			}
		}

		Iterator<String> iter1 = nodes.iterator();
		while (iter1.hasNext()) {
			System.out.println(iter1.next());
		}
		return "success";
	}

	@RequestMapping(value = "/api/getJsonFile", method = RequestMethod.GET)
	public String getJsonFile(@RequestParam("stock") String stock) {
		System.out.println(stock.isEmpty());
		Set<StockMapping> nodes = new LinkedHashSet<StockMapping>();
//		Set<String> links = new LinkedHashSet<String>();
		Multimap<String, String> links = HashMultimap.create();
		List<RelatedKeyword> relatedKeywords = relatedKeywordService.getRelatedKeywordByNameAndAgentId(stock, 1);
		for (RelatedKeyword relatedKeyword : relatedKeywords) {
			if (relatedKeyword.getName() != null && relatedKeyword.getRelname() != null
					&& relatedKeyword.getRelrelname() != null) {
				nodes.add(new StockMapping(relatedKeyword.getName(), relatedKeyword.getStockDetail()));
				nodes.add(new StockMapping(relatedKeyword.getRelname(), relatedKeyword.getRelstock()));
				nodes.add(new StockMapping(relatedKeyword.getRelrelname(), relatedKeyword.getRelrelstock()));
				links.put(relatedKeyword.getName(), relatedKeyword.getRelname());
				links.put(relatedKeyword.getRelname(), relatedKeyword.getRelrelname());
			}
		}
		JSONObject jsonData = new JSONObject();
		for(StockMapping node : nodes){
			JSONObject jsonStock = new JSONObject();
			try {
				jsonStock.put("dependedOnBy",links.get(node.getKeywordName()));
				jsonStock.put("depends", "");
				jsonStock.put("docs","<h2>Anzelma <em>Group 4 long name for docs</em></h2>↵↵<div class=\"alert alert-warning\">No documentation for this object</div>↵↵<h3>Depends on</h3>↵↵<ul>↵<li><a href=\"#obj-Eponine\" class=\"select-object\" data-name=\"Eponine\">Eponine</a></li>↵<li><a href=\"#obj-Thenardier\" class=\"select-object\" data-name=\"Thenardier\">Thenardier</a></li>↵<li><a href=\"#obj-Mme-Thenardier\" class=\"select-object\" data-name=\"Mme.Thenardier\">Mme.Thenardier</a></li>↵</ul>↵↵<h3>Depended on by <em>(none)</em></h3></br>");
				jsonStock.put("name", node.getKeywordName());
				jsonStock.put("type","group1");
				jsonData.put(node.getKeywordName(), jsonStock);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			return new JSONObject().put("data",jsonData).toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
}
