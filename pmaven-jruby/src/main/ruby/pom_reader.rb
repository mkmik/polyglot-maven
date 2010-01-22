require 'rubygems'
require 'builder'

# read the pom
file = ARGV[0] or raise "no file specified"
src = File.read(file)

# construct a builder
builder = Builder::XmlMarkup.new(:target => STDOUT, :indent => 2)

# define a _module method since module is a keyword
def builder._module(*args, &block); self.module(*args, &block); end

# execute the pom against builder
builder.instance_eval(src)
