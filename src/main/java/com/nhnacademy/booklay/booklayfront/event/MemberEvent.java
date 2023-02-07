package com.nhnacademy.booklay.booklayfront.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberEvent extends ApplicationEvent {

    private final Long memberNo;
    private final Type type;

    public MemberEvent(Object source, Long memberNo, Type type) {
        super(source);
        this.memberNo = memberNo;
        this.type = type;
    }

    public static MemberEvent created(Object source, Long memberNo) {
        return new MemberEvent(source, memberNo, Type.CREATE);
    }

    public static MemberEvent loggedIn(Object source, Long memberNo) {
        return new MemberEvent(source, memberNo, Type.LOGIN);
    }

    public enum Type {
        CREATE,
        UPDATE,
        DELETE,
        LOGIN
    }

}
