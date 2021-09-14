package com.example.coon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import com.example.mapper.NCSMapper;
import com.example.vo.NCSInfoVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class TestScheduler {
    @Autowired NCSMapper mapper;
    //cron="초 분 시 일 월 년"
    //cron="10 * * * * *" - 매 10초 마다 1번씩 실행 (16:31:10 / 16:32:10 / 16:33:10)
    //cron="*/10 * * * * *" - 10초가 지날 때 마다 1번 실행(16:31:10 / 16:31:20 / 16:31:30)
    @Scheduled(cron="10 * * * * *")
    public void cronTest() {
        System.out.println("Scheduled running");
    }
    // @Scheduled(cron="* * 3 * * *")
    // @Scheduled(cron="0 * * * * *")
    // public String getNcsinfo() throws IOException {
    //     StringBuilder urlBuilder = new StringBuilder("https://c.q-net.or.kr/openapi/Ncs1info/ncsinfo.do");//url 입력
    //     urlBuilder.append("?"+URLEncoder.encode("ServiceKey", "UTF-8" )
    //     +"=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D");//(ServiceKey)를 UTF-8로 작성
    //     urlBuilder.append("&"+URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode("json","UTF-8"));//제공형태(json,xml)
    //     urlBuilder.append("&"+URLEncoder.encode("pageNo", "UTF-8")+"="+URLEncoder.encode("1","UTF-8"));//페이지번호 - 기본값 : 
    //     urlBuilder.append("&"+URLEncoder.encode("numOfRows", "UTF-8")+"="+URLEncoder.encode("10","UTF-8"));//페이지당 데이터 수 - 기본값 : 
        
    //     // urlBuilder.append("&"+urlBuilder.encode("ServiceKey", "UTF-8")+"="+urlBuilder.encode
    //     // ("3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf+/rxgsJeUfled1zNS0w==","UTF-8"));

    //     URL url = new URL(urlBuilder.toString()); // 문자열로 쓰여진 URL을 자바 URL 객체로 생성 
    //     HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 연결후 Connection 객체 가져오기
    //     conn.setRequestMethod("GET");//Connection 유형은 GET으로 설정
    //     conn.setRequestProperty("Content-type", "application/json");//결과형태는 json으로 설정 

    //     System.out.println("Response Code : "+conn.getResponseCode());//전송 결과코드 (arc 확인)
    //     System.out.println("Response Message : "+conn.getResponseMessage());//전송 결과 메시지(arc 확인)

    //     BufferedReader rd;
    //     if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300){
    //         rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    //     }
    //     else{
    //         rd =  new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    //     }
    //     StringBuilder sb = new StringBuilder();
    //     String line = null;
    //     // BufferedReader 안에 있는 내용을 끝까지 조회한다.
    //     while((line = rd.readLine()) != null){// 한 줄씩 읽기 (null이라면, 종료) - null:파일의 끝
    //         sb.append(line);// 읽은 1줄은 StringBuilder에 추가
    //     }
    //     rd.close();//BufferedReader 닫기
    //     conn.disconnect();// 연결 종료  
        
    //     // return sb.toString();// 만들어딘 결과를 문자열로 변경하여 내보내기
    //     try{
    //         JSONParser jsonParser = new JSONParser();
    //         JSONObject jsonObj = (JSONObject) jsonParser.parse(sb.toString());
    //         JSONObject rootObj = (JSONObject) jsonObj.get("root");
    //         JSONArray itemsArr = (JSONArray) rootObj.get("items");
    //         for(int i=0; i<itemsArr.size(); i++){
    //             JSONObject item = (JSONObject)itemsArr.get(i);
    //             System.out.println("===============================");
    //             System.out.println("능력단위 분류 번호 : "+item.get("ncsClCd"));
    //             System.out.println("능력단위 명 : "+item.get("compeUnitName"));
    //             System.out.println("능력단위 수준 : "+item.get("compeUnitLevel"));
    //             System.out.println("능력단위 정의 : "+item.get("compeUnitDef"));
    //             System.out.println(
    //                 "분류체계 : "+item.get("ncsLclasCdnm")+" > "
    //                 +item.get("ncsMclasCdnm")+" > "
    //                 +item.get("ncsSclasCdnm")+" > "
    //                 +item.get("ncsSubdCdnm")
    //             );
    //             NCSInfoVO vo = new NCSInfoVO();
    //             vo.setNs_code(item.get("ncsClCd").toString());
    //             vo.setNs_name(item.get("compeUnitName").toString());
    //             vo.setNs_level(Integer.parseInt(item.get("compeUnitLevel").toString()));
    //             vo.setNs_def(item.get("compeUnitDef").toString());
    //             vo.setNs_l_class(item.get("ncsLclasCdnm").toString());
    //             vo.setNs_m_class(item.get("ncsMclasCdnm").toString());
    //             vo.setNs_s_class(item.get("ncsSclasCdnm").toString());
    //             vo.setNs_d_class(item.get("ncsSubdCdnm").toString());

    //             mapper.insertNCSInfo(vo);
    //             // mapper.insertNCSInfo(
    //             //     item.get("ncsClCd").toString(),
    //             //     item.get("compeUnitName").toString(), 
    //             //     Integer.parseInt(item.get("compeUnitLevel").toString()), 
    //             //     item.get("compeUnitDef").toString(), 
    //             //     item.get("ncsLclasCdnm").toString(), 
    //             //     item.get("ncsMclasCdnm").toString(),
    //             //     item.get("ncsSclasCdnm").toString(), 
    //             //     item.get("ncsSubdCdnm").toString()
    //             // );
    //         }
    //     }
    //     catch(ParseException pe) {
    //         pe.printStackTrace();
    //     }
    //     return "";
    // }
}
