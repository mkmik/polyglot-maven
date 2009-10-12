package org.sonatype.graven

import org.apache.maven.model.Model
import org.apache.maven.model.io.DefaultModelWriter
import static org.junit.Assert.*

/**
 * Support for model tests.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GravenModelTestSupport
{
    protected String load(final String name) throws IOException {
        assertNotNull(name)
        def url = getClass().getResource(name)
        assertNotNull(url)
        return url.text
    }

    protected String chew(final Model model) throws IOException {
        assertNotNull(model)
        def writer = new DefaultModelWriter()
        def buff = new StringWriter()
        writer.write(buff, null, model)
        return buff.toString()
    }
}
