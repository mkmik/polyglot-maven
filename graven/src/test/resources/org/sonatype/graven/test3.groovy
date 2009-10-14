project {
    // parent { groupId 'a'; artifactId 'b'; version 'c' }
    $parent('a', 'b', 'c')
    
    dependencies {
        // dependency { groupId 'a'; artifactId 'b'; version 'c' }
        $dependency('a', 'b', 'c')
    }

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
