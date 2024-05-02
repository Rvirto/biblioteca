package com.biblioteca.domain.enumeration;

import java.util.Arrays;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public enum BookStatusEnum {

    AVAILABLE(TRUE),
    UNAVAILABLE(FALSE);

    private final Boolean enable;

    BookStatusEnum(Boolean enable) {
        this.enable = enable;
    }

    public Boolean getEnable() {
        return enable;
    }

    public static BookStatusEnum getStatus(Boolean status) {
        return Arrays.stream(BookStatusEnum.values())
                .filter(bookStatusEnum -> bookStatusEnum.getEnable().equals(status))
                .findFirst().get();
    }
}
