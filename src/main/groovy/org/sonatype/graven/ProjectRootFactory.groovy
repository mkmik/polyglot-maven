package org.sonatype.graven

/**
 * Builds the <tt>project</tt> root element.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ProjectRootFactory
{
    // NOTE: I have no clue ATM how to implement this in Java, so we use some Groovy glue here to create the proper closure
    
    static def create(def script, def builder) {
        assert script != null
        assert builder != null

        return {pom ->
            builder.project {
                pom.owner = script
                pom.delegate = builder
                pom()
            }
        }
    }
}