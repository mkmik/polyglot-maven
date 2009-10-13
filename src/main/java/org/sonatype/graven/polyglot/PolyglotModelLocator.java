package org.sonatype.graven.polyglot;

import java.io.File;

import org.apache.maven.model.locator.ModelLocator;
import org.codehaus.plexus.component.annotations.Component;

/**
 * ???
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=ModelLocator.class, hint="polyglot")
public class PolyglotModelLocator
    implements ModelLocator
{
    public File locatePom(final File dir) {
        File file = new File(dir, "pom.xml");

        if (!file.exists()) {
            file = new File(dir, "pom.groovy");
        }

        return file;
    }
}
