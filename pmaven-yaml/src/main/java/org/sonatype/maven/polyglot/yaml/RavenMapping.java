package org.sonatype.maven.polyglot.yaml;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * Raven model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=Mapping.class, hint="raven")
public class RavenMapping
    extends MappingSupport
{
    public RavenMapping() {
        super("raven");
        setPomNames("pom.yml");
        setAcceptLocationExtentions(".yml");
        setAcceptOptionKeys("raven:4.0.0");
    }
}