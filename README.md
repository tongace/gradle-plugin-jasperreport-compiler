# Gradle JasperReports Compiler Plugin
[github source](https://github.com/tongace/gradle-plugin-jasperreport-compiler) 
## Description

Provides the capability to compile JasperReports design files. 
This plugin is designed to work like the Maven plugins [Maven 2 JasperReports Plugin](http://mojo.codehaus.org/jasperreports-maven-plugin) and [JasperReports-plugin](https://github.com/alexnederlof/Jasper-report-maven-plugin). Much of this was inspired by these two projects.
Inspired by non-updated project of [com.github.gmazelier.jasperreports](https://plugins.gradle.org/plugin/com.github.gmazelier.jasperreports)
to support gradle version 8
## Usage

This plugin provides one main task, `compileAllReports`. It uses [InputChanges task](http://www.gradle.org/docs/current/dsl/org.gradle.api.tasks.incremental.IncrementalTaskInputs.html) feature to process out-of-date files and [parallel collections](http://gpars.codehaus.org/GParsPool) from [GPars](http://gpars.codehaus.org) for parallel processing. Adapt your build process to your own needs by defining the proper tasks depedencies (see *Custom Build Process* below).

If your designs compilation needs to run after Groovy compilation, running `compileAllReports` should give a similar output:

    $ gradle compileAllReports
    :compileJava UP-TO-DATE
    :compileGroovy UP-TO-DATE
    :prepareReportsCompilation
    :compileAllReports
    21 designs compiled in 2222 ms

    BUILD SUCCESSFUL

    Total time: 6.577 secs

To clean up and start fresh, simply run:

    $ gradle clean compileAllReports

### Installation

Using the pluging DSL...

    plugins {
      id "org.tongace.projects.jasperreport.compiler" version "1.0.0"
    }

Using the legacy plugin application...

    buildscript {
      repositories {
        maven {
          url "https://plugins.gradle.org/m2/"
        }
      }
      dependencies {
        classpath "org.tongace.projects.jasperreport.compiler:1.0.0"
      }
    }

    apply plugin: "org.tongace.projects.jasperreport.compiler"

### Configuration

Below are the parameters that can be used to configure the build:

| Parameter     | Type             | Description                                                                                   |
|---------------|------------------|-----------------------------------------------------------------------------------------------|
| `srcDir`      | `File`           | Design source files directory. Default value: `src/main/jasperreports`                        |
| `tmpDir`      | `File`           | Temporary files (`.java`) directory. Default value: `${project.buildDir}/jasperreports`       |
| `outDir`      | `File`           | Compiled reports file directory. Default value: `${project.buildDir}/classes/main`            |
| `srcExt`      | `String`         | Design source files extension. Default value: `'.jrxml'`                                      |
| `outExt`      | `String`         | Compiled reports files extension. Default value: `'.jasper'`                                  |
| `compiler`    | `String`         | The report compiler to use. Default value: `net.sf.jasperreports.engine.design.JRJdtCompiler` |
| `keepJava`    | `boolean`        | Keep temporary files after compiling. Default value: `false`                                  |
| `validateXml` | `boolean`        | Validate source files before compiling. Default value: `true`                                 |
| `verbose`     | `boolean`        | Verbose plugin outpout. Default value: `false`                                                |
| `useRelativeOutDir`     | `boolean`        | The outDir is relative to java classpath. Default value: `false`                                                |
| `classpath`   | `Iterable<File>` | Extra elements to add to the classpath when compile. Default value: `[]`                      |

### Example

Below is a complete example, with default values:

    jasperreports {
        srcDir = file('src/main/jasperreports')
        tmpDir = file('${project.buildDir}/jasperreports')
        outDir = file('${project.buildDir}/classes/main')
        srcExt = '.jrxml'
        outExt = '.jasper'
        compiler = 'net.sf.jasperreports.engine.design.JRJdtCompiler'
        keepJava = false
        validateXml = true
        verbose = false
        useRelativeOutDir = false
        classpath = []
    }

### Custom Build Process

Adding a task dependency is very simple. For example, if you want to make sure that Groovy (and Java) compilation is successfully performed before JasperReports designs compilation, just add the following to your build script:

    compileAllReports.dependsOn compileGroovy

### Custom Classpath

#### Sharing dependencies

Here's a way to share dependencies (`joda-time` in this example) between the main project and the designs compilation:

    buildscript {
      ext {
        libs = [
          jrdeps: [
            // all dependencies shared with JasperReports
            'joda-time:joda-time:2.7'
          ]
        ]
      }
      repositories {
        jcenter()
        mavenCentral()
        maven {
            url 'http://jaspersoft.jfrog.io/jaspersoft/third-party-ce-artifacts/'
        }
        maven {
          url 'http://jasperreports.sourceforge.net/maven2'
        }
        maven {
          url 'http://repository.jboss.org/maven2/'
        }
      }
      dependencies {
        classpath 'org.tongace.projects.jasperreport.compiler:1.0.0'
        classpath libs.jrdeps
      }
    }

    apply plugin: 'groovy'
    apply plugin: 'org.tongace.projects.jasperreport.compiler'

    repositories {
        mavenCentral()
    }

    dependencies {
      compile libs.jrdeps
    }

    jasperreports {
      verbose = true
    }

    compileAllReports.dependsOn compileGroovy

#### Adding Project Compiled Sources

Use the `classpath` property to acces your compiled sources in you JasperReports designs. Configure your build script in a similar way:

    jasperreports {
        verbose = true
        classpath = project.sourceSets.main.output
    }

## License
This plugin is licensed under [Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
without warranties or conditions of any kind, either express or implied.
