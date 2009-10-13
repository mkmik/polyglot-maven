package org.sonatype.graven.polyglot.mapping;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

/**
 * Xml model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=Mapping.class, hint="xml")
public class XmlMapping
    extends MappingSupport
{
    @Requirement
    private ModelReader reader;

    @Requirement
    private ModelWriter writer;

    public XmlMapping() {
        setPomNames("pom.xml");
        setAcceptLocationExtentions(".xml", ".pom");
        setAcceptOptionKeys("xml:4.0.0");
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