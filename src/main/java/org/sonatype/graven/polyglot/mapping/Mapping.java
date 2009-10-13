package org.sonatype.graven.polyglot.mapping;

import org.apache.maven.model.io.ModelReader;

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

    // TODO: Alter accept to take input so it can check the files contents if needed?

    ModelReader getReader();
}