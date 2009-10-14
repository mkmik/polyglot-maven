package org.sonatype.graven

// TODO: Consider making bits that take g:a:v take a string and parse

parent = {builder, g, a, v ->
    builder.parent {
        groupId g
        artifactId a
        version v
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

goals = {builder, Object... items ->
    builder.goals {
        for (item in items) {
            goal item
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

/*
plugin = {builder, g, a, v=null, content=null ->
    builder.plugin {
        groupId g
        artifactId a
        if (v) {
            version v
        }
        if (content) {
            content()
        }
    }
}
*/