package org.sonatype.raven;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
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
import org.codehaus.plexus.util.IOUtil;

@Component(role = ModelWriter.class, hint = "raven")
public class RavenModelWriter
    implements ModelWriter
{
    public void write(final File file, final Map<String,Object> options, final Model model) throws IOException {
        assert file != null;
        assert model != null;

        Writer out = new BufferedWriter(new FileWriter(file));
        try {
            write(out, options, model);
            out.flush();
        }
        finally {
            IOUtil.close(out);
        }
    }

    public void write( OutputStream output, Map<String, Object> options, Model model )
        throws IOException
    {
        write( new OutputStreamWriter( output ), options, model );
    }

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
