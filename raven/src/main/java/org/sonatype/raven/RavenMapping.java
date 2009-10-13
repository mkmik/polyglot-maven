package org.sonatype.raven;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * Raven model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role= Mapping.class, hint="raven")
public class RavenMapping
    extends MappingSupport
{
    @Requirement(hint="raven")
    private ModelReader reader;

    @Requirement(hint="raven")
    private ModelWriter writer;

    public RavenMapping() {
        setPomNames("pom.yml");
        setAcceptLocationExtentions(".yml");
        setAcceptOptionKeys("raven:4.0.0");
    }

    public ModelReader getReader() {
        assert reader != null;
        return reader;
    }

    public ModelWriter getWriter() {
        assert writer != null;
        return writer;
    }
}