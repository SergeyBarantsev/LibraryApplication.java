package com.sber.library.library.project.dto;

import com.sber.library.library.project.model.Publishing;
import com.sber.library.library.project.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public class UserPublishDTO
        extends UserDTO {
    private Set<PublishingDTO> publishDTOSet;

    public UserPublishDTO(User user) {
        super(user);
        Set<Publishing> publishes = user.getPublish();
        Set<PublishingDTO> publishDTOS = new HashSet<>();
        for (Publishing publish : publishes) {
            PublishingDTO publishDTO = new PublishingDTO(publish);
            publishDTOS.add(publishDTO);
        }
        this.publishDTOSet = publishDTOS;
    }
}
