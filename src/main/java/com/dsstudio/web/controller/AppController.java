package com.dsstudio.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.dsstudio.web.model.Keyword;
import com.dsstudio.web.service.KeywordService;

@Controller
@RequestMapping("/")
public class AppController {

	@Autowired
	KeywordService keywordService;

	@RequestMapping(value = {"/"}, method = RequestMethod.GET)
	public String main(){
		return "newIndex";
	}
	/*
	@RequestMapping(value = { "/{name}-{agentId}" }, method = RequestMethod.GET)
	public String home(@PathVariable String name, @PathVariable int agentId, ModelMap model) {
		
		Keyword keyword = keywordService.findKeywordByNameAndAgentId(name, agentId);
		//Keyword keyword = keywordService.findById(3);
		//System.out.println(keyword.getName());
		//System.out.println(keyword.getLink());
		//System.out.println(keyword.getDateCreated());
		
		return "index";
	}*/
}
