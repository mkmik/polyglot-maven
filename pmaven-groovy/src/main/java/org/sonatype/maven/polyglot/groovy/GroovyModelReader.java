/*
 * Copyright (C) 2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sonatype.maven.polyglot.groovy;

import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.DefaultModelWriter;
import org.apache.maven.model.io.ModelReader;
import org.codehaus.groovy.runtime.StackTraceUtils;
import org.codehaus.plexus.component.annotations.Component;
import org.codehaus.plexus.component.annotations.Requirement;
import org.codehaus.plexus.logging.Logger;
import org.codehaus.plexus.util.IOUtil;
import org.sonatype.maven.polyglot.PolyglotModelUtil;
import org.sonatype.maven.polyglot.execute.ExecuteManager;
import org.sonatype.maven.polyglot.groovy.builder.ModelBuilder;
import org.sonatype.maven.polyglot.io.ModelReaderSupport;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Map;

/**
 * Reads a <tt>pom.groovy</tt> and transforms into a Maven {@link Model}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 0.7
 */
@Component(role=ModelReader.class, hint="groovy")
public class GroovyModelReader
    extends ModelReaderSupport
{
    @Requirement
    protected Logger log;

    @Requirement
    private ModelBuilder builder;

    @Requirement
    private ExecuteManager executeManager;

    public Model read(final Reader input, final Map<String,?> options) throws IOException {
        assert input != null;

        Model model;

        try {
            model = doRead(input, options);
        }
        catch (Throwable t) {
            t = StackTraceUtils.sanitize(t);

            if (t instanceof IOException) {
                throw (IOException)t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException)t;
            }
            if (t instanceof Error) {
                throw (Error)t;
            }
            
            throw new RuntimeException(t);
        }

        // FIXME: Looks like there are cases where the model is loaded more than once

        executeManager.install(model);

        if (log.isDebugEnabled()) {
            DefaultModelWriter writer = new DefaultModelWriter();
            StringWriter buff = new StringWriter();
            writer.write(buff, null, model);
            log.debug("Read groovy model: \n" + buff);
        }

        return model;
    }

    private Model doRead(final Reader input, final Map<String,?> options) throws IOException {
        assert input != null;

        GroovyShell shell = new GroovyShell();
        String text = IOUtil.toString(input);
        String location = PolyglotModelUtil.getLocation(options);
        Script script = shell.parse(new GroovyCodeSource(text, location, location));

        /*
        FIXME: Bring this back as pure java
        
        def include = {source ->
            assert source != null

            def include

            // TODO: Support String, support loading from resource

            if (source instanceof Class) {
                include = source.newInstance()
            }
            else if (source instanceof File) {
                include = shell.parse((File)source)
            }
            else if (source instanceof URL) {
                include = shell.parse(((URL)source).openStream())
            }
            else {
                throw new IllegalArgumentException("Invalid include source: $source")
            }

            include.run()

            // Include each closure variable which starts with '$' and curry in the builder
            include.binding.properties.variables.each {
                if (it.value instanceof Closure && it.key.startsWith('$')) {
                    binding.setVariable(it.key, it.value.curry(builder))
                }
            }
        }

        include(Macros)

        binding.setProperty('$include', include)
        */

        assert builder != null;
        return (Model) builder.build(script);
    }
}
