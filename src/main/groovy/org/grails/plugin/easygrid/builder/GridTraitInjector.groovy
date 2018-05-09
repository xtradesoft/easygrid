package org.grails.plugin.easygrid.builder

import grails.compiler.ast.SupportsClassNode
import grails.compiler.traits.TraitInjector
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ClassNode
import org.grails.compiler.injection.GrailsASTUtils
import org.grails.plugin.easygrid.Easygrid

/*
 * Intended to inject methods into any controller annotated with "Easygrid"; this is an alternative
 * to the original plugin's use of the now defunct 'registerMapping' method that was present in
 * o.c.g.grails.commons.DefaultGrailsControllerClass prior to Grails 3 but is purposely absent from
 * Grails 3+ org.grails.core.DefaultGrailsControllerClass.
 *
 * See http://docs.grails.org/latest/guide/plugins.html#addingMethodsAtCompileTime
 */

@CompileStatic
class GridTraitInjector implements TraitInjector, SupportsClassNode {

    @Override
    Class getTrait() {
        GridTrait
    }

    @Override
    String[] getArtefactTypes() {
        ['Controller'] as String[]
    }

    boolean supports(ClassNode cn) {
        return GrailsASTUtils.hasAnnotation(cn,Easygrid)
    }
}
