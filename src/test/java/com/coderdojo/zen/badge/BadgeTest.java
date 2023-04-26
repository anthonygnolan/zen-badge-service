package com.coderdojo.zen.badge;

import com.coderdojo.zen.badge.model.Badge;
import com.coderdojo.zen.badge.model.Category;
import com.coderdojo.zen.badge.exceptions.BadgeNotFoundException;
import com.coderdojo.zen.badge.repositories.BadgeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;
import java.util.stream.StreamSupport;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BadgeTests {

    @Autowired
    private BadgeRepository badgeRepository;

    // JUnit test for saveBadge
    @Test
    @Order(1)
    @Rollback(value = false)
    void saveBadgeTest(){

        Badge badge = new Badge(1L, "Scratch", "MacBook Pro", Category.PROGRAMMING);

        badgeRepository.save(badge);

        Assertions.assertThat(badge.getId()).isPositive();
    }

    @Test
    @Order(2)
    void getBadgeTest(){

        Badge badge = badgeRepository.findById(1L).orElseThrow(() -> new BadgeNotFoundException(1L));

        Assertions.assertThat(badge.getId()).isEqualTo(1L);

    }

    @Test
    @Order(3)
    void getListOfBadgesTest(){

        Iterable<Badge> badges = badgeRepository.findAll();

        Assertions.assertThat(StreamSupport.stream(badges.spliterator(), false).count()).isPositive();

    }

    @Test
    @Order(4)
    @Rollback(value = false)
    void updateBadgeTest(){

        Badge badge = badgeRepository.findById(1L).orElseThrow(() -> new BadgeNotFoundException(1L));

        badge.setDescription("New Description");
        badge.setCategory(Category.PROGRAMMING);

        Badge badgeUpdated =  badgeRepository.save(badge);

        Assertions.assertThat(badgeUpdated.getDescription()).isEqualTo("New Description");
        Assertions.assertThat(badgeUpdated.getCategory()).isEqualTo(Category.PROGRAMMING);
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    void deleteBadgeTest(){

        Badge badge = badgeRepository.findById(1L).orElseThrow(() -> new BadgeNotFoundException(1L));

        badgeRepository.delete(badge);

        //badgeRepository.deleteById(1L);

        Badge badge1 = null;

        Optional<Badge> optionalBadge = badgeRepository.findById(1L);

        if(optionalBadge.isPresent()){
            badge1 = optionalBadge.get();
        }

        Assertions.assertThat(badge1).isNull();
    }
}


