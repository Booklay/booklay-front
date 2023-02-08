package com.nhnacademy.booklay.booklayfront.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class MemberEvent extends ApplicationEvent {

    private final String username;
    private final Type type;

    public MemberEvent(Object source, String username, Type type) {
        super(source);
        this.username = username;
        this.type = type;
    }

    public static MemberEvent created(Object source, String username) {
        return new MemberEvent(source, username, Type.CREATE);
    }

    public static MemberEvent loggedIn(Object source, String username) {
        return new MemberEvent(source, username, Type.LOGIN);
    }

    public enum Type {
        CREATE,
        UPDATE,
        DELETE,
        LOGIN
    }

}
