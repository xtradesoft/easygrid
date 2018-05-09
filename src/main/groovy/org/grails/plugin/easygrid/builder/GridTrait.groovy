package org.grails.plugin.easygrid.builder

import grails.converters.JSON
import grails.web.Action
import org.grails.plugin.easygrid.EasygridDispatchService
import org.grails.plugin.easygrid.EasygridService

trait GridTrait {

    EasygridService easygridService
    EasygridDispatchService easygridDispatchService

    @Action
    def gridRows() {
        def gridName = params.gridName
        def gridConfig = easygridService.getGridConfig(controllerName, gridName)
        try {
            render easygridService.gridData(gridConfig)
            return
        }
        catch(e) {
            log.error "********** Failed to fetch grid rows!!",e
            e.printStackTrace()
        }
        render(status:500, 'text':"Failed to retrieve grid rows for ${controllerName} => ${gridName}")
    }

    @Action
    def gridHtml() {
        def gridName = params.gridName
        def gridConfig = easygridService.getGridConfig(controllerName, gridName)
        try {
            def model = easygridService.htmlGridDefinition(gridConfig)
            if (model) {
                model.attrs = [id: params.gridId ?: gridConfig.id, params: params]
                render(template: gridConfig.gridRenderer, model: model)
            }
            return
        }
        catch(e) {
            log.error "********** Failed to fetch grid html!!",e
            e.printStackTrace()
        }
        render(status:500, 'text':"Failed to retrieve grid html for ${controllerName} => ${gridName}")
    }

    @Action
    def gridSelectionLabel() {
        def gridName = params.gridName
        def gridConfig = easygridService.getGridConfig(controllerName, gridName)
        if ("list" == gridConfig.dataSourceType) {
            def ls = [['id':"${params.id}",'label':'---']]
            render ls as JSON
            return
        }
        try {
            render easygridDispatchService.callACLabel(gridConfig)
            return
        }
        catch(e) {
            log.error "********** Failed to fetch grid rows!!",e
            e.printStackTrace()
        }
        render(status:500, 'text':"Failed to retrieve grid autocomplete selection label for ${controllerName} => ${gridName}") as JSON
    }

    @Action
    def gridAutocompleteResult() {
        def gridName = params.gridName
        def gridConfig = easygridService.getGridConfig(controllerName, gridName)
        try {
            render easygridDispatchService.callACSearchedElementsJSON(gridConfig)
            return
        }
        catch(e) {
            log.error "********** Failed to fetch grid autocomplete result!!",e
            e.printStackTrace()
        }
        render(status:500, 'text':"Failed to retrieve grid autocomplete result ${controllerName} => ${gridName}")
    }

    /*
	 * Compares two objects using traditional 'compareTo' mechanics and handling potential for nullness in one or
	 * both objects being compared.
	 */
    int objectCompare(sortType, o1, o2)
    {
        def res = 0
        if (o1 == null && o2 == null) {
            return 0
        }
        else if (o1 == null && o2 != null) {
            res = -1
        }
        else if (o1 != null && o2 == null) {
            res = 1
        }
        else {
            res = o1.compareTo(o2)
        }
        sortType == 'asc' ? res : res*-1
    }
}