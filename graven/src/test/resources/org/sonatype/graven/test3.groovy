project {
    // parent { groupId 'a'; artifactId 'b'; version 'c' }
    $parent('a', 'b', 'c')
    
    dependencies {
        // dependency { groupId 'a'; artifactId 'b'; version 'c' }
        $dependency('a', 'b', 'c')

        dependency {
            groupId 'bar'
            artifactId 'foo'
            version 'baz'
            exclusions {
                $exclusion('a', 'b')
            }
        }

        dependency {
            groupId '1'
            artifactId '2'
            version '3'
            $exclusions('a:b', 'c:d')
        }
    }

    $modules('a', 'b', 'c')

    build {
        plugins {
            plugin {
                groupId 'a'
                artifactId 'b'
                executions {
                    execution {
                        phase 'compile'
                        $goals('foo', 'bar')
                        configuration {
                            $includes('a', 'b', 3)
                            $excludes(1, true, 'b')
                        }
                    }
                    execution {
                        $configuration(foo: "bar", a: "b")
                    }
                }
            }
        }
    }
}
