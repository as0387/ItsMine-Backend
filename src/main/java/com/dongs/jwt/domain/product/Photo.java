package com.dongs.jwt.domain.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String origFilename;  // 파일 원본명
    
    @Column(nullable = false)
    private String filename; //중복을 피하기위해 변경한 파일명

    @Column(nullable = false)
    private String imageUrl;  // 파일 저장 경로

    @JoinColumn(name = "postId")
    @ManyToOne
    private Post post;


    @Builder
    public Photo(Long id, String origFilename, String filename, String imageUrl) {
        this.id = id;
        this.origFilename = origFilename;
        this.filename = filename;
        this.imageUrl = imageUrl;
    }

}