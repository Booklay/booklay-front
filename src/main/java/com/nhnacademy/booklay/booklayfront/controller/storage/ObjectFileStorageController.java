package com.nhnacademy.booklay.booklayfront.controller.storage;

import com.nhnacademy.booklay.booklayfront.service.ObjectFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/storage")
public class ObjectFileStorageController {

  private final ObjectFileService objectFileService;

  @GetMapping("/{objectFileId}")
  public String downloadFile(@PathVariable Long objectFileId) {

    String fileAddress = objectFileService.loadImage(objectFileId);

    return "redirect:" + fileAddress;

  }

}
