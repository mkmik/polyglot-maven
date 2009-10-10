package com.googlecode.graven;

testdependency = {dependencies, gid, aid, v ->
    dependencies.dependency {
        groupId gid
        artifactId aid
        version v
        scope "test"
    }
}

dependency = {dependencies, gid, aid, v, s = "compile" ->
    dependencies.dependency {
        groupId gid
        artifactId aid
        version v
        scope s
    }
}

compiler = {plugins, version ->
    plugins.plugin {
        groupId "org.apache.maven.plugins"
        artifactId "maven-compiler-plugin"
        configuration {
            source version
            target version
        }
    }
}

executableJar = {plugins, mainclassname ->
    plugins.plugin {
        artifactId "maven-assembly-plugin"
        configuration {
            descriptorRefs {
                descriptorRef "jar-with-dependencies"
            }
            archive {
                manifest {
                    mainClass mainclassname
                }
            }
        }
        executions {
            execution {
                id "make-assembly"
                phase "package"
                goals {
                    goal "attached"
                }
            }
        }
    }
}

repository = {parent, _id, _name, _url ->
    parent.repository {
        id _id
        name _name
        url _url
    }
}

legacyrepository = {parent, _id, _name, _url ->
    parent.repository {
        id _id
        name _name
        url _url
        layout "legacy"
    }
}

pluginRepository = {parent, _id, _name, _url ->
    parent.pluginRepository {
        id _id
        name _name
        url _url
    }
}

groovydeps = {dependencies ->
    dependencies.dependency {
        groupId "org.codehaus.groovy.maven"
        artifactId "gmaven-mojo"
        version "1.0-rc-2"
    }
    dependencies.dependency {
        groupId "org.codehaus.groovy.maven.runtime"
        artifactId "gmaven-runtime-default"
        version "1.0-rc-2"
    }
}

groovy = {plugins ->
    plugins.plugin {
        groupId "org.codehaus.groovy.maven"
        artifactId "gmaven-plugin"
        version "1.0-rc-2"
        executions {
            execution {
                goals {
                    goal "compile"
                    goal "testCompile"
                }
            }
        }
    }
}

antlr = {plugins, debugEnabled ->
    plugins.plugin {
        groupId "org.codehaus.mojo"
        artifactId "antlr3-maven-plugin"
        configuration {
            debug "${debugEnabled}"
        }
        executions {
            execution {
                goals {
                    goal "antlr"
                }
            }
        }
    }
}

xjc = {executions, name, pkg, schema, dir, clear = "true" ->
    executions.execution {
        id name
        goals {
            goal "xjc"
        }
        configuration {
            if (pkg != null) {
			    packageName pkg
			}
            schemaFiles schema
            staleFile '${project.build.directory}/generated-sources/jaxb/.' + name + 'StaleFlag'
            clearOutputDir clear
            schemaDirectory dir
        }
    }
}

war = {plugins, dir ->
    plugins.plugin {
        groupId "org.apache.maven.plugins"
        artifactId "maven-war-plugin"
        executions {
            execution {
                goals {
                    goal "war"
                }
            }
        }
        configuration {
            warSourceDirectory dir
        }
    }
}