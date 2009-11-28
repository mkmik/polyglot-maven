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

package org.sonatype.maven.polyglot.groovy.builder.factory;

import groovy.util.FactoryBuilderSupport;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Reporting;

import java.util.Map;

/**
 * Builds {@link org.apache.maven.model.Reporting} nodes.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ReportingFactory
    extends NamedFactory
{
    public ReportingFactory() {
        super("reporting");
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attrs) throws InstantiationException, IllegalAccessException {
        builder.getContext().put(Reporting.class.getName(), true);
        return new Reporting();
    }

    @Override
    public void onNodeCompleted(FactoryBuilderSupport builder, Object parent, Object node) {
        builder.getContext().remove(Reporting.class.getName());
    }
}