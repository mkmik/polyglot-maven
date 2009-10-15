package org.sonatype.maven.polyglot.mapping;

import org.apache.maven.model.io.ModelReader;
import org.apache.maven.model.io.ModelWriter;

import java.io.File;
import java.util.Map;

/**
 * Provides a mapping to polyglot specific models.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public interface Mapping
{
    File locatePom(File dir);

    boolean accept(Map<String,?> options);

    ModelReader getReader();

    ModelWriter getWriter();

    // TODO: Add priority to support, say picking XML first if there?
}