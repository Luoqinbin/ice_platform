package com.ice.mapper;


import com.github.abel533.mapper.Mapper;
import com.github.abel533.mapper.MySqlMapper;

public interface BaseMapper<T> extends Mapper<T>, MySqlMapper<T> {

}
