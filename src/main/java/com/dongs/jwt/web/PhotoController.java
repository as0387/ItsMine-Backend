package com.dongs.jwt.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.dongs.jwt.repository.PhotoRepository;
import com.dongs.jwt.service.PhotoService;

@RestController
public class PhotoController {

	@Autowired
	PhotoRepository photoRepository;
	
}
