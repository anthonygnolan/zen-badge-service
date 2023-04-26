package com.coderdojo.zen.badge.exceptions;

import com.coderdojo.zen.badge.controllers.BadgeController;
import com.coderdojo.zen.badge.model.Badge;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BadgeModelAssembler implements RepresentationModelAssembler<Badge, EntityModel<Badge>> {

    @Override
    public EntityModel<Badge> toModel(Badge badge) {

        // Unconditional links to single-item resource and aggregate root

        return EntityModel.of(badge,
                linkTo(methodOn(BadgeController.class).getBadge(badge.getId())).withSelfRel(),
                linkTo(methodOn(BadgeController.class).getBadges(null)).withRel("badges"));
    }
}