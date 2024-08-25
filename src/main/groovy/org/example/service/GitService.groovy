package org.example.service

import org.example.exception.GitExecutionException

class GitService {

    static String executeCommand(String command) {
        def exec = command.execute()
        exec.waitFor()
        def execOutput = exec.text.trim()
        if (exec.exitValue() != 0 && !execOutput.isEmpty()) {
            throw new GitExecutionException("Failed to execute command: $command")
        }
        return execOutput
    }

    static String getCurrentBranch() {
        return executeCommand("git branch --show-current")
    }

    static String getGitVersion() {
        return executeCommand("git --version")
    }

    static List<String> getRemoteRepositories() {
        def remotes = executeCommand("git remote")
        return remotes ? remotes.split("\n") : []
    }

    static boolean hasUncommittedChanges() {
        def status = executeCommand("git status --porcelain --untracked-files=no")
        return !status.isEmpty()
    }
}