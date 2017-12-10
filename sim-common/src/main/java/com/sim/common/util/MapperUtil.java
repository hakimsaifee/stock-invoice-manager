package com.sim.common.util;

import org.modelmapper.ModelMapper;

public class MapperUtil {

	public static <E> E map(Object source, Class<E> clz) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(source, clz);
	}
}
