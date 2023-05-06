package com.sparta.myblog.dto;

import com.sparta.myblog.entity.Image;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ImageDto {

    private String originImageName;
    private String imageName;
    private String imagePath;

    public Image toEntity() {
        return Image.builder()
                .originImageName(originImageName)
                .imageName(imageName)
                .imagePath(imagePath)
                .build();
    }

    @Builder
    public ImageDto (String originImageName, String imageName,String imagePath) {
        this.originImageName = originImageName;
        this.imageName = imageName;
        this.imagePath = imagePath;
    }

}