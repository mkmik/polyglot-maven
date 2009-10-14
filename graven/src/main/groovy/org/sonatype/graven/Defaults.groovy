package org.sonatype.graven

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