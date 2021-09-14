package com.example.mapper;

import com.example.vo.TourismVO;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TourismMapper {
    public void insertTourism(TourismVO vo);
}
