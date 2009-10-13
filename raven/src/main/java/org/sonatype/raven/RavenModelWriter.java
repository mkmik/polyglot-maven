package org.sonatype.raven;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelWriter;
import org.apache.maven.model.io.ModelReader;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.codehaus.plexus.component.annotations.Component;

@Component(role=ModelWriter.class,hint="raven")
public class RavenModelWriter
    implements ModelWriter
{
    public void write( File output, Map<String, Object> options, Model model )
        throws IOException
    {
    }

    public void write( Writer output, Map<String, Object> options, Model model )
        throws IOException
    {
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setDefaultFlowStyle( DumperOptions.FlowStyle.BLOCK );
        dumperOptions.setIndent( 2 );
        dumperOptions.setWidth( 80 );
        Yaml yaml = new Yaml( dumperOptions );
        yaml.dump( model, output );
    }

    public void write( OutputStream output, Map<String, Object> options, Model model )
        throws IOException
    {
    }
}
