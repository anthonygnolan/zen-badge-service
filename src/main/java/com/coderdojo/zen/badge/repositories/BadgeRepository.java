package com.coderdojo.zen.badge.repositories;

import com.coderdojo.zen.badge.model.Badge;
import com.coderdojo.zen.badge.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    List<Badge> findByCategory(Category category);
}