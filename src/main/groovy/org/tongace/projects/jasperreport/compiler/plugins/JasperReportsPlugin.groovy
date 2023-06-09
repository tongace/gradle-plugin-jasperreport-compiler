package org.tongace.projects.jasperreport.compiler.plugins


import org.tongace.projects.jasperreport.compiler.tasks.JasperReportsPreCompile
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.tongace.projects.jasperreport.compiler.tasks.JasperReportsCompile

class JasperReportsPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.create("jasperreports", JasperReportsExtension, project)
        def extension = project.jasperreports as JasperReportsExtension

        def preCompileTask = project.task(
                'prepareReportsCompilation',
                description: 'Configure JasperReports compiler and environment.',
                type: JasperReportsPreCompile
        ) as JasperReportsPreCompile

        def compileTask = project.task(
                'compileAllReports',
                description: 'Compile JasperReports design source files.',
                group: 'jasperReports',
                dependsOn: 'prepareReportsCompilation',
                type: JasperReportsCompile
        ) as JasperReportsCompile

        project.afterEvaluate {
            // Precompile task
            preCompileTask.srcDir = extension.srcDir
            preCompileTask.tmpDir = extension.tmpDir
            preCompileTask.outDir = extension.outDir
            preCompileTask.srcExt = extension.srcExt
            preCompileTask.outExt = extension.outExt
            preCompileTask.compiler = extension.compiler
            preCompileTask.keepJava = extension.keepJava
            preCompileTask.validateXml = extension.validateXml
            preCompileTask.verbose = extension.verbose
            preCompileTask.useRelativeOutDir = extension.useRelativeOutDir
            // Compile task
            compileTask.classpath = project.jasperreports.classpath
            compileTask.srcDir = project.jasperreports.srcDir
            compileTask.outDir = project.jasperreports.outDir
            compileTask.srcExt = project.jasperreports.srcExt
            compileTask.outExt = project.jasperreports.outExt
            compileTask.verbose = project.jasperreports.verbose
            compileTask.useRelativeOutDir = project.jasperreports.useRelativeOutDir
        }
    }

}