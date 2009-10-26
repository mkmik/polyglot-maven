package org.sonatype.maven.graven

import org.sonatype.maven.polyglot.groovy.builder.ModelBuilder
import org.apache.maven.model.Model
import org.apache.maven.model.building.ModelProcessor

/**
 * Model loader.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */

class ModelLoader
{
    final org.sonatype.maven.polyglot.groovy.builder.ModelBuilder builder

    final GroovyShell shell

    ModelLoader(final org.sonatype.maven.polyglot.groovy.builder.ModelBuilder builder, final GroovyShell shell) {
        this.builder = builder
        this.shell = shell
    }

    Model load(final InputStream input, final Map<?,?> options) {
        String location = options?.get(ModelProcessor.LOCATION)
        Script script = location != null ? shell.parse(input, location) : shell.parse(input);
        def binding = shell.context
        
        def include = {source ->
            assert source != null

            def include

            // TODO: Support String, support loading from resource

            if (source instanceof Class) {
                include = source.newInstance()
            }
            else if (source instanceof File) {
                include = shell.parse(source)
            }
            else if (source instanceof URL) {
                include = shell.parse(source.openStream())
            }
            else {
                throw new IllegalArgumentException("Invalid include source: $source")
            }

            include.run()

            // Include each closure variable which starts with '$' and curry in the builder
            include.binding.properties.variables.each {
                if (it.value instanceof Closure && it.key.startsWith('$')) {
                    binding.setVariable(it.key, it.value.curry(builder))
                }
            }
        }

        include(Macros)

        binding.setProperty('$include', include)
        
        return (Model) builder.build(script);
    }
}