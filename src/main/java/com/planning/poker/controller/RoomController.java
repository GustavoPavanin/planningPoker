package com.planning.poker.controller;

import com.planning.poker.model.dto.RoomDto;
import com.planning.poker.services.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomController {
	@Autowired
	private ContentService contentService;

	@GetMapping("/hall")
	public List<RoomDto> getRoomForHall(){
		return contentService.getAllRooms();
	}

}
