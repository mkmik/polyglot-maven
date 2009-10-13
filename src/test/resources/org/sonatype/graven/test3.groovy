project {
    // parent { groupId 'a'; artifactId 'b'; version 'c' }
    $parent('a', 'b', 'c')
    
    dependencies {
        $dependency("a", "b", "c")
    }
}
