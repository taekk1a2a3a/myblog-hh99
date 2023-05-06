package com.sparta.myblog.controller;

import com.sparta.myblog.dto.ImageDto;
import com.sparta.myblog.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image")
    public String imageUploadV1(@RequestParam(name = "image") MultipartFile image) throws IOException {

        // 폴더 생성과 파일명 새로 부여를 위한 현재 시간 알아내기
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int millis = now.get(ChronoField.MILLI_OF_SECOND);

        String absolutePath = new File("/Users/murphy/Desktop").getAbsolutePath() + "/"; // 파일이 저장될 절대 경로
        String newFileName = "image" + hour + minute + second + millis; // 새로 부여한 이미지명
        String fileExtension = '.' + image.getOriginalFilename().replaceAll("^.*\\.(.*)$", "$1"); // 정규식 이용하여 확장자만 추출
        String path = "images/test/" + year + "/" + month + "/" + day; // 저장될 폴더 경로

        try {
            if(!image.isEmpty()) {
                File file = new File(absolutePath + path);
                if(!file.exists()){
                    file.mkdirs(); // mkdir()과 다르게 상위 폴더가 없을 때 상위폴더까지 생성
                }

                file = new File(absolutePath + path + "/" + newFileName + fileExtension);
                image.transferTo(file);

                ImageDto imgDto = ImageDto.builder()
                        .originImageName(image.getOriginalFilename())
                        .imagePath(path)
                        .imageName(newFileName + fileExtension)
                        .build();

                imageService.saveImage(imgDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "test/imageV1";
    }
}
