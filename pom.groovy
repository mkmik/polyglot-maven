project {
    modelVersion '4.0.0'
    parent {
        artifactId 'forge-parent'
        groupId 'org.sonatype.forge'
        version '5'
    }
    groupId 'org.sonatype.pmaven'
    artifactId 'pmaven'
    version '1.0-SNAPSHOT'
    packaging 'pom'
    name 'Polyglot Maven'
    licenses {
        license {
            name 'The Apache Software License, Version 2.0'
            url 'http://www.apache.org/licenses/LICENSE-2.0.txt'
            distribution 'repo'
        }
    }
    issueManagement {
        system 'JIRA'
        url 'https://issues.sonatype.org/browse/PMAVEN'
    }
    scm {
        connection 'scm:git:git://github.com/sonatype/graven.git'
        developerConnection 'scm:git:ssh://git@github.com/sonatype/graven.git'
        url 'https://github.com/sonatype/graven'
    }
    ciManagement {
        system 'Hudson'
        url 'https://grid.sonatype.org/ci/view/PMaven'
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
        module 'pmaven-common'
        module 'pmaven-maven-plugin'
        module 'pmaven-groovy'
        module 'pmaven-yaml'
        module 'pmaven-cli'
    }
    pluginRepositories {
        pluginRepository {
            releases {
                enabled 'false'
            }
            id 'codehaus.org'
            url 'http://snapshots.repository.codehaus.org'
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
    dependencyManagement {
        dependencies {
            dependency {
                groupId 'org.apache.maven'
                artifactId 'apache-maven'
                version '3.0-alpha-4'
                type 'zip'
                classifier 'bin'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'maven-model-builder'
                version '3.0-alpha-4'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'maven-embedder'
                version '3.0-alpha-4'
            }
            dependency {
                groupId 'org.apache.maven'
                artifactId 'maven-plugin-api'
                version '3.0-alpha-4'
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
                artifactId 'pmaven-maven-plugin'
                version '1.0-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-groovy'
                version '1.0-SNAPSHOT'
            }
            dependency {
                groupId 'org.sonatype.pmaven'
                artifactId 'pmaven-yaml'
                version '1.0-SNAPSHOT'
            }
        }
    }
    properties {
        mavenVersion '3.0-alpha-4'
        'project.build.sourceEncoding' 'UTF-8'
    }
}
