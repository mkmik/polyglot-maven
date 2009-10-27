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

package org.sonatype.maven.polyglot.groovy.builder;

import org.codehaus.groovy.runtime.InvokerHelper;
import org.apache.maven.model.Model;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Extension;
import org.apache.maven.model.Resource;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.Notifier;
import org.apache.maven.model.Contributor;
import org.apache.maven.model.Developer;
import org.apache.maven.model.License;
import org.apache.maven.model.MailingList;
import org.apache.maven.model.Profile;
import org.apache.maven.model.Repository;
import org.apache.maven.model.Exclusion;
import org.apache.maven.model.PluginExecution;
import org.apache.maven.model.Parent;
import groovy.util.FactoryBuilderSupport;
import groovy.util.Factory;
import groovy.lang.Closure;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.HashSet;

/**
 * Builds Maven {@link Model} instances.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 *
 * @since 1.0
 */
public class ModelBuilder
    extends FactoryBuilderSupport
{
    private final Set<String> factoryNames = new HashSet<String>();

    private final Set<Class> factoryTypes = new HashSet<Class>();

    public ModelBuilder() {
        registerFactories();
    }

    @Override
    protected void setClosureDelegate(final Closure c, final Object o) {
        c.setDelegate(this);
        c.setResolveStrategy(Closure.DELEGATE_FIRST);
    }

    @Override
    public void setVariable(final String name, final Object value) {
        InvokerHelper.setProperty(getCurrent(), name, value);
    }

    public void registerFactories() {
        registerBeanFactory("project", Model.class);
        registerStringFactory("module");
        registerFactory(new ModulesFactory());
        registerChildFactory("dependency", Dependency.class);
        registerChildFactory("exclusion", Exclusion.class);
        registerFactory(new ExclusionsFactory());
        registerChildFactory("extension", Extension.class);
        registerStringFactory("filter");
        registerChildFactory("resource", Resource.class);
        registerChildFactory("testResource", Resource.class);
        registerFactory(new IncludesFactory());
        registerStringFactory("include");
        registerFactory(new ExcludesFactory());
        registerStringFactory("exclude");
        registerChildFactory("plugin", Plugin.class);
        registerChildFactory("execution", PluginExecution.class);
        registerFactory(new GoalsFactory());
        registerStringFactory("goal");
        registerChildFactory("notifier", Notifier.class);
        registerChildFactory("contributor", Contributor.class);
        registerStringFactory("role");
        registerChildFactory("developer", Developer.class);
        registerChildFactory("license", License.class);
        registerChildFactory("mailingList", MailingList.class);
        registerStringFactory("otherArchive");
        registerChildFactory("profile", Profile.class);
        registerChildFactory("pluginRepository", Repository.class);
        registerChildFactory("repository", Repository.class);
        registerFactory(new ExecuteFactory());
    }

    @Override
    public void registerBeanFactory(final String name, final Class type) {
        super.registerBeanFactory(name, type);
        registerFactoriesFor(Model.class);
    }

    @Override
    public void registerFactory(final String name, final String groupName, final Factory factory) {
        // System.out.println("Factory: " + name);
        factoryNames.add(name);
        super.registerFactory(name, groupName, factory);
    }

    public void registerFactory(final NamedFactory factory) {
        assert factory != null;
        registerFactory(factory.getName(), factory);
    }

    public void registerChildFactory(final String name, final Class type) {
        registerFactory(createChildFactory(name, type));
        registerFactoriesFor(type);
    }

    private NamedFactory createChildFactory(final String name, final Class type) {
        assert name != null;
        assert type != null;

        if (type == Parent.class) {
            return new ParentFactory();
        }
        if (type == Dependency.class) {
            return new DependencyFactory();
        }
        if (type == Exclusion.class) {
            return new ExclusionFactory();
        }

        return new ChildFactory(name, type);
    }

    public void registerStringFactory(final String... names) {
        for (String name : names) {
            registerFactory(new StringFactory(name));
        }
    }

    public void registerListFactory(final String name) {
        registerFactory(new ListFactory(name));
    }

    public void registerPropertiesFactory(final String name) {
        registerFactory(new PropertiesFactory(name));
    }

    public void registerObjectFactory(final String name) {
        registerFactory(new ObjectFactory(name));
    }

    private void registerFactoriesFor(final Class type) {
        assert type != null;

        if (factoryTypes.contains(type)) {
            return;
        }
        factoryTypes.add(type);

        // System.out.println("Registering factories for type: " + type);
        
        Method[] methods = type.getMethods();
        for (Method method : methods) {
            if (isSetter(method) ){
                String name = propertyNameOf(method);

                if (factoryNames.contains(name)) {
                    continue;
                }

                Class param = method.getParameterTypes()[0];
                if (param == String.class) {
                    registerStringFactory(name);
                }
                else if (param == List.class) {
                    registerListFactory(name);
                }
                else if (param == Properties.class) {
                    registerPropertiesFactory(name);
                }
                else if (param == Object.class) {
                    registerObjectFactory(name);
                }
                else if (param.getName().startsWith("org.apache.maven.model.")) {
                    registerChildFactory(name, param);
                }
                else {
                    // Skip setters with unsupported types (model will use string versions)
                }
            }
        }
    }

    private boolean isSetter(final Method method) {
        assert method != null;
        if (!method.getName().startsWith("set")) {
            return false;
        }
        
        if (method.getParameterTypes().length > 1) {
            return false;

        }
        if (method.getReturnType() != Void.TYPE) {
            return false;
        }

        int m = method.getModifiers();
        if (!Modifier.isPublic(m) || Modifier.isStatic(m)) {
            return false;
        }

        return true;
    }

    private String propertyNameOf(final Method method) {
        assert method != null;

        String name = method.getName();
        name = name.substring(3, name.length());
        
        return new StringBuffer(name.length())
                .append(Character.toLowerCase(name.charAt(0)))
                .append(name.substring(1))
                .toString();
    }
}