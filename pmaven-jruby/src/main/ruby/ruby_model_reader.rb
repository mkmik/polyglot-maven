java_import 'org.apache.maven.model.Model'
java_import 'org.apache.maven.model.io.ModelReader'
java_import 'java.io.InputStream'
java_import 'java.io.Reader'
java_import 'java.io.File'
java_import 'java.util.Map'

class RubyModuleReader
  java_implements :ModelReader
  
  java_signature ['InputStream', 'Map<String,?>'] => :Model
  java_name :read
  def read_stream(stream, map)
    puts 'hello'
  end

  java_signature ['Reader', 'Map<String,?>'] => :Model
  java_name :read
  def read_reader(reader, map)
    puts 'hello'
  end

  java_signature ['File', 'Map<String,?>'] => :Model
  java_name :read
  def read_file(file, map)
    puts 'hello'
  end
end
