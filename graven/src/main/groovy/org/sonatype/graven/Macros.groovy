package org.sonatype.graven

/**
 * Default pom macros.
 *
 * @author <a href="mailto:jason@planet57.com">Jason Dillon</a>
 */

def vars = binding.properties.variables

//
// FIXME: Create parse helper to share spome of this, need to accept objects not strings
//

parseArtifact = {String... items ->
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

        case 4:
            map.groupId = items[0]
            map.artifactId = items[1]
            map.classifier = items[2]
            map.version = items[3]
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
            throw new IllegalArgumentException("Unable to parse artifact for: $items")
    }

    return map
}


vars.'$artifact' = {builder, String... items ->
    def map = parseArtifact(items)

    map.each {
        builder."${it.key}" it.value
    }
}

parseParent = {String... items ->
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
            throw new IllegalArgumentException("Unable to parse parent for: $items")
    }

    return map
}

vars.'$parent' = {builder, String... items ->
    def map = parseParent(items)

    builder.parent {
        groupId map.groupId
        artifactId map.artifactId
        version map.version
    }
}

parseGAV = {String... items ->
    assert items != null && items.size() != 0

    if (items.size() == 1 && items[0].contains(':')) {
        return parseGAV(items[0].split(':'))
    }

    def map = [:]

    switch (items.size()) {
        case 3:
            map.groupId = items[0]
            map.artifactId = items[1]
            map.version = items[2]
            break

        case 2:
            map.groupId = items[0]
            map.artifactId = items[1]
            break

        case 1:
            map.artifactId = items[1]
            break

        default:
            throw new IllegalArgumentException("Unable to parse GAV for: $items")
    }

    return map
}

vars.'$gav' = {builder, String... items ->
    def map = parseGAV(items)

    builder.groupId map.groupId
    if (map.artifactId) {
        builder.artifactId map.artifactId
    }
    if (map.version) {
        builder.version map.version
    }
}

parseDependency = {String... items ->
    assert items != null && items.size() != 0

    if (items.size() == 1 && items[0].contains(':')) {
        return parseDependency(items[0].split(':'))
    }

    def map = [:]

    switch (items.size()) {
        case 4:
            map.groupId = items[0]
            map.artifactId = items[1]
            map.version = items[2]
            map.scope = items[3]
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
            throw new IllegalArgumentException("Unable to parse dependency for: $items")
    }

    return map
}

vars.'$dependency' = {builder, String... items ->
    def map = parseDependency(items)

    builder.dependency {
        groupId map.groupId
        artifactId map.artifactId
        if (map.version) {
            version map.version
        }
        if (map.scope) {
            scope map.scope
        }
    }
}

parseExclusion = {String... items ->
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
            throw new IllegalArgumentException("Unable to parse exclusion for: $items")
    }

    return map
}

vars.'$exclusion' = {builder, String... items ->
    def map = parseExclusion(items)

    builder.exclusion {
        groupId map.groupId
        artifactId map.artifactId
    }
}

vars.'$exclusions' = {builder, String... items ->
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

vars.'$goals' = {builder, String... items ->
    builder.goals {
        for (item in items) {
            goal item
        }
    }
}

vars.'$modules' = {builder, String... items ->
    builder.modules {
        for (item in items) {
            module item
        }
    }
}

vars.'$configuration' = {builder, Map items ->
    builder.configuration {
        for (item in items) {
            "${item.key}"(item.value)
        }
    }
}

vars.'$includes' = {builder, Object... items ->
    builder.includes {
        for (item in items) {
            include item
        }
    }
}

vars.'$excludes' = {builder, Object... items ->
    builder.excludes {
        for (item in items) {
            exclude item
        }
    }
}

vars.'$uuid' = {builder, prefix=null ->
    def val = UUID.randomUUID().toString()
    if (prefix) {
        val = "${prefix}${val}"
    }
    builder.id val
}