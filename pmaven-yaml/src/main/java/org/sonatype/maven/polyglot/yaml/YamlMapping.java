package org.sonatype.maven.polyglot.yaml;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * YAML model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=Mapping.class, hint="yaml")
public class YamlMapping
    extends MappingSupport
{
    public YamlMapping() {
        super("yaml");
        setPomNames("pom.yml");
        setAcceptLocationExtentions(".yml");
        setAcceptOptionKeys("yaml:4.0.0");
    }
}