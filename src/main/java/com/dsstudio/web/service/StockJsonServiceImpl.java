package com.dsstudio.web.service;

import com.dsstudio.web.model.RelatedKeyword;
import com.dsstudio.web.model.StockMapping;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.sun.org.apache.regexp.internal.RE;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by DongwooSeo on 7/2/2017.
 */

@Service("stockJsonService")
@Transactional
public class StockJsonServiceImpl implements StockJsonService{

    @Autowired
    RelatedKeywordService relatedKeywordService;

    private Set<StockMapping> nodes;
    private Multimap<String, String> dependedLinks;
    private Multimap<String, String> dependLinks;

    public String generate(String stockName, int agentId) {
        initializeContainers();
        List<RelatedKeyword> relatedKeywords = getRelatedKeywords(stockName, agentId);
        setupNodesAndLinks(relatedKeywords);
        return makeStockJson();
    }

    private void initializeContainers() {
        nodes = new LinkedHashSet<>();
        dependLinks = HashMultimap.create();
        dependedLinks = HashMultimap.create();
    }

    private List<RelatedKeyword> getRelatedKeywords(String stockName, int agentId) {
        return relatedKeywordService.getRelatedKeywordByNameAndAgentId(stockName, agentId);
    }

    private void setupNodesAndLinks(List<RelatedKeyword> relatedKeywords) {
        for (RelatedKeyword relatedKeyword : relatedKeywords) {
            if (isRelatedKeywordValid(relatedKeyword)) {
                addNodes(relatedKeyword);
                addDependLinks(relatedKeyword);
                addDependedLinks(relatedKeyword);
            }
        }
    }

    private boolean isRelatedKeywordValid(RelatedKeyword relatedKeyword) {
        return relatedKeyword.getName() != null && relatedKeyword.getRelname() != null
                && relatedKeyword.getRelrelname() != null;
    }

    private void addNodes(RelatedKeyword relatedKeyword) {
        nodes.add(new StockMapping(relatedKeyword.getName(), relatedKeyword.getStockDetail()));
        nodes.add(new StockMapping(relatedKeyword.getRelname(), relatedKeyword.getRelstock()));
        nodes.add(new StockMapping(relatedKeyword.getRelrelname(), relatedKeyword.getRelrelstock()));
    }

    private void addDependLinks(RelatedKeyword relatedKeyword) {
        dependLinks.put(relatedKeyword.getRelname(), relatedKeyword.getName());
        dependLinks.put(relatedKeyword.getRelrelname(), relatedKeyword.getRelname());
    }

    private void addDependedLinks(RelatedKeyword relatedKeyword) {
        dependedLinks.put(relatedKeyword.getName(), relatedKeyword.getRelname());
        dependedLinks.put(relatedKeyword.getRelname(), relatedKeyword.getRelrelname());
    }

    private String makeStockJson() {
        JSONObject jsonData = new JSONObject();
        for(StockMapping node : nodes){
            JSONObject jsonStock = new JSONObject();
            try {
                jsonStock.put("dependLinks", dependLinks.get(node.getKeywordName()));
                jsonStock.put("dependedOnBy", dependedLinks.get(node.getKeywordName()));
                jsonStock.put("docs","<h2>Anzelma <em>Group 4 long name for docs</em></h2>↵↵<div class=\"alert alert-warning\">No documentation for this object</div>↵↵<h3>Depends on</h3>↵↵<ul>↵<li><a href=\"#obj-Eponine\" class=\"select-object\" data-name=\"Eponine\">Eponine</a></li>↵<li><a href=\"#obj-Thenardier\" class=\"select-object\" data-name=\"Thenardier\">Thenardier</a></li>↵<li><a href=\"#obj-Mme-Thenardier\" class=\"select-object\" data-name=\"Mme.Thenardier\">Mme.Thenardier</a></li>↵</ul>↵↵<h3>Depended on by <em>(none)</em></h3></br>");
                jsonStock.put("name", node.getKeywordName());
                jsonStock.put("type",getGroupType(node.getStockDetail().getFluctRate()));
                jsonData.put(node.getKeywordName(), jsonStock);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            return new JSONObject().put("data",jsonData).put("errors",new ArrayList()).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getGroupType(double fluctRate) {
        String type;
        if(fluctRate > 2.0)
            type = "2.0% < ";
        else if(fluctRate > 1.5)
            type = "1.5% < ";
        else if(fluctRate > 1.2)
            type = "1.2% < ";
        else if(fluctRate > 1.0)
            type = "1.0% < ";
        else if(fluctRate > 0.8)
            type = "0.8% < ";
        else if(fluctRate > 0.5)
            type = "0.5% < ";
        else
            type = "< 0.5%";
        return type;
    }
}
