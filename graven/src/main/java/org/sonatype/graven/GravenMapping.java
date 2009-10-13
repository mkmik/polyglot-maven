package org.sonatype.graven;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * Graven model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role= Mapping.class, hint="graven")
public class GravenMapping
    extends MappingSupport
{
    @Requirement(hint="graven")
    private ModelReader reader;

    @Requirement(hint="graven")
    private ModelWriter writer;

    public GravenMapping() {
        setPomNames("pom.groovy", "pom.gy");
        setAcceptLocationExtentions(".groovy", ".gy");
        setAcceptOptionKeys("graven:4.0.0");
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