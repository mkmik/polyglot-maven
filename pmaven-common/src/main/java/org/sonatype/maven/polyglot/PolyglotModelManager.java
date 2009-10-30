package org.sonatype.maven.polyglot;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.apache.maven.model.locator.ModelLocator;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.sonatype.maven.polyglot.mapping.Mapping;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Manages the mapping for polyglot model support.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
@Component(role=PolyglotModelManager.class)
public class PolyglotModelManager
    implements ModelLocator
{
    @Requirement
    protected Logger log;
    
    @Requirement(role=Mapping.class)
    private List<Mapping> mappings;

    public void addMapping(final Mapping mapping) {
        assert mapping != null;
        mappings.add(mapping);
    }

    public ModelReader getReaderFor(final Map<String, ?> options) {
        for (Mapping mapping : mappings) {
            if (mapping.accept(options)) {
                return mapping.getReader();
            }
        }

        throw new RuntimeException("Unable determine model input format; options=" + options);
    }

    public ModelWriter getWriterFor(final Map<String, ?> options) {
        for (Mapping mapping : mappings) {
            if (mapping.accept(options)) {
                return mapping.getWriter();
            }
        }

        throw new RuntimeException("Unable determine model output format; options=" + options);
    }

    public File locatePom(final File dir) {
        assert dir != null;

        Set<File> found = new HashSet<File>();
        for (Mapping mapping : mappings) {
            File file = mapping.locatePom(dir);
            if (file != null) {
                found.add(file);
            }
        }

        if (found.size() > 1) {
            throw new RuntimeException("Found more than one matching pom file: " + found +
                    "; Use --file option to select the desired pom to execute.");
        }
        else if (!found.isEmpty()) {
            return found.iterator().next();
        }

        return null;
    }
}