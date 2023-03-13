package org.tongace.projects.jasperreport.compiler.plugins

import groovy.test.GroovyTestCase
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.tongace.projects.jasperreport.compiler.tasks.JasperReportsCompile
import org.tongace.projects.jasperreport.compiler.tasks.JasperReportsPreCompile

class JasperReportsPluginTest extends GroovyTestCase{
    void testPluginAddsJasperReportsPreCompileTask() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'

        assert project.tasks.prepareReportsCompilation instanceof JasperReportsPreCompile
    }

    void testPluginAddsJasperReportsCompileTask() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'

        assert project.tasks.compileAllReports instanceof JasperReportsCompile
    }

    void testCompileAllReportsDependsOnPrepareReportsCompilation() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'

        assert project.tasks.compileAllReports.dependsOn(project.tasks.prepareReportsCompilation)
    }

    void testPluginAddsJasperReportsExtension() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'

        assert project.jasperreports instanceof JasperReportsExtension
    }

    void testPluginHasDefaultValues() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'

        def jasperreports = project.jasperreports as JasperReportsExtension
        assert jasperreports.classpath == []
        assert jasperreports.srcDir == new File('src/main/jasperreports')
        assert jasperreports.tmpDir == new File("${project.buildDir}/jasperreports")
        assert jasperreports.outDir == new File("${project.buildDir}/classes/main")
        assert jasperreports.srcExt == '.jrxml'
        assert jasperreports.outExt == '.jasper'
        assert jasperreports.compiler == 'net.sf.jasperreports.engine.design.JRJdtCompiler'
        assert !jasperreports.keepJava
        assert jasperreports.validateXml
        assert !jasperreports.verbose
        assert !jasperreports.useRelativeOutDir
    }

    void testPluginSpreadsDirOptions() {
        File src = new File('src/jasperreports')
        File tmp = new File('tmp/jasperreports')
        File out = new File('out/jasperreports')
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'
        project.jasperreports {
            srcDir = src
            tmpDir = tmp
            outDir = out
        }
        project.evaluate()

        assert src == project.jasperreports.srcDir
        assert src == project.tasks.prepareReportsCompilation.srcDir
        assert src == project.tasks.compileAllReports.srcDir

        assert tmp == project.jasperreports.tmpDir
        assert tmp == project.tasks.prepareReportsCompilation.tmpDir

        assert out == project.jasperreports.outDir
        assert out == project.tasks.prepareReportsCompilation.outDir
        assert out == project.tasks.compileAllReports.outDir
    }

    void testPluginSpreadsExtOptions() {
        String src = '.xml'
        String out = '.class'
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'
        project.jasperreports {
            srcExt = src
            outExt = out
        }
        project.evaluate()

        assert src == project.jasperreports.srcExt
        assert src == project.tasks.prepareReportsCompilation.srcExt
        assert src == project.tasks.compileAllReports.srcExt

        assert out == project.jasperreports.outExt
        assert out == project.tasks.prepareReportsCompilation.outExt
        assert out == project.tasks.compileAllReports.outExt
    }

    void testPluginSpreadsClasspathOption() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'groovy'
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'
        project.jasperreports {
            classpath = project.sourceSets.main.output
        }
        project.evaluate()

        assert project.sourceSets.main.output == project.jasperreports.classpath
        assert project.sourceSets.main.output == project.tasks.compileAllReports.classpath
    }

    void testPluginSpreadsCompilerOption() {
        String groovyCompiler = 'net.sf.jasperreports.compilers.JRGroovyCompiler'
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'
        project.jasperreports {
            compiler = groovyCompiler
        }
        project.evaluate()

        assert groovyCompiler == project.jasperreports.compiler
        assert groovyCompiler == project.tasks.prepareReportsCompilation.compiler
    }

    void testPluginSpreadsKeepJavaOption() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'
        project.jasperreports {
            keepJava = true
        }
        project.evaluate()

        assert project.jasperreports.keepJava
        assert project.tasks.prepareReportsCompilation.keepJava
    }

    void testPluginSpreadsValidateXmlOption() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'
        project.jasperreports {
            validateXml = false
        }
        project.evaluate()

        assert !project.jasperreports.validateXml
        assert !project.tasks.prepareReportsCompilation.validateXml
    }

    void testPluginSpreadsVerboseOption() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'
        project.jasperreports {
            verbose = true
        }
        project.evaluate()

        assert project.jasperreports.verbose
        assert project.tasks.prepareReportsCompilation.verbose
        assert project.tasks.compileAllReports.verbose
    }

    void testPluginSpreadsUseRelativeOutDirOption() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'org.tongace.projects.jasperreport.compiler'
        project.jasperreports {
            useRelativeOutDir = true
        }
        project.evaluate()

        assert project.jasperreports.useRelativeOutDir
        assert project.tasks.prepareReportsCompilation.useRelativeOutDir
        assert project.tasks.compileAllReports.useRelativeOutDir
    }
}
