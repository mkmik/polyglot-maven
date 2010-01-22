project {
    modelVersion '4.0.0'
    parent {
        artifactId 'forge-parent'
        groupId 'org.sonatype.forge'
        version '5'
    }
    groupId 'org.sonatype.pmaven'
    artifactId 'pmaven'
    version '0.7-SNAPSHOT'
    packaging 'pom'
    name 'Polyglot Maven'
    url 'https://docs.sonatype.org/display/PMAVEN'
    licenses {
        license {
            name 'The Apache Software License, Version 2.0'
            url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
            distribution 'repo'
        }
    }
    scm {
        connection 'scm:git:git://github.com/sonatype/polyglot-maven.git'
        developerConnection 'scm:git:ssh://git@github.com/sonatype/polyglot-maven.git'
        url 'https://github.com/sonatype/polyglot-maven'
    }
    issueManagement {
        system 'JIRA'
        url 'https://issues.sonatype.org/browse/PMAVEN'
    }
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
                version '1.2'
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
                version '1.5.1'
                executions {
                    execution {
                        goals {
                            goal 'generate-metadata'
                            goal 'generate-test-metadata'
                        }
                    }
                }
            }
            plugin {
                artifactId 'maven-release-plugin'
                version '2.0-beta-9'
                configuration {
                    useReleaseProfile 'false'
                    goals 'deploy'
                    arguments '-B -Prelease'
                    autoVersionSubmodules 'true'
                }
            }
            plugin {
                artifactId 'maven-scm-plugin'
                version '1.2'
            }
        }
    }
    modules {
        _module 'pmaven-common'
        _module 'pmaven-maven-plugin'
        _module 'pmaven-groovy'
        _module 'pmaven-yaml'
        _module 'pmaven-clojure'
        _module 'pmaven-scala'
        _module 'pmaven-cli'
    }
    properties {
        mavenVersion '3.0-alpha-6'
        'project.build.sourceEncoding' 'UTF-8'
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
                artifactId 'maven-plugin-api'
                version '${mavenVersion}'
            }
            dependency {
                groupId 'org.codehaus.groovy'
                artifactId 'groovy'
                version '1.7.0'
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
                groupId 'org.sonatype.gossip'
                artifactId 'gossip'
                version '1.2'
            }
            dependency {
                groupId 'com.google.inject'
                artifactId 'guice'
                version '2.0'
            }
            dependency {
                groupId 'org.sonatype.grrrowl'
                artifactId 'grrrowl'
                version '1.0'
            }
            dependency {
                groupId 'net.java.dev.jna'
                artifactId 'jna'
                version '3.2.3'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-common'
                version '0.7-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-cli'
                version '0.7-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-maven-plugin'
                version '0.7-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-groovy'
                version '0.7-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-yaml'
                version '0.7-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-clojure'
                version '0.7-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-scala'
                version '0.7-SNAPSHOT'
            }
        }
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
}
