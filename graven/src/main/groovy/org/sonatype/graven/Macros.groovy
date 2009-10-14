package org.sonatype.graven

/**
 * Default pom macros.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */

private Map parseArtifact(final String... items) {
    assert items != null && items.size() != 0
    def artifact = [:]

    switch (items.size()) {
        case 5:
            artifact.groupId = items[0]
            artifact.artifactId = items[1]
            artifact.type = items[2]
            artifact.classifier = items[3]
            artifact.version = items[4]
            break

        case 3:
            artifact.groupId = items[0]
            artifact.artifactId = items[1]
            artifact.version = items[2]
            break

        case 2:
            artifact.groupId = items[0]
            artifact.artifactId = items[1]
            break

        default:
            throw new IllegalArgumentException("Unable to parse artifact for: ${items}")
    }

    return artifact
}

private Map parseArtifact(final String spec) {
    assert spec != null
    return parseArtifact(spec.trim().split(':'))
}

artifact = {builder, String spec ->
    Map map = parseArtifact(spec);
    map.each {
        builder."${it.key}" it.value
    }
}

// TODO: Consider making bits that take g:a:v take a string and parse

parent = {builder, g, a, v, p=null ->
    builder.parent {
        groupId g
        artifactId a
        version v
        if (p) {
            relativePath p
        }
    }
}

gav = {builder, g, a, v=null ->
    builder.groupId g
    builder.artifactId a
    if (v) {
        builder.version v
    }
}

dependency = {builder, g, a, v=null, s=null ->
    builder.dependency {
        groupId g
        artifactId a
        if (v) {
            version v
        }
        if (s) {
            scope s
        }
    }
}

exclusion = {builder, g, a ->
    builder.exclusion {
        groupId g
        artifactId a
    }
}

exclusions = {builder, String... items ->
    builder.exclusions {
        for (String item in items) {
            def artifact = parseArtifact(item)
            exclusion {
                groupId artifact.groupId
                artifactId artifact.artifactId
            }
        }
    }
}

goals = {builder, String... items ->
    builder.goals {
        for (item in items) {
            goal item
        }
    }
}

modules = {builder, String... items ->
    builder.modules {
        for (item in items) {
            module item
        }
    }
}

configuration = {builder, Map items ->
    builder.configuration {
        for (item in items) {
            "${item.key}"(item.value)
        }
    }
}

includes = {builder, Object... items ->
    builder.includes {
        for (item in items) {
            include item
        }
    }
}

excludes = {builder, Object... items ->
    builder.excludes {
        for (item in items) {
            exclude item
        }
    }
}

uuid = {builder, prefix=null ->
    def val = UUID.randomUUID().toString()
    if (prefix) {
        val = "${prefix}${val}"
    }
    builder.id val
}