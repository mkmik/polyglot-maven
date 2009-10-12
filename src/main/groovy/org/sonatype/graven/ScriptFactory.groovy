package org.sonatype.graven

import groovy.xml.MarkupBuilder

/**
 * Builds {@link Script} instances to handle <tt>pom.groovy</tt> to XML.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
class ScriptFactory
{
    static Script create(final InputStream input, final Writer output) {
        assert input != null;
        assert output != null;

        def shell = new GroovyShell()
        def script = shell.parse(input)

        def builder = new MarkupBuilder(output)
        def binding = script.binding = new Binding()

        binding.setProperty("project", {Closure c ->
            builder.project {
                c.owner = script
                c.delegate = builder
                c()
            }
        })
        
        def include = {source ->
            assert source != null

            if (source instanceof Class) {
                def include = source.newInstance()
                include.run()
                include.binding.properties.variables.each {
                    binding.setVariable(it.key, it.value.curry(builder))
                }
            }
            else if (source instanceof File) {
                def include = shell.parse(source)
                include.run()
                include.binding.properties.variables.each {
                    binding.setVariable(it.key, it.value.curry(builder))
                }
            }
            else {
                throw new IllegalArgumentException("Invalid include source: $source")
            }
        }
        
        binding.setProperty("include", include)
    
        include(Defaults)

        return script
    }
}