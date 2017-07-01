<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="<c:url value='/static/css/app.css' />" rel="stylesheet"></link>

	<title>Insert title here</title>
</head>
<body>
<div class='searchPanel' style='position: fixed; top: 0; left: 0;'>
	<input id="stockKeyword" type='text' />
	<button class='btnSearch'>search</button>
</div>
<!--
<div id="realtimeKeyword" style="float: left;">
    <h5>네이버</h5>
    <ul data-agentId='1'>

    </ul>
    <br />
    <h5>다음</h5>
    <ul data-agentId='2'>

    </ul>
</div>
-->

<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
    $(document).ready(function(){
        $init = 0;

        $.ajax({
            url: "api/getAllRealtimeKeywords",
            success: function(result){
                $container = $("#realtimeKeyword");
                for(var i=0; i<result.length; i++){
                    $container.find("ul[data-agentId='"+result[i].agentId+"']").append("<li>"+result[i].name+"</li>");
                }

            }});
        $(document).on('click','#realtimeKeyword ul li',function(){
            //alert($(this).text() + " | " + $(this).parent().attr('data-agentId'));
            $.ajax({
                type:"POST",
                url: "api/getRelatedKeyword",
                data: {name:$(this).text(), agentId:$(this).parent().attr('data-agentId')},
                success: function(result){console.log(result)}
            });
        });
        $("svg").css('width',$(window).innerWidth());
        $("svg").css('height',$(window).innerHeight());

        $(".btnSearch").bind('click',function(){
            initGraph();
        });

        $(document).keypress(function(e){
            if(e.which == 13) {
                initGraph();
            }
        });

        function initGraph(){
            $txtField = $("#stockKeyword");
            update($txtField.val(),$init);
            $txtField.val('').focus();
            $init = 1;
        }
    });
</script>
<style>
	.link {
		stroke: #ddd;
	}

	.node text {
		pointer-events: none;
		font: 12px sans-serif;
		letter-spacing: -1px;
		color: red;
	}

</style>
<svg></svg>
<script src="https://d3js.org/d3.v4.min.js"></script>
<script>


    var svg = d3.select("svg").call(d3.zoom().on("zoom", function () {
            svg.attr("transform", d3.event.transform)
        })).append("g"),
        width = +svg.attr("width"),
        height = +svg.attr("height");
    var color = d3.scaleOrdinal(d3.schemeCategory20);


    var simulation = d3.forceSimulation()
        .force("link", d3.forceLink().id(function(d) { return d.id; }).distance(100))
        .force("charge", d3.forceManyBody().strength([-500]))
        .force("center", d3.forceCenter($(document).innerWidth()/2, $(window).innerHeight()/2));

	/*
	 d3.json("api/getJsonFile?stockDetail=삼성전자", function(error, graph) {
	 if (error) throw error;

	 var link = svg.selectAll(".link")
	 .data(graph.links)
	 .enter().append("line")
	 .attr("class", "link");

	 var node = svg.selectAll(".node")
	 .data(graph.nodes)
	 .enter().append("g")
	 .attr("class", "node")
	 .call(d3.drag()
	 .on("start", dragstarted)
	 .on("drag", dragged)
	 .on("end", dragended));


	 node.append("circle").attr("r",5).style("fill", "#555").style("stroke", "#FFF").style("stroke-width", 3);


	 node.append("text")
	 .attr("dx", 10)
	 .attr("dy", ".35em")
	 .text(function(d) { return d.id });

	 simulation
	 .nodes(graph.nodes)
	 .on("tick", ticked);

	 simulation.force("link")
	 .links(graph.links);

	 function ticked() {
	 link
	 .attr("x1", function(d) { return d.source.x; })
	 .attr("y1", function(d) { return d.source.y; })
	 .attr("x2", function(d) { return d.target.x; })
	 .attr("y2", function(d) { return d.target.y; });

	 node.attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
	 }
	 });
	 */

    function dragstarted(d) {
        if (!d3.event.active) simulation.alphaTarget(0.3).restart();
        d.fx = d.x;
        d.fy = d.y;
    }

    function dragged(d) {
        d.fx = d3.event.x;
        d.fy = d3.event.y;
    }

    function dragended(d) {
        if (!d3.event.active) simulation.alphaTarget(0);
        d.fx = null;
        d.fy = null;
    }

    function update(a,b){
        d3.json("api/getJsonFile?stock="+a, function(error, graph) {
            svg.selectAll(".link").remove();
            svg.selectAll(".node").remove();
            //  d3.select("svg").clear();
            if (error) throw error;
            if(0<graph.nodes.length){
                var formatComma = d3.format(",");
                var link = svg.selectAll(".link")
                    .data(graph.links)
                    .enter().append("line")
                    .attr("class", "link");

                var node = svg.selectAll(".node")
                    .data(graph.nodes)
                    .enter().append("g")
                    .attr("class", "node")
                    .call(d3.drag()
                        .on("start", dragstarted)
                        .on("drag", dragged)
                        .on("end", dragended));



                node.append("circle").attr("r",4).style("fill", "#555").style("stroke", "#FFF").style("stroke-width", 3);


                node.append("text")
                    .attr("dx", 7)
                    .attr("dy", ".35em")
                    .style("fill", function(d){
                        if(d.price==undefined)
                            return "red";
                    })
                    .text(function(d) { return d.id });

                node.append("text")
                    .attr("dx", 7)
                    .attr("dy", "1.5em")
                    .text(function(d) {
                        if(d.price!=undefined)
                            return formatComma(d.price)
                    });

                simulation
                    .nodes(graph.nodes)
                    .on("tick", ticked);

                simulation.force("link")
                    .links(graph.links);
                simulation.alpha(1).restart();

                function ticked() {
                    svg.selectAll(".link")
                        .attr("x1", function(d) { return d.source.x; })
                        .attr("y1", function(d) { return d.source.y; })
                        .attr("x2", function(d) { return d.target.x; })
                        .attr("y2", function(d) { return d.target.y; });

                    svg.selectAll(".node").attr("transform", function(d) { return "translate(" + d.x + "," + d.y + ")"; });
                }
            }
        });
    }

</script>
</body>
</html>