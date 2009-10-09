package org.sonatype.graven;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Map;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.ModelParseException;
import org.apache.maven.model.io.ModelReader;

public class GravenModelReader
    implements ModelReader
{
    public Model read( File input, Map options )
        throws IOException, ModelParseException
    {
        return null;
    }

    public Model read( Reader input, Map options )
        throws IOException, ModelParseException
    {
        return null;
    }

    public Model read( InputStream input, Map options )
        throws IOException, ModelParseException
    {
        return null;
    }
}
