/*
 * Copyright (C) 2009 the original author(s).
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

package org.sonatype.maven.polyglot.groovy.execute;

import groovy.lang.Closure;
import org.sonatype.maven.polyglot.execute.ExecuteContainer;
import org.sonatype.maven.polyglot.execute.ExecuteContext;

import java.util.Map;

/**
 * ???
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class GroovyExecuteContainer
    implements ExecuteContainer
{
    private final Object value;

    private final Map attrs;

    private String phase;

    private Closure closure;

    public GroovyExecuteContainer(final Object value, final Map attrs) {
        this.value = value;
        this.attrs = attrs;
    }

    public Object getValue() {
        return value;
    }

    public Map getAttributes() {
        return attrs;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(final String phase) {
        this.phase = phase;
    }

    public Closure getClosure() {
        return closure;
    }

    public void setClosure(final Closure closure) {
        this.closure = closure;
    }

    public void execute(final ExecuteContext context) throws Exception {
        // TODO:
    }
}