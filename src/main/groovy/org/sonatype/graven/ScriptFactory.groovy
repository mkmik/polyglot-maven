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
        def binding = shell.context
        
        def root = {Closure c ->
            builder.project {
                c.owner = script
                c.delegate = builder
                c()
            }
        }

        def include = {source ->
            assert source != null

            def include
            
            if (source instanceof Class) {
                include = source.newInstance()
            }
            else if (source instanceof File) {
                include = shell.parse(source)
            }
            else {
                throw new IllegalArgumentException("Invalid include source: $source")
            }

            include.run()
            include.binding.properties.variables.each {
                binding.setVariable(it.key, it.value.curry(builder))
            }
        }

        include(Defaults)

        // Bind these last so they don't get clobbered
        binding.setProperty("project", root)
        binding.setProperty("include", include)

        return script
    }
}