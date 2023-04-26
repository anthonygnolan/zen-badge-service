package com.coderdojo.zen.badge.controllers;

import com.coderdojo.zen.badge.dto.BadgeDTO;
import com.coderdojo.zen.badge.model.Badge;
import com.coderdojo.zen.badge.exceptions.BadgeModelAssembler;
import com.coderdojo.zen.badge.exceptions.BadgeNotFoundException;
import com.coderdojo.zen.badge.model.Category;
import com.coderdojo.zen.badge.repositories.BadgeRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
/**
 * Returns an Image object that can then be painted on the screen.
 * The url argument must specify an absolute. The name
 * argument is a specifier that is relative to the url argument.
 * <p>
 * This method always returns immediately, whether the
 * image exists. When this applet attempts to draw the image on
 * the screen, the data will be loaded. The graphics primitives
 * that draw the image will incrementally paint on the screen.
 *
 */
@RestController
@RequestMapping("/api/v1")
public class BadgeController {

    private final BadgeRepository badgeRepository;

    /**
     * The public name of a hero that is common knowledge
     */
    private final BadgeModelAssembler badgeAssembler;

    BadgeController(BadgeRepository badgeRepository, BadgeModelAssembler assembler) {

        this.badgeRepository = badgeRepository;
        this.badgeAssembler = assembler;
    }

    /**
     * Hero is the main entity we'll be using to . . .
     * Please see the class for true identity
     * @author Captain America
     * @param category the amount of incoming damage
     * @return EntityModel
     */
    @GetMapping("/badges")
    public CollectionModel<EntityModel<Badge>> getBadges(@RequestParam(required = false, name = "category")Category category) {

        List<EntityModel<Badge>> badges;
        if (category != null) {
            badges = badgeRepository.findByCategory(category).stream() //
                    .map(badgeAssembler::toModel) //
                    .toList();
        } else {
            badges = badgeRepository.findAll().stream() //
                    .map(badgeAssembler::toModel) //
                    .toList();
        }

        return CollectionModel.of(badges, linkTo(methodOn(BadgeController.class).getBadges(null)).withSelfRel());
    }

    @PostMapping("/badges")
    ResponseEntity<EntityModel<Badge>> createBadge(@RequestBody BadgeDTO badgeDTO) {

        EntityModel<Badge> entityModel = badgeAssembler.toModel(badgeRepository.save(
                new Badge(null, badgeDTO.getName(), badgeDTO.getDescription(), badgeDTO.getCategory())
        ));

        return ResponseEntity //
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()) //
                .body(entityModel);
    }
    /**
     * <p>This is a simple description of the method. . .
     * <a href="http://www.supermanisthegreatest.com">Superman!</a>
     * </p>
     * @param id the amount of incoming damage
     * @return the amount of health hero has after attack
     * @see <a href="http://www.link_to_jira/HERO-402">HERO-402</a>
     * @since 1.0
     */
    @GetMapping("/badges/{id}")
    public EntityModel<Badge> getBadge(@PathVariable Long id) {

        Badge badge = badgeRepository.findById(id) //
                .orElseThrow(() -> new BadgeNotFoundException(id));

        return badgeAssembler.toModel(badge);
    }

    @PutMapping("/badges/{id}")
    ResponseEntity<Object> updateBadge(@PathVariable Long id, @RequestBody BadgeDTO badgeDTO) {

        Badge badge = badgeRepository.findById(id)
                .orElseThrow(() -> new BadgeNotFoundException(id));

        badge.setName(badgeDTO.getName());
        badge.setDescription(badgeDTO.getDescription());
        badge.setCategory(badgeDTO.getCategory());

        return ResponseEntity.ok(badgeAssembler.toModel(badgeRepository.save(badge)));
    }

    @DeleteMapping("/badges/{id}")
    ResponseEntity<Void> deleteBadge(@PathVariable Long id) {

        badgeRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}