package org.example.task

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GitInstallCheckTask extends DefaultTask{

    @TaskAction
    void checkGitInstall() {
        def execOutput = new ByteArrayOutputStream()
        project.exec {
            commandLine 'powershell.exe', 'git', '-v'
            standardOutput = execOutput
        }
        println "${execOutput.toString()}"
    }

}
