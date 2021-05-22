package com.covid.minus.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name="REQUESTS")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Data
public class Event {

	@Id 
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long requestId;
	@Column(length = 501)
	private String message;
	private String categoryName;
	@Column(length = 121)
	private String subCategoryName;
	private Long userId;
	private Boolean isOpen;
	//@Column(name = "location", columnDefinition = "POINT")
	//private Point location;
	private BigDecimal lat;
	private BigDecimal lon;
	@Transient
	private Double x;
	@Transient
	private Double y;
	@Transient
	private int i;
	@Transient
	private int o;
	private Long timeCreated;
	
	
	
}
