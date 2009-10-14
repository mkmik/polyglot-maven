package org.sonatype.graven

/**
 * Default pom macros.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */

private Map parseArtifact(final String... items) {
    assert items != null && items.size() != 0

    if (items.size() == 1 && items[0].contains(':')) {
        return parseArtifact(items[0].split(':'))
    }

    def map = [:]

    switch (items.size()) {
        case 5:
            map.groupId = items[0]
            map.artifactId = items[1]
            map.type = items[2]
            map.classifier = items[3]
            map.version = items[4]
            break

        case 3:
            map.groupId = items[0]
            map.artifactId = items[1]
            map.version = items[2]
            break

        case 2:
            map.groupId = items[0]
            map.artifactId = items[1]
            break

        default:
            throw new IllegalArgumentException("Unable to parse artifact for: ${items}")
    }

    return map
}

$artifact = {builder, String... items ->
    def map = parseArtifact(items)
    
    map.each {
        builder."${it.key}" it.value
    }
}

private Map parseParent(final String... items) {
    assert items != null && items.size() != 0

    if (items.size() == 1 && items[0].contains(':')) {
        return parseParent(items[0].split(':'))
    }

    def map = [:]

    switch (items.size()) {
        case 3:
            map.groupId = items[0]
            map.artifactId = items[1]
            map.version = items[2]
            break

        default:
            throw new IllegalArgumentException("Unable to parse parent for: ${items}")
    }

    return map
}

$parent = {builder, String... items ->
    def map = parseParent(items)
    
    builder.parent {
        groupId map.groupId
        artifactId map.artifactId
        version map.version
    }
}

$gav = {builder, g, a, v=null ->
    builder.groupId g
    builder.artifactId a
    if (v) {
        builder.version v
    }
}

$dependency = {builder, g, a, v=null, s=null ->
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

private Map parseExclusion(final String... items) {
    assert items != null && items.size() != 0

    if (items.size() == 1 && items[0].contains(':')) {
        return parseExclusion(items[0].split(':'))
    }

    def map = [:]

    switch (items.size()) {
        case 2:
            map.groupId = items[0]
            map.artifactId = items[1]
            break

        default:
            throw new IllegalArgumentException("Unable to parse exclusion for: ${items}")
    }

    return map
}

$exclusion = {builder, String... items ->
    def map = parseExclusion(items)

    builder.exclusion {
        groupId map.groupId
        artifactId map.artifactId
    }
}

$exclusions = {builder, String... items ->
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

$goals = {builder, String... items ->
    builder.goals {
        for (item in items) {
            goal item
        }
    }
}

$modules = {builder, String... items ->
    builder.modules {
        for (item in items) {
            module item
        }
    }
}

$configuration = {builder, Map items ->
    builder.configuration {
        for (item in items) {
            "${item.key}"(item.value)
        }
    }
}

$includes = {builder, Object... items ->
    builder.includes {
        for (item in items) {
            include item
        }
    }
}

$excludes = {builder, Object... items ->
    builder.excludes {
        for (item in items) {
            exclude item
        }
    }
}

$uuid = {builder, prefix=null ->
    def val = UUID.randomUUID().toString()
    if (prefix) {
        val = "${prefix}${val}"
    }
    builder.id val
}