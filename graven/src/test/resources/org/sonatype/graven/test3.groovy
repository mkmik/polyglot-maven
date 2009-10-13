project {
    // parent { groupId 'a'; artifactId 'b'; version 'c' }
    $parent('a', 'b', 'c')
    
    dependencies {
        // dependency { groupId 'a'; artifactId 'b'; version 'c' }
        $dependency('a', 'b', 'c')
    }
}
