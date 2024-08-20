package org.example.exception

class UncommitedChangesException extends Exception{
    UncommitedChangesException(String message) {
        super(message)
    }
}
