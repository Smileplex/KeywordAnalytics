package com.dsstudio.web.controller;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dsstudio.web.model.RealtimeKeyword;
import com.dsstudio.web.model.RelatedKeyword;
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
		Set<String> links = new LinkedHashSet<String>();
		List<RelatedKeyword> relatedKeywords = relatedKeywordService.getRelatedKeywordByNameAndAgentId(name, agentId);
		System.out.println(relatedKeywords.size());
		for (RelatedKeyword relatedKeyword : relatedKeywords) {
			if (relatedKeyword.getName() != null && relatedKeyword.getRelname() != null
					&& relatedKeyword.getRelrelname() != null) {
				nodes.add(relatedKeyword.getName());
				nodes.add(relatedKeyword.getRelname());
				nodes.add(relatedKeyword.getRelrelname());
				links.add(relatedKeyword.getName()+"-"+relatedKeyword.getRelname());
				links.add(relatedKeyword.getRelname()+"-"+relatedKeyword.getRelrelname());
			}
		}

		Iterator<String> iter1 = nodes.iterator();
		while (iter1.hasNext()) {
			System.out.println(iter1.next());
		}
		
		Iterator<String> iter2 = links.iterator();
		while(iter2.hasNext()){
			System.out.println(iter2.next());
		}
		return "success";
	}

	@RequestMapping(value = "/api/getJsonFile", method = RequestMethod.GET)
	public String getJsonFile() {
		Set<String> nodes = new LinkedHashSet<String>();
		Set<String> links = new LinkedHashSet<String>();
		List<RelatedKeyword> relatedKeywords = relatedKeywordService.getRelatedKeywordByNameAndAgentId("±¸Âî", 1);
		System.out.println(relatedKeywords.size());
		for (RelatedKeyword relatedKeyword : relatedKeywords) {
			if (relatedKeyword.getName() != null && relatedKeyword.getRelname() != null
					&& relatedKeyword.getRelrelname() != null) {
				nodes.add(relatedKeyword.getName());
				nodes.add(relatedKeyword.getRelname());
				nodes.add(relatedKeyword.getRelrelname());
				links.add(relatedKeyword.getName()+"-"+relatedKeyword.getRelname());
				links.add(relatedKeyword.getRelname()+"-"+relatedKeyword.getRelrelname());
			}
		}
		String jsonString = "";
		try {
			JSONArray arrIds = new JSONArray();
			JSONArray arrLinks = new JSONArray();
			Iterator<String> iter1 = nodes.iterator();
			Iterator<String> iter2 = links.iterator();
			
			while (iter1.hasNext()) {
				arrIds.put(new JSONObject().put("id",iter1.next()));
			
			}
			
			while(iter2.hasNext()){
				String str = iter2.next();
				arrLinks.put(new JSONObject().put("source", str.split("-")[0]).put("target", str.split("-")[1]));
			}
			
			jsonString = new JSONObject().put("nodes", arrIds)
					.put("links", arrLinks)
					.toString();
			System.out.println(jsonString);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jsonString;
	}
}
