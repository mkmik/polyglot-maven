package org.sonatype.graven

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
        for (item in items) {
            // TODO: Use a Maven parser here
            def ab = item.split(':')
            exclusion {
                groupId ab[0]
                artifactId ab[1]
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
