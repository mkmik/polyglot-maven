package org.sonatype.raven;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelWriter;
import org.yaml.snakeyaml.Dumper;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.nodes.Tags;
import org.yaml.snakeyaml.representer.Representer;
import org.codehaus.plexus.component.annotations.Component;
import org.sonatype.maven.polyglot.io.ModelWriterSupport;

@Component(role = ModelWriter.class, hint = "raven")
public class RavenModelWriter
    extends ModelWriterSupport
{
    public void write( Writer output, Map<String, Object> o, Model model )
        throws IOException
    {
        DumperOptions options = new DumperOptions();
        options.setExplicitRoot( Tags.MAP );
        options.setDefaultFlowStyle( FlowStyle.AUTO );
        options.setIndent( 2 );
        options.setWidth( 80 );

        Representer representer = new ModelRepresenter();

        Dumper dumper = new Dumper( representer, options );
        Yaml yaml = new Yaml( dumper );
        yaml.dump( model, output );
    }

}
