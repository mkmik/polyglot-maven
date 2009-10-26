package org.sonatype.maven.polyglot.groovy.builder

import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.sonatype.maven.graven.GravenModelTestSupport
import org.sonatype.maven.polyglot.groovy.builder.ModelBuilder

/**
 * Tests for {@link ModelBuilder}.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */
public class ModelBuilderTest
    extends GravenModelTestSupport
{
    private ModelBuilder builder

    @Before
    void setUp() {
        builder = new ModelBuilder()
    }

    @Test
    void testBuildWithElements() {
        def model = builder.project {
            parent {
                groupId 'a'
                artifactId 'b'
                version 'c'
            }

            dependencies {
                dependency {
                    groupId 'a'
                    artifactId 'b'
                    version 'c'
                }
            }
        }

        assertNotNull(model)

        def p = model.parent
        assertNotNull(p);

        assertEquals('a', p.groupId)
        assertEquals('b', p.artifactId)
        assertEquals('c', p.version)

        assertNotNull(model.dependencies)
        assertEquals(1, model.dependencies.size())

        def d = model.dependencies[0]
        assertEquals('a', d.groupId)
        assertEquals('b', d.artifactId)
        assertEquals('c', d.version)
    }

    @Test
    void testBuildWithAttributes() {
        def model = builder.project {
            parent(groupId: 'a', artifactId :'b', version: 'c')

            dependencies {
                dependency(groupId: 'a', artifactId: 'b', version: 'c')
            }
        }

        assertNotNull(model)

        def p = model.parent
        assertNotNull(p);

        assertEquals('a', p.groupId)
        assertEquals('b', p.artifactId)
        assertEquals('c', p.version)

        assertNotNull(model.dependencies)
        assertEquals(1, model.dependencies.size())

        def d = model.dependencies[0]
        assertEquals('a', d.groupId)
        assertEquals('b', d.artifactId)
        assertEquals('c', d.version)
    }

    @Test
    void testBuildWithConfiguration() {
        def model = builder.project {
            build {
                plugins {
                    plugin {
                        configuration {
                            foo 'a'
                            bar """
                                blah
                            """
                        }
                    }
                }
            }
        }

        assertNotNull(model)
    }

    @Test
    void testBuildWithProperties() {
        def model = builder.project {
            properties {
                foo 'a'
            }
        }

        assertNotNull(model)
    }

    @Test
    void testBuildParseParent() {
        def model = builder.project {
            parent("a:b:c")
        }

        assertNotNull(model)

        def p = model.parent
        assertNotNull(p);

        assertEquals('a', p.groupId)
        assertEquals('b', p.artifactId)
        assertEquals('c', p.version)
    }

    @Test
    void testBuildParseGoals1() {
        def model = builder.project {
            build {
                plugins {
                    plugin {
                        executions {
                            execution {
                                goals("foo")
                            }
                        }
                    }
                }
            }
        }

        assertNotNull(model)

        def g = model?.build?.plugins[0]?.executions[0].goals
        assertNotNull(g)
        assertEquals(1, g.size())
        assertEquals("foo", g[0])
    }

    @Test
    void testBuildParseGoals2() {
        def model = builder.project {
            build {
                plugins {
                    plugin {
                        executions {
                            execution {
                                goals("foo", "bar", "baz")
                            }
                        }
                    }
                }
            }
        }

        assertNotNull(model)

        def g = model?.build?.plugins[0]?.executions[0].goals
        assertNotNull(g)
        assertEquals(3, g.size())
        assertEquals(["foo", "bar", "baz"], g)
    }

    @Test
    void testBuildParseExclusions1() {
        def model = builder.project {
            dependencies {
                dependency {
                    exclusions("foo:bar")
                }
            }
        }

        assertNotNull(model)

        def e = model?.dependencies[0]?.exclusions
        assertNotNull(e)
        assertEquals(1, e.size())
        assertEquals("foo", e[0].groupId)
        assertEquals("bar", e[0].artifactId)
    }

    @Test
    void testBuildParseExclusions2() {
        def model = builder.project {
            dependencies {
                dependency {
                    exclusions("foo:bar", "a:b")
                }
            }
        }

        assertNotNull(model)

        def e = model?.dependencies[0]?.exclusions
        assertNotNull(e)
        assertEquals(2, e.size())
        assertEquals("foo", e[0].groupId)
        assertEquals("bar", e[0].artifactId)
        assertEquals("a", e[1].groupId)
        assertEquals("b", e[1].artifactId)
    }
}
