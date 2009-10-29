Description
-----------

Provides Polgyglot Maven the ability to understand Groovy POM files.

Example POM
------------------
    project {
        groupId 'foo'
        artifactId 'bar'
        version '1.0'
    }

Custom Element Value Parsing
----------------------------

### parent

#### Example
    parent('foo:bar:1.0')

##### Result
    parent {
        groupId 'foo'
        artifactId 'bar'
        version: '1.0'
    }

### dependency

#### Example
    dependency('foo:bar')

#####  Result
    dependency {
        groupId 'foo'
        artifactId 'bar'
    }

#### Example
    dependency('foo:bar:1.0')

#####  Result
    dependency {
        groupId 'foo'
        artifactId 'bar'
        version '1.0'
    }

#### Example
    dependency('foo:bar:1.0:test')

#####  Result
    dependency {
        groupId 'foo'
        artifactId 'bar'
        version '1.0'
        scope 'test'
    }

### exclusion

#### Example
    exclusion('foo:bar')

##### Result
    exclusion {
        groupId 'foo'
        artifactId 'bar'
    }

### exclusions

#### Example
    exclusions('foo:bar', 'a:b')

##### Result
    exclusions {
        exclusion {
            groupId 'foo'
            artifactId 'bar'
        }
        exclusion {
            groupId 'a'
            artifactId 'b'
        }
    }

### goals

#### Example
    goals('compile', 'execute')

##### Result
    goals {
        goal 'compile'
        goal 'execute'
    }

### modules

#### Example
    modules('a', 'b')

##### Result
    modules {
        goal 'a'
        goal 'b'
    }

### includes

#### Example
    includes('*.bat', '*.sh')

##### Result
    includes {
        include '*.bat'
        include '*.sh'
    }

### excludes

#### Example
    excludes('*.bat', '*.sh')

##### Result
    excludes {
        exclude '*.bat'
        exclude '*.sh'
    }

Macros
------

Macros help define commonly used POM elements with a terse syntax.

All macros are prefixed with '$' to avoid clashes with POM element names.

Macros must be used in the correct context, or they will produce the given elements in the wrong portion of the POM.

Examples list all supported syntax and result shows what each would produce in the POM.

### $artifact

#### Example
    $artifact('foo:bar')
    $artifact('foo', 'bar')

##### Result
    groupId 'foo'
    artifactId 'bar'
    
#### Example
    $artifact('foo:bar:1.0')
    $artifact('foo', 'bar, '1.0')

##### Result
    groupId 'foo'
    artifactId 'bar'
    version '1.0'

#### Example
    $artifact('foo:bar:bin:1.0')
    $artifact('foo', 'bar', 'bin, '1.0')

##### Result
    groupId 'foo'
    artifactId 'bar'
    version '1.0'
    classifier 'bin'

#### Example
    $artifact('foo:bar:jar:bin:1.0')
    $artifact('foo', 'bar', 'jar', 'bin', '1.0')

##### Result
    groupId 'foo'
    artifactId 'bar'
    version '1.0'
    type 'jar'
    classifier 'bin'

### $configuration

#### Example
    $configuration(foo: '1', bar: '2')

##### Result
    configuration {
        foo '1'
        bar '2'
    }

