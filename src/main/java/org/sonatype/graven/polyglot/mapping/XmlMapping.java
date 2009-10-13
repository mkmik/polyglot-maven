package org.sonatype.graven.polyglot.mapping;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.DefaultModelReader;
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
    private final String[] KEYS = { "xml:4.0.0" };

    private final String[] NAMES = { "pom.xml" };

    private final String[] EXTS = { ".xml", ".pom" };

    @Requirement
    private ModelReader reader;

    @Override
    protected String[] getAcceptOptionKeys() {
        return KEYS;
    }

    @Override
    protected String[] getPomNames() {
        return NAMES;
    }

    @Override
    protected String[] getAcceptFileExtentions() {
        return EXTS;
    }

    public ModelReader getReader() {
        assert reader != null;
        return reader;
    }
}