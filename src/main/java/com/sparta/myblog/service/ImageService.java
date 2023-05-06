package com.sparta.myblog.service;

import com.sparta.myblog.dto.ImageDto;
import com.sparta.myblog.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ImageService {

    private final ImageRepository imageRepository;

    public void saveImage(ImageDto imageDto) {
        imageRepository.save(imageDto.toEntity());
    }
}
