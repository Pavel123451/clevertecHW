package org.example.task

import org.example.exception.CurrentBuildTagExistsException
import org.example.extension.GitPluginExtension
import org.example.service.TagService
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class GetCurrentBuildVersionTask extends DefaultTask {

    @TaskAction
    void getCurrentBuildVersion() {
        def extension = project.extensions.findByType(GitPluginExtension)

        if (TagService.currentBuildTagExists()) {
            throw new CurrentBuildTagExistsException("Git tag for the current state of the project already exists.")
        }

        extension.lastTag = TagService.getLastTag()
        println "Last tag: ${extension.lastTag}"

        def newTag = TagService.generateNewTag(extension.currentBranch, extension.lastTag)
        println "New tag: $newTag"

        TagService.createTag(newTag)
        TagService.pushTag(newTag)
    }
}

