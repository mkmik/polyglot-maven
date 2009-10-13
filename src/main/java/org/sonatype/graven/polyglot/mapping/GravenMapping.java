package org.sonatype.graven.polyglot.mapping;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.sonatype.graven.GravenModelReader;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;

/**
 * Graven model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=Mapping.class, hint="graven")
public class GravenMapping
    extends MappingSupport
{
    private final String[] KEYS = { "graven:4.0.0" };

    private final String[] NAMES = { "pom.groovy", "pom.gy" };

    private final String[] EXTS = { ".groovy", ".gy" };

    @Requirement(hint="graven")
    private ModelReader reader;

    @Requirement(hint="graven")
    private ModelWriter writer;

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

    public ModelWriter getWriter() {
        assert writer != null;
        return writer;
    }
}