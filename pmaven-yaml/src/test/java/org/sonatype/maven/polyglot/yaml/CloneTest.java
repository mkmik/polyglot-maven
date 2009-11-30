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

package org.sonatype.maven.polyglot.yaml;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelWriter;
import org.junit.Test;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;

import static org.junit.Assert.assertNotNull;

public class CloneTest
{
    @Test
    public void testModelCloning()
        throws Exception
    {
        getModel().clone();
    }

    @Test
    public void testModelWriter()
        throws Exception
    {
        StringWriter sw = new StringWriter();
        ModelWriter writer = new YamlModelWriter();
        Model model = getModel();
        Properties p = new Properties();
        p.setProperty( "FOO", "BAR" );
        model.setProperties( p );
        writer.write( sw, null, model );
        System.out.println( sw.toString() );
    }

    protected Model getModel()
        throws Exception
    {
        YamlModelReader modelReader = new YamlModelReader();
        URL url = getClass().getResource("test2.yml");
        assertNotNull(url);
        InputStream reader = url.openStream();
        return modelReader.read( reader, null );
    }
}