package com.example.mapper;

import com.example.vo.DaeguVO;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface DaeguMapper {
    public void insertDaeguinfo(DaeguVO vo);
}
