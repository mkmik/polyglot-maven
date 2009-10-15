Description
-----------

Graven; Provides Polgyglot Maven the ability to understand Groovy POM files.

Macros
------

Macros help define commonly used POM elements with a terse syntax.

All macros are prefixed with '$' to avoid clashes with POM element names.

Macros must be used in the correct context, or they will produce the given elements in the wrong portion of the POM.

### $artifact

#### Example
    $artifact('foo:bar')

##### Result
    <groupId>foo</groupId>
    <artifactId>bar</artifactId>

#### Example
    $artifact('foo:bar:1.0')

##### Result
    <groupId>foo</groupId>
    <artifactId>bar</artifactId>
    <version>1.0</version>

#### Example
    $artifact('foo:bar:bin:1.0')

##### Result
    <groupId>foo</groupId>
    <artifactId>bar</artifactId>
    <version>1.0</version>
    <classifier>bin</classifier>
            
#### Example
    $artifact('foo:bar:jar:bin:1.0')

##### Result
    <groupId>foo</groupId>
    <artifactId>bar</artifactId>
    <version>1.0</version>
    <type>jar</type>
    <classifier>bin</classifier>

### $parent

#### Example
    $parent('foo:bar:1.0')

##### Result
    <parent>
        <groupId>foo</groupId>
        <artifactId>bar</artifactId>
        <version>1.0</version>
    </parent>

### $dependency

#### Example
    $dependency('foo:bar:1.0:test')

##### Result
    <dependency>
        <groupId>foo</groupId>
        <artifactId>bar</artifactId>
        <version>1.0</version>
        <scope>test</scope>
    </dependency>

#### Example
    $dependency('foo:bar:1.0')

##### Result
    <dependency>
        <groupId>foo</groupId>
        <artifactId>bar</artifactId>
        <version>1.0</version>
    </dependency>

#### Example
    $dependency('foo:bar')

##### Result
    <dependency>
        <groupId>foo</groupId>
        <artifactId>bar</artifactId>
    </dependency>

### $exclusion

#### Example
    $exclusion('foo:bar')

##### Result
    <exclusion>
        <groupId>foo</groupId>
        <artifactId>bar</artifactId>
    </exclusion>

### $exclusions

#### Example
    $exclusions('foo:bar', 'a:b')

##### Result
    <exclusions>
        <exclusion>
            <groupId>foo</groupId>
            <artifactId>bar</artifactId>
        </exclusion>
        <exclusion>
            <groupId>a</groupId>
            <artifactId>b</artifactId>
        </exclusion>
    </exclusions>

### $goals

#### Example
    $goals('compile', 'execute')

##### Result
    <goals>
        <goal>compile</goal>
        <goal>execute</goal>
    <goals>

### $modules

#### Example
    $modules('a', 'b')

##### Result
    <modules>
        <goal>a</goal>
        <goal>b</goal>
    <modules>

### $configuration

#### Example
    $configuration(foo: '1', bar: '2')

##### Result
    <configuration>
        <foo>1</foo>
        <bar>2</bar>
    </configuration>

### $includes

#### Example
    $includes('*.bat', '*.sh')

##### Result
    <includes>
        <include>*.bat</include>
        <include>*.sh</include>
    </includes>

### $excludes

#### Example
    $excludes('*.bat', '*.sh')

##### Result
    <excludes>
        <exclude>*.bat</exclude>
        <exclude>*.sh</exclude>
    </excludes>

