package org.grails.plugin.easygrid

import grails.plugins.Plugin
import grails.util.Environment
import grails.core.GrailsApplication
import org.grails.plugin.easygrid.EasygridInitService
import org.grails.plugin.easygrid.JsUtils

class EasygridGrailsPlugin extends Plugin {

/*    def version = "1.7.2-SNAPSHOT"*/

    def grailsVersion = "3.3 > *"

    def loadAfter = ['services', 'controllers']

    def pluginExcludes = [
/*
            'grails-app/controllers/org/grails/plugin/easygrid/TestDomainController.groovy',
            'grails-app/domain/org/grails/plugin/easygrid/TestDomain.groovy',
            'grails-app/domain/org/grails/plugin/easygrid/OwnerTest.groovy',
            'grails-app/domain/org/grails/plugin/easygrid/PetTest.groovy',
            'grails-app/services/org/grails/plugin/easygrid/grids/TestGridService.groovy',
            'grails-app/views/templates/easygrid/_testGridRenderer.gsp',
*/
    ]

//    def dependsOn = [
//            'jquery-ui': "1.8.14 > *"
//    ]

    //the location of external grids config - to enable reloading
    def watchedResources = "file:./src/groovy/grids/**/*.groovy"

    def observe = ["controllers", "services"]

    def title = "Easygrid Plugin"
    def author = "Tudor Malene"
    def authorEmail = "tudor.malene@gmail.com"
    def description = '''
        Provides a declarative way of defining Data Grids.
        It works currently with jqGrid, google visualization and jquery dataTables.
        Out of the box it provides sorting, filtering, exporting and inline edit just by declaring a grid in a controller and adding a tag to your gsp.
        It also provides a powerful selection widget ( a direct replacement for drop-boxes )
        '''

    def documentation = "https://github.com/tudor-malene/Easygrid"

    def license = "APACHE"
    def issueManagement = [system: "GITHUB", url: "https://github.com/tudor-malene/Easygrid/issues"]
    def scm = [url: "https://github.com/tudor-malene/Easygrid"]

    void doWithDynamicMethods() { }

    Closure doWithSpring() { {->
        loadEasygridConfig(application)
        }
    }

    void onChange(Map<String, Object> event) {
        event.ctx.getBean(EasygridInitService).initializeGrids()
    }

    void onConfigChange(Map<String, Object> event) {
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    void onShutdown(Map<String, Object> event) {
        // TODO Implement code that is executed when the application shuts down (optional)
    }

    void doWithApplicationContext() {
        JsUtils.registerMarshallers()
        this.applicationContext.getBean(EasygridInitService).initializeGrids()
    }

    private ConfigObject loadEasygridConfig(GrailsApplication grailsApplication) {
        def config = grailsApplication.config
        GroovyClassLoader classLoader = new GroovyClassLoader(getClass().classLoader)

        // Merging default Easygrid config into main application config
        final ConfigObject defaultEasyGridConfig = new ConfigSlurper(Environment.current.name).parse(classLoader.loadClass('DefaultEasygridConfig'))
        def easyGridConfig
        // Merging user-defined Easygrid config into main application config if provided
        try {
            easyGridConfig = classLoader.loadClass('EasygridConfig')
        } catch (any) {
            println 'Could not process the EasygridConfig file '
            // ignore, just use the defaults
        }

        if(easyGridConfig) {
            println 'Merge the easygrid configs... '
            defaultEasyGridConfig.merge(new ConfigSlurper(Environment.current.name).parse(easyGridConfig))
            println 'Ole!'
        }
        config.put("easygrid", defaultEasyGridConfig.get("easygrid"))
        println 'We have merged it!'

        return config
    }

}
