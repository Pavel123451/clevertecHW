package org.example.exception

class UncommittedChangesException extends Exception {
    UncommittedChangesException(String message) {
        super(message)
    }
}
