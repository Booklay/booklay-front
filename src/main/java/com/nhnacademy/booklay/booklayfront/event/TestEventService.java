package com.nhnacademy.booklay.booklayfront.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestEventService implements EventService{

    @Override
    public void doRegister(Long memberNo) {
        log.info("TestEventService.doRegister");
    }

    @Override
    public void doLogin(Long memberNo) {
        log.info("TestEventService.doLogin");
    }

}
