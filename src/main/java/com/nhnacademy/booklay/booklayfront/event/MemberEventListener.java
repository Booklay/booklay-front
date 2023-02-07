package com.nhnacademy.booklay.booklayfront.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberEventListener implements ApplicationListener<MemberEvent> {

    private final EventService eventService;

    @Override
    public void onApplicationEvent(MemberEvent event) {
        if (MemberEvent.Type.CREATE == event.getType()) {
            log.info("Member created: {}", event.getMemberNo());
            eventService.doRegister(event.getMemberNo());
        }

        if (MemberEvent.Type.LOGIN == event.getType()){
            log.info("Member logged in: {}", event.getMemberNo());
            eventService.doLogin(event.getMemberNo());
        }
    }
}
