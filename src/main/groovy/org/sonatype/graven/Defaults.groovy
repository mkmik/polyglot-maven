package org.sonatype.graven

parent = {builder, gid, aid, v ->
    builder.parent {
        groupId gid
        artifactId aid
        version v
    }
}

dependency = {builder, gid, aid, v, s=null ->
    builder.dependency {
        groupId gid
        artifactId aid
        version v
        if (s) {
            scope s
        }
    }
}

testdependency = {builder, gid, aid, v ->
    builder.dependency {
        groupId gid
        artifactId aid
        version v
        scope "test"
    }
}
