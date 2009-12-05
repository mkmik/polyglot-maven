package org.sonatype.maven.polyglot.clojure;

import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.mapping.Mapping;
import org.sonatype.maven.polyglot.mapping.MappingSupport;

/**
 * Clojure model mapping.
 *
 * @author <a href="mailto:mark@derricutt.com">Mark Derricutt</a>
 *
 * @since 1.0
 */
@Component(role=Mapping.class, hint="clojure")
public class ClojureMapping
    extends MappingSupport
{
    public ClojureMapping() {
        super("clojure");
        setPomNames("pom.clj");
        setAcceptLocationExtensions(".clj");
        setAcceptOptionKeys("clojure:4.0.0");
    }
}