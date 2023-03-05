package com.epam.esm.service;

import com.epam.esm.repository.TagRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TagService {
    @Autowired
    private final TagRepository tagRepository;
}
