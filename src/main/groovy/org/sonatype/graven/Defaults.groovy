package org.sonatype.graven

dependency = {parent, gid, aid, v, s=null ->
    parent.dependency {
        groupId gid
        artifactId aid
        version v
        if (s) {
            scope s
        }
    }
}

testdependency = {parent, gid, aid, v ->
    parent.dependency {
        groupId gid
        artifactId aid
        version v
        scope "test"
    }
}

/*
repository = {parent, _id, _name, _url ->
    parent.repository {
        id _id
        name _name
        url _url
    }
}

legacyrepository = {parent, _id, _name, _url ->
    parent.repository {
        id _id
        name _name
        url _url
        layout "legacy"
    }
}

pluginRepository = {parent, _id, _name, _url ->
    parent.pluginRepository {
        id _id
        name _name
        url _url
    }
}
*/