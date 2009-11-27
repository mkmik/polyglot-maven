package org.sonatype.maven.polyglot.mapping;

import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;

import java.io.File;
import java.util.Map;

/**
 * Support for {@link Mapping} impls.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public abstract class MappingSupport
    implements Mapping
{
    private static final String[] EMPTY = {};

    private String roleHint;

    private String[] pomNames = EMPTY;

    private String[] acceptOptionKeys = EMPTY;

    private String[] acceptLocationExtentions = EMPTY;

    private float priority;

    @Requirement
    private PlexusContainer container;

    private ModelReader reader;

    private ModelWriter writer;

    protected MappingSupport(final String roleHint) {
        this.roleHint = roleHint;
    }

    public ModelReader getReader() {
        if (reader == null) {
            try {
                assert container != null;
                reader = container.lookup(ModelReader.class, roleHint);
            }
            catch (ComponentLookupException e) {
                throw new RuntimeException(e);
            }
        }
        return reader;
    }

    public ModelWriter getWriter() {
        if (writer == null) {
            try {
                assert container != null;
                writer = container.lookup(ModelWriter.class, roleHint);
            }
            catch (ComponentLookupException e) {
                throw new RuntimeException(e);
            }
        }
        return writer;
    }

    public String[] getAcceptLocationExtentions() {
        return acceptLocationExtentions;
    }

    public void setAcceptLocationExtentions(final String... accept) {
        this.acceptLocationExtentions = accept;
    }

    public String[] getAcceptOptionKeys() {
        return acceptOptionKeys;
    }

    public void setAcceptOptionKeys(final String... accept) {
        this.acceptOptionKeys = accept;
    }

    public String[] getPomNames() {
        return pomNames;
    }

    public void setPomNames(final String... names) {
        this.pomNames = names;
    }

    public File locatePom(final File dir) {
        assert dir != null;

        for (String name : getPomNames()) {
            File file = new File(dir, name);
            if (file.exists()) {
                return file;
            }
        }

        return null;
    }

    public boolean accept(final Map<String,?> options) {
        if (options != null) {
            for (String key : getAcceptOptionKeys()) {
                if (options.containsKey(key)) {
                    return true;
                }
            }

            String location = String.valueOf(options.get(ModelProcessor.LOCATION));
            if (location != null) {
                for (String ext : getAcceptLocationExtentions()) {
                    if (location.endsWith(ext)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public float getPriority()
    {
        return priority;
    }

    protected void setPriority( float priority )
    {
        this.priority = priority;
    }

}
