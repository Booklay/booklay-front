package com.nhnacademy.booklay.booklayfront.dto.storage.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ObjectFileResponse {
    private Long id;

    private String fileAddress;

    private String fileName;
}
