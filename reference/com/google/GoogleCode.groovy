package com.google

googlecode = {project, name ->
    project.scm {
        connection "scm:svn:http://${name}.googlecode.com/svn/trunk"
        developerConnection "scm:svn:https://${name}.googlecode.com/svn/trunk"
        url "http://${name}.googlecode.com/svn"
    }

    project.issueManagement {
        system "Google Code"
        url "http://code.google.com/p/${name}/issues/list"
    }
}
