package com.coderdojo.zen.badge.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BADGES")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY")
    private Category category;

    public Badge() {}

    public Badge(Long id, String name, String description, Category category) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
