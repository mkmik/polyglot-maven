package org.sonatype.graven.polyglot;

import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.DefaultModelReader;
import org.apache.maven.model.io.ModelWriter;
import org.apache.maven.model.building.ModelProcessor;
import org.apache.maven.model.locator.ModelLocator;
import org.sonatype.graven.GravenModelReader;
import org.sonatype.graven.polyglot.mapping.Mapping;
import org.sonatype.graven.polyglot.mapping.MappingSupport;
import org.sonatype.graven.polyglot.mapping.GravenMapping;
import org.sonatype.graven.polyglot.mapping.XmlMapping;

import java.util.Map;
import java.util.List;
import java.util.LinkedList;
import java.io.File;

/**
 * Manages the mapping for polyglot model support.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=PolyglotModelManager.class)
public class PolyglotModelManager
    implements ModelLocator
{
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

        for (Mapping mapping : mappings) {
            File file = mapping.locatePom(dir);
            if (file != null) {
                return file;
            }
        }

        return null;
    }
}