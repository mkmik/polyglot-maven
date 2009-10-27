project {
    modelVersion '4.0.0'
    groupId 'org.sonatype.pmaven'
    artifactId 'pmaven'
    version '1.0-SNAPSHOT'
    packaging 'pom'
    name 'Polyglot Maven'
    build {
        defaultGoal 'install'
        plugins {
            plugin {
                artifactId 'maven-surefire-plugin'
                version '2.4.3'
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
            plugin {
                artifactId 'maven-compiler-plugin'
                version '2.0.2'
                configuration {
                    source '1.5'
                    target '1.5'
                }
            }
            plugin {
                groupId 'org.codehaus.gmaven'
                artifactId 'gmaven-plugin'
                version '1.1-SNAPSHOT'
                executions {
                    execution {
                        goals {
                            goal 'generateStubs'
                            goal 'compile'
                            goal 'generateTestStubs'
                            goal 'testCompile'
                        }
                    }
                }
                configuration {
                    providerSelection '1.7'
                }
            }
            plugin {
                groupId 'org.codehaus.plexus'
                artifactId 'plexus-component-metadata'
                version '1.4.0-SNAPSHOT'
                executions {
                    execution {
                        goals {
                            goal 'generate-metadata'
                            goal 'generate-test-metadata'
                        }
                    }
                }
            }
        }
    }
    modules {
        module 'pmaven-common'
        module 'pmaven-cli'
        module 'pmaven-graven'
        module 'pmaven-raven'
    }
    dependencies {
        dependency {
            groupId 'junit'
            artifactId 'junit'
            version '4.7'
            scope 'test'
        }
        dependency {
            groupId 'org.codehaus.groovy'
            artifactId 'groovy'
            scope 'test'
        }
    }
    dependencyManagement {
        dependencies {
            dependency {
                groupId 'org.apache.maven'
                artifactId 'apache-maven'
                version '${mavenVersion}'
                type 'zip'
                classifier 'bin'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'maven-model-builder'
                version '${mavenVersion}'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'maven-embedder'
                version '${mavenVersion}'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'maven-compat'
                version '${mavenVersion}'
            }
            dependency {
                groupId 'org.codehaus.groovy'
                artifactId 'groovy'
                version '1.7-beta-2'
                exclusions {
                    exclusion {
                        artifactId 'jline'
                        groupId 'jline'
                    }
                    exclusion {
                        artifactId 'junit'
                        groupId 'junit'
                    }
                    exclusion {
                        artifactId 'ant'
                        groupId 'org.apache.ant'
                    }
                    exclusion {
                        artifactId 'ant-launcher'
                        groupId 'org.apache.ant'
                    }
                }
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-common'
                version '1.0-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-cli'
                version '1.0-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-graven'
                version '1.0-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-raven'
                version '1.0-SNAPSHOT'
            }
        }
    }
    properties {
        mavenVersion '3.0-SNAPSHOT'
        'project.build.sourceEncoding' 'UTF-8'
    }
}
