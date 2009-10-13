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
    protected abstract String[] getPomNames();

    protected abstract String[] getAcceptOptionKeys();

    protected abstract String[] getAcceptFileExtentions();

    public File locatePom(final File dir) {
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
                for (String ext : getAcceptFileExtentions()) {
                    if (location.endsWith(ext)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
