package com.dongs.jwt.dto;

import com.dongs.jwt.domain.product.Photo;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class PhotoDto {
    private Long id;
    private String origFilename;
    private String filename;
    private String imageUrl;

    public Photo toEntity() {
    	Photo build = Photo.builder()
                .id(id)
                .origFilename(origFilename)
                .filename(filename)
                .imageUrl(imageUrl)
                .build();
        return build;
    }

    @Builder
    public PhotoDto(Long id, String origFilename, String filename, String imageUrl) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.imageUrl = imageUrl;
    }
}
