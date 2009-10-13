package org.sonatype.graven.polyglot.mapping;

import org.apache.maven.model.building.ModelProcessor;

import java.io.File;
import java.util.Map;

/**
 * Support for {@link Mapping} impls.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public abstract class MappingSupport
    implements Mapping
{
    private static final String[] EMPTY = {};

    private String[] pomNames = EMPTY;

    private String[] acceptOptionKeys = EMPTY;

    private String[] acceptLocationExtentions = EMPTY;

    public String[] getAcceptLocationExtentions() {
        return acceptLocationExtentions;
    }

    public void setAcceptLocationExtentions(final String... acceptLocationExtentions) {
        this.acceptLocationExtentions = acceptLocationExtentions;
    }

    public String[] getAcceptOptionKeys() {
        return acceptOptionKeys;
    }

    public void setAcceptOptionKeys(final String... acceptOptionKeys) {
        this.acceptOptionKeys = acceptOptionKeys;
    }

    public String[] getPomNames() {
        return pomNames;
    }

    public void setPomNames(final String... pomNames) {
        this.pomNames = pomNames;
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

    public boolean accept(final Map<String, ?> options) {
        if (options != null) {
            for (String key : getAcceptOptionKeys()) {
                if (options.containsKey(key)) {
                    return true;
                }
            }

            String location = (String) options.get(ModelProcessor.LOCATION);
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
}
