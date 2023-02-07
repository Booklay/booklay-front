package com.nhnacademy.booklay.booklayfront.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishMemberCreated(Long memberNo) {
        MemberEvent memberEvent = MemberEvent.created(this, memberNo);
        log.info("Member created: {}", memberEvent.getMemberNo());
        applicationEventPublisher.publishEvent(memberEvent);
    }

    public void publishMemberLoggedIn(Long memberNo) {
        MemberEvent memberEvent = MemberEvent.loggedIn(this, memberNo);
        log.info("Member logged in: {}", memberEvent.getMemberNo());
        applicationEventPublisher.publishEvent(memberEvent);
    }

}
