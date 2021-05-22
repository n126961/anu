package com.covid.minus.repo;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.covid.minus.entity.Event;

@Repository
public interface EventRepo extends PagingAndSortingRepository<Event,Long> {

	Event findByRequestId(long eventID);
	
	@Query("SELECT e FROM REQUESTS e WHERE e.lat BETWEEN :xMin AND :xMax AND e.lon BETWEEN :yMin AND :yMax AND e.isOpen = true ORDER BY e.requestId DESC")
	List<Event> findByRadius(@Param("xMin") BigDecimal xMin, @Param("xMax") BigDecimal xMax, @Param("yMin") BigDecimal yMin, @Param("yMax") BigDecimal yMax, Pageable pageable);
	
	List<Event> findAll();
	
	@Query("SELECT e FROM REQUESTS e WHERE e.userId = :userId order by  e.isOpen DESC, e.requestId DESC")
	List<Event> findByUser(@Param("userId") long userId);
	
	//void add(Event event);
}
