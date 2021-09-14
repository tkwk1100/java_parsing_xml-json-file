package com.example.api;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.example.vo.DaeguVO;
import com.example.mapper.DaeguMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@RestController
public class DaeguAPIcintroller {
    @Autowired DaeguMapper D_mapper; //mapper 가져오기
    @GetMapping("/Daegu/xml")
    public String getDaeguinfoXMl() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://car.daegu.go.kr/openapi-data/service/rest/data/linkspeed"); //요청 url
        urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") +
            "=6o9k%2FijVJS6Syp4mxKkkLoK4Ax%2F5LpR6Rl0CcUgX6BB%2FzD1%2BL7FGFGaF7wocaB0J6A5B%2Bu3qY1%2FZY%2BQsDaseSQ%3D%3D"); //ServiceKey 입력
        // urlBuilder.append("&"+URLEncoder.encode("type", "UTF-8")+"="+URLEncoder.encode("xml","UTF-8"));//제공형태(json,xml) 타입
        // urlBuilder.append("&"+URLEncoder.encode("pageNo", "UTF-8")+"="+URLEncoder.encode("1","UTF-8"));//페이지번호 - 기본값 : 
        // urlBuilder.append("&"+URLEncoder.encode("numOfRows", "UTF-8")+"="+URLEncoder.encode("10","UTF-8"));//페이지당 데이터 수 - 기본값 : 

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(urlBuilder.toString()); // 공공데이터 요청 url
            // System.out.println(urlBuilder.toString());
            doc.getDocumentElement().normalize(); //java에서 파싱가능 하도록 format전환
            System.out.println(doc.getDocumentElement().getNodeName()); //전체문서 갔고오기
            NodeList nList = doc.getElementsByTagName("item"); //1덩어리 = 노드
            System.out.println(nList.getLength()); // 요청노드 개수

            String fileName = makeFlieName("Daegu_xml.txt");

            //파일 열기 (생성?)
            FileOutputStream fos = new FileOutputStream("/home/data"+fileName);
            //파일 쓰기 스트림 생성 (파라미터는 FileOutputStream 개체를 넣는다.)
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8"); //액셀에서 깨지면 "MS949"
            //파이 쓰기 중 끊기는 오류가 발생할 수도 있기 때문에
            BufferedWriter bw = new BufferedWriter(osw);

            for (int i = 0; i < nList.getLength(); i++) { //반복(전부)
                Node n = nList.item(i);
                // System.out.println(n.getTextContent());
                Element emel = (Element) n; //테그이름 조회를 위해 Element로 전환 
                String atmsTm = emel.getElementsByTagName("atmsTm").item(0).getTextContent();
                Double dist = Double.parseDouble(emel.getElementsByTagName("dist").item(0).getTextContent());
                String dsrcLinkSn = emel.getElementsByTagName("dsrcLinkSn").item(0).getTextContent();
                String endFacNm = emel.getElementsByTagName("endFacNm").item(0).getTextContent();
                String linkSpeed = emel.getElementsByTagName("linkSpeed").item(0).getTextContent();
                String linkTime = emel.getElementsByTagName("linkTime").item(0).getTextContent();
                String roadNm = emel.getElementsByTagName("roadNm").item(0).getTextContent();
                String sectionInfoCd = emel.getElementsByTagName("sectionInfoCd").item(0).getTextContent();
                String sectionNm = emel.getElementsByTagName("sectionNm").item(0).getTextContent();
                String startFacNm = emel.getElementsByTagName("startFacNm").item(0).getTextContent();
                String stdLinkId = emel.getElementsByTagName("stdLinkId").item(0).getTextContent();

                DaeguVO vo = new DaeguVO(); //vo 연결
                vo.setDu_atmsTm(atmsTm);
                vo.setDu_dist(dist);
                vo.setDu_dsrcLinkSn(dsrcLinkSn);
                vo.setDu_endFacNm(endFacNm);
                vo.setDu_linkSpeed(linkSpeed);
                vo.setDu_linkTime(linkTime);
                vo.setDu_roadNm(roadNm);
                vo.setDu_sectionInfoCd(sectionInfoCd);
                vo.setDu_sectionNm(sectionNm);
                vo.setDu_startFacNm(startFacNm);
                vo.setDu_stdLinkId(stdLinkId);
                // D_mapper.insertDaeguinfo(vo); //db로 날려

                String s = "";
                s += vo.getDu_atmsTm() + "|";
                s += vo.getDu_dist() + "|";
                s += vo.getDu_dsrcLinkSn() + "|";
                s += vo.getDu_endFacNm() + "|";
                s += vo.getDu_linkSpeed() + "|";
                s += vo.getDu_linkTime() + "|";
                s += vo.getDu_roadNm() + "|";
                s += vo.getDu_sectionInfoCd() + "|";
                s += vo.getDu_sectionNm() + "|";
                s += vo.getDu_startFacNm() + "|";
                s += vo.getDu_stdLinkId() + "\n";

                bw.write(s);
            }
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String makeFlieName(String name){
        Calendar c = Calendar.getInstance();
        Integer y = c.get(Calendar.YEAR);
        Integer m = c.get(Calendar.MONTH)+1;
        Integer d = c.get(Calendar.DATE);
        Integer h = c.get(Calendar.HOUR_OF_DAY);
        Integer min = c.get(Calendar.MINUTE);

        String fileName = "";
        fileName += y;

        if(m<10) fileName += "0"+m;
        else fileName += m;// fileName += m<10?"0"+m:m; 과 같은것



        // fileName += m<10?"0"+m:m;//삼항연산자
        fileName += d<10?"0"+d:d;
        fileName += h<10?"0"+h:h;
        fileName += min<10?"0"+min:min;
        fileName += "_"+name;

        return fileName;
    }
}