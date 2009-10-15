package org.sonatype.maven.polyglot.mapping;

import org.codehaus.plexus.component.annotations.Component;

/**
 * Xml model mapping.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
@Component(role=Mapping.class, hint="xml")
public class XmlMapping
    extends MappingSupport
{
    public XmlMapping() {
        super(null);
        setPomNames("pom.xml");
        setAcceptLocationExtentions(".xml", ".pom");
        setAcceptOptionKeys("xml:4.0.0");
    }
}