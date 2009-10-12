package org.sonatype.graven

import org.apache.maven.model.Model

/**
 * Builds the <tt>project</tt> root element.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ProjectRoot
{
    // NOTE: I have no clue ATM how to implement this in Java, so we use some Groovy glue here to create the proper closure

    static Closure create(final Script script, final Object builder) {
        // assert script != null
        // assert builder != null

        return {Closure c ->
            builder.project {
                c.owner = script
                c.delegate = builder
                c()
            }
        }
    }
}