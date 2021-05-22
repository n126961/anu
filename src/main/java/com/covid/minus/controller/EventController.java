package com.covid.minus.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.covid.minus.entity.Event;
import com.covid.minus.repo.EventRepo;
import com.covid.minus.util.NumberUtil;

@RestController
public class EventController {
	
	@Autowired
	EventRepo eventsRepo;
	
	@Autowired
	NumberUtil util;
	
	@PostMapping("/request-help")
	@ResponseStatus(HttpStatus.CREATED)
	public long addHelpEvent(Event e) {
		
		Long mob = util.decryptMobileNumber(e.getUserId(), e.getI());
		if(mob == null || mob == 0) {
			return 0;
		}
		e.setUserId(mob);
		BigDecimal bigLat = e.getLat();
		e.setLat(bigLat.setScale(2, RoundingMode.UP));
		
		BigDecimal bigLon = e.getLon();
		e.setLon(bigLon.setScale(2, RoundingMode.UP));
		
		
		e.setTimeCreated(Instant.now().getEpochSecond());
		Event saved =eventsRepo.save(e);
		
		return saved.getRequestId();
		
	}
	
	@PostMapping("/close-request")
	public long closeHelpEvent(Event e) {
		
		/*Event e = new Event();
		e.setMessage(message);
		e.setLocation(GeoUtil.getPoint(x,y));
		e.setCategoryName(categoryName);
		e.setSubCategoryName(subCategoryName);
		e.setIsOpen(true);
		e.setUserId(userId);
		*/
		//se.setLocation(GeoUtil.getPoint(e.getX(), e.getY()));
		//if(e.getMessage() != null) {
		
		Long mob = util.decryptMobileNumber(e.getUserId(), e.getI());
		if(mob == null && mob == 0) {
			Optional<Event> eOpt = eventsRepo.findById(e.getRequestId());
			Event stage = eOpt.get();
			stage.setIsOpen(false);
			if(stage.getUserId() == mob) {
				Event saved =eventsRepo.save(stage);
				return saved.getRequestId();
			}
		}
		
		return 0;
		
	}
	
	
	@GetMapping("/all")
	public List<Event> findAll(){
		return eventsRepo.findAll();
	}
	
	public Geometry createCircle(double x, double y, double radius) {
	    GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
	    shapeFactory.setNumPoints(32);
	    shapeFactory.setCentre(new Coordinate(x, y));
	    shapeFactory.setSize((radius * 2)/88.1);
	    return shapeFactory.createCircle();
	}
	
	@GetMapping("/near-me")
	public List<Event> findAllNearBy(@RequestParam(name="x") BigDecimal x, @RequestParam(name="y") BigDecimal y, @RequestParam(name="page") Integer page) throws ParseException{
		x.setScale(2, RoundingMode.UP);
		y.setScale(2, RoundingMode.UP);
		BigDecimal xMin = x.subtract(new BigDecimal(0.02));
		BigDecimal xMax = x.add(new BigDecimal(0.02));
		BigDecimal yMin = x.subtract(new BigDecimal(0.02));
		BigDecimal yMax = x.add(new BigDecimal(0.02));
		// 4 is good for 5 km
		//Geometry g =createCircle(x, y, 10);
		Pageable pageable = PageRequest.of((page - 1), 3);
		return eventsRepo.findByRadius(xMin, xMax,yMin,yMax,pageable);
	}
	
	@GetMapping("/req-details")
	public Event findByRequestId(@RequestParam(name="requestId") Long x) {
		
		return eventsRepo.findByRequestId(x);
	}
	
	@GetMapping("/user-requests")
	public List<Event> findByUserId(@RequestParam(name="c") Long c, @RequestParam(name="i") int i, @RequestParam(name="o") int o) {
		Long mob = util.decryptMobileNumber(c, i);
		return eventsRepo.findByUser(mob);
	}
	
	
		

}
