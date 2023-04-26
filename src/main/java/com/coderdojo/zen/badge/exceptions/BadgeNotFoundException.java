package com.coderdojo.zen.badge.exceptions;

public class BadgeNotFoundException extends RuntimeException {

    public BadgeNotFoundException(Long id) {
        super("Could not find badge " + id);
    }
}
