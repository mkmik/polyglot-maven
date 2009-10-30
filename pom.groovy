project {
    modelVersion '4.0.0'
    groupId 'org.sonatype.pmaven'
    artifactId 'pmaven'
    version '1.0-SNAPSHOT'
    packaging 'pom'
    name 'Polyglot Maven'

    properties {
        mavenVersion '3.0-SNAPSHOT'
        'project.build.sourceEncoding' 'UTF-8'
    }

    def mods = ['pmaven-common','pmaven-cli','pmaven-maven-plugin', 'pmaven-groovy','pmaven-yaml','pmaven-jruby']

    dependencyManagement {
        dependencies {
            dependency {
                groupId 'org.apache.maven'
                artifactId 'apache-maven'
                version '${mavenVersion}'
                type 'zip'
                classifier 'bin'
            }

            ['maven-model-builder', 'maven-embedder', 'maven-compat', 'maven-plugin-api'].each { artifact ->
                dependency {
                    groupId 'org.apache.maven'
                    artifactId "$artifact"
                    version '${mavenVersion}'
                }
            }

            dependency('org.codehaus.groovy:groovy:1.7-beta-2') {
                exclusions('jline:jline', 'junit:junit', 'org.apache.ant:ant', 'org.apache.ant:ant-launcher')
            }

            // INTERNAL

            mods.each { artifact ->
                dependency {
                    groupId 'org.sonatype.pmaven'
                    artifactId "$artifact"
                    version '1.0-SNAPSHOT'
                }
            }
        }
    }

    dependencies {
        dependency('junit:junit:4.7:test')
        dependency('org.codehaus.groovy:groovy') {
            scope 'test'
        }
    }

    build {
        defaultGoal 'install'
        plugins {
            plugin('org.apache.maven.plugins:maven-surefire-plugin:2.4.3') {
                configuration {
                    redirectTestOutputToFile 'true'
                    forkMode 'once'
                    argLine '-ea'
                    failIfNoTests 'false'
                    workingDirectory '${project.build.directory}'
                    excludes {
                        exclude '**/Abstract*.java'
                        exclude '**/Test*.java'
                    }
                    includes {
                        include '**/*Test.java'
                    }
                }
            }
            plugin('org.apache.maven.plugins:maven-compiler-plugin:2.0.2') {
                configuration {
                    source '1.5'
                    target '1.5'
                }
            }
            plugin('org.codehaus.gmaven:gmaven-plugin:1.1-SNAPSHOT') {
                executions {
                    execution {
                        goals('generateStubs','compile','generateTestStubs','testCompile')
                    }
                }
                configuration {
                    providerSelection '1.7'
                }
            }

            plugin('org.codehaus.plexus:plexus-component-metadata:1.4.0-SNAPSHOT') {
                executions {
                    execution(goals: 'generate-metadata')
                }
            }
        }
    }

    modules(*mods)
}
