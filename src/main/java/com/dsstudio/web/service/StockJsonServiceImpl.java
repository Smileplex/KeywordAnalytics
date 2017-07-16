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

import java.util.*;

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
            Collection<String> _dependLinks = dependLinks.get(node.getKeywordName());
            Collection<String> _dependedLinks = dependedLinks.get(node.getKeywordName());
            String docs = generateDocs(node, _dependLinks, _dependedLinks);
            JSONObject jsonStock = new JSONObject();
            try {
                jsonStock.put("depends", _dependLinks);
                jsonStock.put("dependedOnBy", _dependedLinks);
                jsonStock.put("docs",docs);
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

    private String generateDocs(StockMapping node, Collection<String> dependLinks, Collection<String> dependedLinks) {
        String docs = "";
        docs+="<h2>"+node.getKeywordName()+" <em>"+node.getStockDetail().getCode()+"</em></h2>";

        if(node.getStockDetail().getRiseFall()==2)
            docs+="<span class='stock_price up'>";
        else if(node.getStockDetail().getRiseFall()==3)
            docs+="<span class='stock_price mid'>";
        else if(node.getStockDetail().getRiseFall()==5)
            docs+="<span class='stock_price'>";
        docs+="<strong>"+String.format("%,d",node.getStockDetail().getPrice())+"</strong>" +
                "<span class='n_ch'>";
        if(node.getStockDetail().getRiseFall()==2)
            docs+="<span class='ico up'></span>";
        else if(node.getStockDetail().getRiseFall()==3)
            docs+="<span class='ico'></span>";
        else if(node.getStockDetail().getRiseFall()==5)
            docs+="<span class='ico'></span>";
        docs+=  "<em>"+String.format("%,d",node.getStockDetail().getFluct())+"</em>" +
                "<em>("+node.getStockDetail().getFluctRate()+"%)</em>" +
                "</span>";
        docs+= "</span>";
        docs+="<ul class='stock_additional'>";
        docs+="<li><em>전일종가</em>"+String.format("%,d", node.getStockDetail().getPricePrev())+"</li>";
        docs+="<li><em>고가</em><span class='up'>"+String.format("%,d", node.getStockDetail().getPriceMax())+"</span></li>";
        docs+="<li><em>저가</em><span class='down'>"+String.format("%,d", node.getStockDetail().getPriceMin())+"</span></li>";
        docs+="</ul>";
//        docs+="<ul class='stock_additional'>";
//        docs+="<li><em>전일종가</em>"+String.format("%,d", node.getStockDetail().getPricePrev())+"</li>";
//        docs+="<li><em>고가</em><span class='up'>"+String.format("%,d", node.getStockDetail().getPriceMax())+"</span></li>";
//        docs+="<li><em>저가</em><span class='down'>"+String.format("%,d", node.getStockDetail().getPriceMin())+"</span></li>";
//        docs+="</ul>";

        docs+="<div class=\"alert alert-warning\">";
        docs+="<img src="+node.getStockDetail().getChartDaily()+"/>&nbsp;&nbsp;&nbsp;&nbsp;";
        docs+="<img src="+node.getStockDetail().getChartWeekly()+"/>&nbsp;&nbsp;&nbsp;&nbsp;";
        docs+="<img src="+node.getStockDetail().getChartMonthly()+"/>";
        docs+="</div>";
        docs+="<h3>Depends on</h3>";
        docs+="<ul>";
        docs = putLinks(dependLinks, docs);
        docs+="</ul>";
        docs+="<h3>Depended on by</h3>";
        docs+="<ul>";
        docs = putLinks(dependedLinks, docs);
        docs+="</ul>";
        return docs;

    }

    private String putLinks(Collection<String> links, String docs) {
        for(String link : links){
            docs+="<li>";
            docs+="<a href=\"#obj-"+link+"\" class=\"select-object\" data-name=\""+link+"\">"+link+"</a>";
            docs+="</li>";
        }
        return docs;
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
