package com.nhnacademy.booklay.booklayfront.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@RequiredArgsConstructor
public class MemberEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishMemberCreated(String username) {
        MemberEvent memberEvent = MemberEvent.created(this, username);
        log.info("Member created: {}", memberEvent.getUsername());
        applicationEventPublisher.publishEvent(memberEvent);
    }

    public void publishMemberLoggedIn(String username) {
        MemberEvent memberEvent = MemberEvent.loggedIn(this, username);
        log.info("Member logged in: {}", memberEvent.getUsername());
        applicationEventPublisher.publishEvent(memberEvent);
    }

}
