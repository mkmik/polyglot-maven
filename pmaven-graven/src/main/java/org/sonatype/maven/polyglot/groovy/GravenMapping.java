package org.sonatype.maven.polyglot.groovy;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * Graven model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=Mapping.class, hint="graven")
public class GravenMapping
    extends MappingSupport
{
    public GravenMapping() {
        super("graven");
        setPomNames("pom.groovy", "pom.gy");
        setAcceptLocationExtentions(".groovy", ".gy");
        setAcceptOptionKeys("graven:4.0.0");
    }
}