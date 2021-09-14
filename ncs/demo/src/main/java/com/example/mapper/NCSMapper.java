package com.example.mapper;

import com.example.vo.NCSInfoVO;

import org.apache.ibatis.annotations.Mapper;

@Mapper
//Mapper가 인터페이스로 만들어지는 이유는,
//mybatis framework에서 mapper xml을 참고하여, java코드를 자동으로 생성해주기 때문에 
//java 코드 쪽은 상세 구현 내용이 필요하지 않다.
public interface NCSMapper {
    //public void insertNCSInfo(//String nc_code, String nc_name, Integer nc_level, String nc_def, String nc_l_class,String nc_m_class, String nc_s_class, String nc_d_class);
    public void insertNCSInfo(NCSInfoVO vo);
}
