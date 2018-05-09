<%@ page import="org.grails.plugin.easygrid.JsUtils" defaultCodec="none" %>

<g:javascript>
%{--// attach the grid data to the element--}%
    $.data(document.getElementById("${attrs.id}_div"), 'grid',
    {
        options:${JsUtils.convertToJs(gridConfig.visualization, "${attrs.id}_div")},
        url:'${g.createLink(action: "${gridConfig.id}Rows", params: [gridName: "${gridConfig.id}"])}',
        loadAll: ${gridConfig.visualization.loadAllData ? 'true' : 'false'}
    });

    google.load('visualization', '1', {
        'packages' : ['table'],
        'callback' : easygrid.initTable("${attrs.id}","")
        });

</g:javascript>

<div id="${attrs.id}_div"></div>

