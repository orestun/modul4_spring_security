package com.epam.esm.controller;

import com.epam.esm.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("tag")
public class TagController {

    @Autowired
    private final TagService tagService;
}
