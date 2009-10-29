package org.sonatype.maven.polyglot.groovy;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * Groovy model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
@Component(role=Mapping.class, hint="groovy")
public class GroovyMapping
    extends MappingSupport
{
    public GroovyMapping() {
        super("groovy");
        setPomNames("pom.groovy", "pom.gy");
        setAcceptLocationExtentions(".groovy", ".gy");
        setAcceptOptionKeys("groovy:4.0.0");
    }
}