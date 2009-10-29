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

package org.sonatype.maven.polyglot;

import org.apache.maven.classrealm.ClassRealmManagerDelegate;
import org.apache.maven.model.building.ModelProcessor;
import org.codehaus.plexus.classworlds.realm.ClassRealm;
import org.codehaus.plexus.component.annotations.Component;

import java.util.Map;

/**
 * Support for models.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class PolyglotModelUtil
{
    public static String getLocation(final Map<?, ?> options) {
        if (options != null) {
            return options.containsKey(ModelProcessor.LOCATION) ? String.valueOf(options.get(ModelProcessor.LOCATION)) : null;
        }
        return null;
    }
}