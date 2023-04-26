package com.coderdojo.zen.badge.dto;

import com.coderdojo.zen.badge.model.Category;

public class BadgeDTO {

    private final String name;
    private final String description;
    private final Category category;

    public BadgeDTO(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

}
