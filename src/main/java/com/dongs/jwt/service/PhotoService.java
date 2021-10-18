package com.dongs.jwt.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dongs.jwt.domain.product.Banner;
import com.dongs.jwt.domain.product.Photo;
import com.dongs.jwt.dto.PhotoDto;
import com.dongs.jwt.repository.PhotoRepository;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class PhotoService {
	
	private final PhotoRepository photoRepository;


    @Transactional
    public Photo saveFile(PhotoDto photoDto) {
        return photoRepository.saveAndFlush(photoDto.toEntity());        
    }

    @Transactional
    public PhotoDto getFile(Long id) {
    	Photo photo = photoRepository.findById(id).get();

    	PhotoDto photoDto = PhotoDto.builder()
                .id(id)
                .origFilename(photo.getOrigFilename())
                .filename(photo.getFilename())
                .imageUrl(photo.getImageUrl())
                .build();
        return photoDto;
    }
}