package org.grails.plugin.easygrid

import static org.grails.plugin.easygrid.EasygridContextHolder.resetParams
import static org.grails.plugin.easygrid.GridUtils.isControllerEasygridEnabled

class EasygridFilters {

    def filters = {
        easygridDisableCallingGridClosures(controller: '*', action: '*Grid') {
            before = {
                log.info("EA Filter before : ${controllerName}")
                !isControllerEasygridEnabled(grailsApplication, controllerName)
            }
        }
        resetParamsAfterExport(controller: '*', action: '*Export') {
            afterView = { Exception e ->
                log.info("EA Filter before : ${controllerName}")
                if (isControllerEasygridEnabled(grailsApplication, controllerName)) {
                    resetParams()
                }
            }
        }
    }
}
