package com.nhnacademy.booklay.booklayfront.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestEventService implements EventService{

    @Override
    public void doRegister(String username) {
        log.info("TestEventService.doRegister");
    }

    @Override
    public void doLogin(String username) {
        log.info("TestEventService.doLogin");
        log.info("=================");
        log.info("이거 커스터마이징 하면 됩니다~~");
    }

}
