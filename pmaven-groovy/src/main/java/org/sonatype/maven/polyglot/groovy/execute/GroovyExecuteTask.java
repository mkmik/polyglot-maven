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

package org.sonatype.maven.polyglot.groovy.execute;

import groovy.lang.Closure;
import org.codehaus.groovy.runtime.StackTraceUtils;
import org.sonatype.maven.polyglot.execute.ExecuteContext;
import org.sonatype.maven.polyglot.execute.ExecuteTask;
import org.sonatype.maven.polyglot.execute.ExecuteTaskSupport;

import java.util.Map;

/**
 * Encapsulates a Groovy {@link ExecuteTask}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class GroovyExecuteTask
    extends ExecuteTaskSupport
{
    private final Object value;

    private final Map attrs;

    private Closure closure;

    public GroovyExecuteTask(final Object value, final Map attrs) {
        this.value = value;
        this.attrs = attrs;
    }

    public Object getValue() {
        return value;
    }

    public Map getAttributes() {
        return attrs;
    }

    public Closure getClosure() {
        return closure;
    }

    public void setClosure(final Closure closure) {
        this.closure = closure;
    }

    public void execute(final ExecuteContext context) throws Exception {
        try {
            getClosure().call();
        }
        catch (Throwable t) {
            t = StackTraceUtils.sanitize(t);

            if (t instanceof RuntimeException) {
                throw (RuntimeException)t;
            }
            if (t instanceof Exception) {
                throw (Exception)t;
            }
            if (t instanceof Error) {
                throw (Error)t;
            }

            throw new RuntimeException(t);
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
            "id='" + getId() + '\'' +
            ", phase='" + getPhase() + '\'' +
            ", value=" + value +
            ", attrs=" + attrs +
            ", closure=" + closure +
            '}';
    }
}