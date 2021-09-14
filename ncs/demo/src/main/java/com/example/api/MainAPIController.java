package com.example.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.example.mapper.NCSMapper;
import com.example.vo.NCSInfoVO;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

@RestController//데이터 전용
public class MainAPIController {
    //mapper는 myBatis Framewok가 Interace와 mapper xml을 참고하여,
    //S
    @Autowired NCSMapper mapper;

    @GetMapping("/ncs_info/json")
    public String getNcsinfo() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("https://c.q-net.or.kr/openapi/Ncs1info/ncsinfo.do");//url 입력
        urlBuilder.append("?"+URLEncoder.encode("ServiceKey", "UTF-8" )
        +"=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D");//(ServiceKey)를 UTF-8로 작성
        urlBuilder.append("&"+URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode("json","UTF-8"));//제공형태(json,xml)
        urlBuilder.append("&"+URLEncoder.encode("pageNo", "UTF-8")+"="+URLEncoder.encode("1","UTF-8"));//페이지번호 - 기본값 : 
        urlBuilder.append("&"+URLEncoder.encode("numOfRows", "UTF-8")+"="+URLEncoder.encode("10","UTF-8"));//페이지당 데이터 수 - 기본값 : 
        
        // urlBuilder.append("&"+urlBuilder.encode("ServiceKey", "UTF-8")+"="+urlBuilder.encode
        // ("3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf+/rxgsJeUfled1zNS0w==","UTF-8"));

        URL url = new URL(urlBuilder.toString()); // 문자열로 쓰여진 URL을 자바 URL 객체로 생성 

        System.out.println(urlBuilder.toString()); //url 주소 확인

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();// 연결후 Connection 객체 가져오기
        conn.setRequestMethod("GET");//Connection 유형은 GET으로 설정
        conn.setRequestProperty("Content-type", "application/json");//결과형태는 json으로 설정 

        System.out.println("Response Code : "+conn.getResponseCode());//전송 결과코드 (arc 확인)
        System.out.println("Response Message : "+conn.getResponseMessage());//전송 결과 메시지(arc 확인)

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300){
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }   
        else{
            rd =  new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line = null;
        // BufferedReader 안에 있는 내용을 끝까지 조회한다.
        while((line = rd.readLine()) != null){// 한 줄씩 읽기 (null이라면, 종료) - null:파일의 끝
            sb.append(line);// 읽은 1줄은 StringBuilder에 추가
        }
        rd.close();//BufferedReader 닫기
        conn.disconnect();// 연결 종료  
        System.out.println(sb.toString());// 결과확인
        
        // return sb.toString();// 만들어진 결과를 문자열로 변경하여 내보내기
        try{
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(sb.toString());
            JSONObject rootObj = (JSONObject) jsonObj.get("root");
            JSONArray itemsArr = (JSONArray) rootObj.get("items");
            //파일 열기 (생성?)
            FileOutputStream fos = new FileOutputStream("/test_text/ncs_info.csv");
            //파일 쓰기 스트림 생성 (파라미터는 FileOutputStream 개체를 넣는다.)
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");//액셀에서 깨지면 "ms949"
            //파이 쓰기 중 끊기는 오류가 발생할 수도 있기 때문에
            BufferedWriter bw = new BufferedWriter(osw);


            for(int i=0; i<itemsArr.size(); i++){
                JSONObject item = (JSONObject)itemsArr.get(i);
                System.out.println("===============================");
                System.out.println("능력단위 분류 번호 : "+item.get("ncsClCd"));
                System.out.println("능력단위 명 : "+item.get("compeUnitName"));
                System.out.println("능력단위 수준 : "+item.get("compeUnitLevel"));
                System.out.println("능력단위 정의 : "+item.get("compeUnitDef"));
                System.out.println(
                    "분류체계 : "+item.get("ncsLclasCdnm")+" > "
                    +item.get("ncsMclasCdnm")+" > "
                    +item.get("ncsSclasCdnm")+" > "
                    +item.get("ncsSubdCdnm")
                );
                NCSInfoVO vo = new NCSInfoVO();
                vo.setNs_code(item.get("ncsClCd").toString());
                vo.setNs_name(item.get("compeUnitName").toString());
                vo.setNs_level(Integer.parseInt(item.get("compeUnitLevel").toString()));
                vo.setNs_def(item.get("compeUnitDef").toString());
                vo.setNs_l_class(item.get("ncsLclasCdnm").toString());
                vo.setNs_m_class(item.get("ncsMclasCdnm").toString());
                vo.setNs_s_class(item.get("ncsSclasCdnm").toString());
                vo.setNs_d_class(item.get("ncsSubdCdnm").toString());
                mapper.insertNCSInfo(vo);//db로 날려

                String ncs_def = vo.getNs_def().replace(",", " ");
                String s = "";

                s += vo.getNs_code()+"|";
                s += vo.getNs_name()+"|";
                s += vo.getNs_level()+"|";
                s += vo.getNs_def()+"|";
                s += vo.getNs_l_class()+"|";
                s += vo.getNs_m_class()+"|";
                s += vo.getNs_s_class()+"|";
                s += vo.getNs_d_class()+"\n";

                bw.write(s);
            }
            bw.close(); 
        }
        catch(ParseException pe) {
            pe.printStackTrace();
        }
        return "";
    }


    @GetMapping("/ncs_info/xml")
    public String getNcsInfoXML() throws IOException{
        StringBuilder urlBuilder = new StringBuilder("https://c.q-net.or.kr/openapi/Ncs1info/ncsinfo.do");//url 입력
        urlBuilder.append("?"+URLEncoder.encode("ServiceKey", "UTF-8" )
        +"=3CID6KRU4kjF4jvHanoFBLwycg6Htt86aVfgEOgBmAecshZIcO5EC9UM9FhVGwAX2Zf%2B%2FrxgsJeUfled1zNS0w%3D%3D");//(ServiceKey)를 UTF-8로 작성
        urlBuilder.append("&"+URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode("xml","UTF-8"));//제공형태(json,xml) 타입
        urlBuilder.append("&"+URLEncoder.encode("pageNo", "UTF-8")+"="+URLEncoder.encode("1","UTF-8"));//페이지번호 - 기본값 : 
        urlBuilder.append("&"+URLEncoder.encode("numOfRows", "UTF-8")+"="+URLEncoder.encode("10","UTF-8"));//페이지당 데이터 수 - 기본값 : 

        try{
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(urlBuilder.toString());// 공공데이터 요청 url
        // System.out.println(urlBuilder.toString());
        doc.getDocumentElement().normalize();//java에서 파싱가능 하도록 format전환
        System.out.println(doc.getDocumentElement().getNodeName());//전체문서 갔고오기
        NodeList nList = doc.getElementsByTagName("ncsInfo");//1덩어리 = 노드
        System.out.println(nList.getLength());// 요청노드 개수
        for(int i=0; i<nList.getLength(); i++) {//10번반목
            Node n = nList.item(i);//0~9 번 조회
            // System.out.println(n.getTextContent());
            Element elem = (Element)n;//테그이름 죄회를 위해서 Element로 전환 
            String ncsClCd = elem.getElementsByTagName("ncsClCd").item(0).getTextContent();//getElementsByTagName = NoList
            String compeUnitName = elem.getElementsByTagName("compeUnitName").item(0).getTextContent();
            Integer compeUnitLevel = Integer.parseInt(elem.getElementsByTagName("compeUnitLevel").item(0).getTextContent());
            String compeUnitDef = elem.getElementsByTagName("compeUnitDef").item(0).getTextContent();
            String ncsLclasCdnm = elem.getElementsByTagName("ncsLclasCdnm").item(0).getTextContent();
            String ncsMclasCdnm = elem.getElementsByTagName("ncsMclasCdnm").item(0).getTextContent();
            String ncsSclasCdnm = elem.getElementsByTagName("ncsSclasCdnm").item(0).getTextContent();
            String ncsSubdCdnm = elem.getElementsByTagName("ncsSubdCdnm").item(0).getTextContent();
            
            NCSInfoVO vo = new NCSInfoVO(); //vo연결 
            vo.setNs_code(ncsClCd);
            vo.setNs_name(compeUnitName);
            vo.setNs_level(compeUnitLevel);
            vo.setNs_def(compeUnitDef);
            vo.setNs_l_class(ncsLclasCdnm);
            vo.setNs_m_class(ncsMclasCdnm);
            vo.setNs_s_class(ncsSclasCdnm);
            vo.setNs_d_class(ncsSubdCdnm);
            mapper.insertNCSInfo(vo);//db로 날려
        }
    }
    catch(Exception e) {
        e.printStackTrace();
    }
        
        return "";
    }
}
    