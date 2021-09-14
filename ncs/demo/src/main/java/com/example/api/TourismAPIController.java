package com.example.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


import com.example.mapper.TourismMapper;
import com.example.vo.TourismVO;

import java.io.BufferedReader;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@RestController
public class TourismAPIController {
    @Autowired TourismMapper t_mapper;
    @GetMapping("/Tourism")
    public String getTourism() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("http://api.data.go.kr/openapi/tn_pubr_public_trsmic_api"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=6o9k%2FijVJS6Syp4mxKkkLoK4Ax%2F5LpR6Rl0CcUgX6BB%2FzD1%2BL7FGFGaF7wocaB0J6A5B%2Bu3qY1%2FZY%2BQsDaseSQ%3D%3D "); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*XML/JSON 여부*/
        

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        System.out.println(sb.toString());


        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(sb.toString());
            JSONObject rootObj = (JSONObject) jsonObj.get("response");
            JSONArray itemsArr = (JSONArray) rootObj.get("items");
            for (int i = 0; i < itemsArr.size(); i++) {
                JSONObject item = (JSONObject) itemsArr.get(i);

                TourismVO vo = new TourismVO();
                vo.setT_trsmicNm(item.get("trsmicNm").toString());
                vo.setT_trsmicLc(item.get("trsmicLc").toString());
                vo.setT_ctprvnNm(item.get("ctprvnNm").toString());
                vo.setT_signguNm(item.get("signguNm").toString());
                vo.setT_trsmicIntrcn(item.get("trsmicIntrcn").toString());
                vo.setT_adiSvcInfo(item.get("adiSvcInfo").toString());
                vo.setT_rstde(item.get("rstde").toString());
                vo.setT_summerOperOpenHhmm(item.get("summerOperOpenHhmm").toString());
                vo.setT_summerOperCloseHhmm(item.get("summerOperCloseHhmm").toString());
                vo.setT_winterOperOpenHhmm(item.get("winterOperOpenHhmm").toString());
                vo.setT_winterOperCloseHhmm(item.get("winterOperCloseHhmm").toString());
                vo.setT_avrgWorkCo(Integer.parseInt(item.get("avrgWorkCo").toString()));
                vo.setT_engGuidanceYn(item.get("engGuidanceYn").toString());
                vo.setT_jpGuidanceYn(item.get("jpGuidanceYn").toString());
                vo.setT_chGuidanceYn(item.get("chGuidanceYn").toString());
                vo.setT_guidanceFggg(item.get("guidanceFggg").toString());
                vo.setT_guidancePhoneNumber(item.get("guidancePhoneNumber").toString());
                vo.setT_rdnmadr(item.get("rdnmadr").toString());
                vo.setT_lnmadr(item.get("lnmadr").toString());
                vo.setT_operInstitutionNm(item.get("operInstitutionNm").toString());
                vo.setT_homepageUrl(item.get("homepageUrl").toString());
                vo.setT_latitude(item.get("latitude").toString());
                vo.setT_longitude(item.get("longitude").toString());
                vo.setT_referenceDate(item.get("referenceDate").toString());
                vo.setT_instt_code(item.get("instt_code").toString());
                vo.setT_instt_nm(item.get("instt_nm").toString());
                t_mapper.insertTourism(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}