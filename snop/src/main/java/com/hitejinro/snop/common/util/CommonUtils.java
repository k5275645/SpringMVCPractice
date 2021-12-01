package com.hitejinro.snop.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 공통 유틸 함수
 * @author ykw
 *
 */
@Component
public class CommonUtils {
    
    // - log4j
    private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);
    
    @Inject
    private SessionUtil sessionUtil;


    /**
     * 파라메터의 내용을 Debug
     * @param params
     * @return
     */
    public String debugParams(Map<String, Object> params) {
        String sParamsLogStr = "";
        if (params != null && !params.isEmpty()) {
            Iterator<String> itr = params.keySet().iterator();
            for (; itr.hasNext();) {
                String sKey = itr.next();
                if(params.get(sKey) instanceof Clob) {
                    try {
                        String sVal = clobToString((Clob)params.get(sKey));
                        if(sVal != null && sVal.length() > 500) sVal = sVal.substring(0, 500) + " 외 " + (sVal.length()-500) + "자";
                        sParamsLogStr += ("".equals(sParamsLogStr) ? "" : ", ") + sKey + "=" + sVal;
                    } catch (Exception e) {
                        sParamsLogStr += ("".equals(sParamsLogStr) ? "" : ", ") + sKey + "=" + params.get(sKey);
                        e.printStackTrace();
                    }
                } else if(params.get(sKey) instanceof String) {
                    String sVal = (String)params.get(sKey);
                    if(sVal != null && sVal.length() > 500) sVal = sVal.substring(0, 500) + " 외 " + (sVal.length()-500) + "자";
                    sParamsLogStr += ("".equals(sParamsLogStr) ? "" : ", ") + sKey + "=" + sVal;
                } else {
                    sParamsLogStr += ("".equals(sParamsLogStr) ? "" : ", ") + sKey + "=" + params.get(sKey);
                }
            }
        }

        // - 호출된 클래스/메소드 알아오기 : 바로전의 Class와 MethodName으로 처리
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        StackTraceElement beforeStack = stacks[1];
        
        String sLog = "[" + sessionUtil.getUserId() + "]" + sessionUtil.getUserNm() + "(" + sessionUtil.getClientIp() + ") :: " + beforeStack.getClassName() + "." + beforeStack.getMethodName() + " :: params :: " + sParamsLogStr;
        logger.info(sLog);
        
        return sLog;
    }
    
    /**
     * 내용을 Debug
     * @param sParam
     * @return
     */
    public String debugParam(String sParam) {

        // - 호출된 클래스/메소드 알아오기 : 바로전의 Class와 MethodName으로 처리
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        StackTraceElement beforeStack = stacks[1];
        
        String sLog = "[" + sessionUtil.getUserId() + "]" + sessionUtil.getUserNm() + "(" + sessionUtil.getClientIp() + ") :: " + beforeStack.getClassName() + "." + beforeStack.getMethodName() + " :: param :: " + sParam;
        logger.info(sLog);
        
        return sLog;
    }

    /**
     * Error를 Debug
     * @param params
     * @return
     */
    public void debugError(Exception e) {
        String sErrorStr = "";
        if(e != null) {
            try {
                StringWriter swErr = new StringWriter();
                e.printStackTrace(new PrintWriter(swErr));
                sErrorStr = swErr.toString();

                // - 호출된 클래스/메소드 알아오기 : 바로전의 Class와 MethodName으로 처리
                StackTraceElement[] stacks = new Throwable().getStackTrace();
                StackTraceElement beforeStack = stacks[1];
                
                String sLog = "[" + sessionUtil.getUserId() + "]" + sessionUtil.getUserNm() + "(" + sessionUtil.getClientIp() + ") :: " + beforeStack.getClassName() + "." + beforeStack.getMethodName() + " :: Error :: " + sErrorStr;
                logger.error(sLog);
                
            } catch(Exception ee) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Clob 를 String 으로 변경
     *
     * @param clob
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public String clobToString(Clob clob) throws SQLException, IOException {
        if (clob == null) {
            return "";
        }
        StringBuffer strOut = new StringBuffer();
        String str = "";
        BufferedReader br = null;
        try {
            br = new BufferedReader(clob.getCharacterStream());
            while ((str = br.readLine()) != null) {
                strOut.append(str + "\n");
            }
        } catch(Exception e) {
            throw e;
        } finally {
            try { br.close(); } catch(Exception e) {};
        }
        return strOut.toString();
    }
    
    /**
     * 데이터를 트리그리드 형식에 맞춰서 담기
     * <P>참고) 트리그리드의 데이터는 List<List<Map<String, ObjecT>>> 형식으로 만들어야함.
     * @param mParam
     * @param sDataId 데이터를 담을 ID
     * @param arrDataList
     * @return
     * @throws Exception
     */
    public Map<String, Object> setTreeGridData(Map<String, Object> mParam, String sDataId, List<Map<String, Object>> arrDataList) throws Exception {
        if(mParam == null) {
            mParam = new HashMap<String, Object>();
        }
        if(StringUtils.isEmpty(sDataId)) {
            throw new Exception("데이터를 담는 명칭 선언이 없음");
        }
        if(arrDataList == null) {
            arrDataList = new ArrayList<Map<String, Object>>();
        }
        
        List<List<Map<String, Object>>> arrTreeGridDataList = new ArrayList<List<Map<String, Object>>>();
        arrTreeGridDataList.add(arrDataList);
        mParam.put(sDataId, arrTreeGridDataList);
        
        return mParam;
    }
    /**
     * Map을 Json 문자열 형식으로 변환
     * <BR/>Map안에 Map/HashMap, List/ArrayList 모두 가능
     * @param mObj
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String getMapToJsonStr(Map<String, Object> mObj) throws Exception {
        JSONObject objJson = new JSONObject();
        
        if (mObj == null) return objJson.toJSONString();
        
        Iterator itr = mObj.keySet().iterator();
        while(itr.hasNext()) {
            String sKey = (String)itr.next();
            if(mObj.get(sKey) == null) {
                objJson.put(sKey, mObj.get(sKey));
                
            } else if(mObj.get(sKey) instanceof List || mObj.get(sKey) instanceof ArrayList) {
                // - 배열
                List<Map<String, Object>> arrData = (List<Map<String, Object>>)(List<?>)mObj.get(sKey);
                JSONArray arrJson = new JSONArray();
                for(int i = 0 ; i < arrData.size() ; i++) {
                    JSONObject ojTmp = new JSONObject();
                    Map<String, Object> mTmp = arrData.get(i);
                    if(mTmp != null && mTmp.keySet() != null) {
                        Iterator itrTmp = mTmp.keySet().iterator();
                        while(itrTmp.hasNext()) {
                            String stmpKey = (String)itrTmp.next();
                            if(mTmp.get(stmpKey) instanceof Clob) {
                                // - Clob
                                ojTmp.put(stmpKey, clobToString((Clob) mTmp.get(stmpKey)));
                                
                            } else {
                                // - 그외
                                ojTmp.put(stmpKey, mTmp.get(stmpKey));
                            }
                        }
                    }
                    arrJson.add(ojTmp);
                }
                objJson.put(sKey, arrJson);
                
            } else if(mObj.get(sKey) instanceof Map || mObj.get(sKey) instanceof HashMap || mObj.get(sKey) instanceof LinkedHashMap) {
                // - Map
                JSONObject ojTmp = new JSONObject();
                Map<String, Object> mTmp = (Map)mObj.get(sKey);
                if(mTmp != null && mTmp.keySet() != null) {
                    Iterator itrTmp = mTmp.keySet().iterator();
                    while(itrTmp.hasNext()) {
                        String stmpKey = (String)itrTmp.next();
                        if(mTmp.get(stmpKey) instanceof Clob) {
                            // - Clob
                            ojTmp.put(stmpKey, clobToString((Clob) mTmp.get(stmpKey)));
                            
                        } else {
                            // - 그외
                            ojTmp.put(stmpKey, mTmp.get(stmpKey));
                        }
                    }
                }
                objJson.put(sKey, ojTmp);
                
            } else if(mObj.get(sKey) instanceof Clob) {
                // - Clob
                objJson.put(sKey, clobToString((Clob) mObj.get(sKey)));
                
            } else {
                // - 그외
                objJson.put(sKey, mObj.get(sKey));
            }
        }
        return objJson.toJSONString();
    }
    
    /**
     * List를 Json 문자열 형식으로 변환
     * @param arrList
     * @return
     * @throws Exception
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String getListToJsonStr(List<Map<String, Object>> arrList) throws Exception {
        JSONArray arrJson = new JSONArray();
        
        if (arrList == null) return arrJson.toJSONString();
        
        for(int i = 0 ; i < arrList.size() ; i++) {
            JSONObject ojTmp = new JSONObject();
            Map<String, Object> mTmp = arrList.get(i);
            if(mTmp != null && mTmp.keySet() != null) {
                Iterator itrTmp = mTmp.keySet().iterator();
                while(itrTmp.hasNext()) {
                    String stmpKey = (String)itrTmp.next();
                    if(mTmp.get(stmpKey) instanceof Clob) {
                        // - Clob
                        ojTmp.put(stmpKey, clobToString((Clob) mTmp.get(stmpKey)));
                        
                    } else {
                        // - 그외
                        ojTmp.put(stmpKey, mTmp.get(stmpKey));
                    }
                }
            }
            arrJson.add(ojTmp);
        }
        return arrJson.toJSONString();
    }
    
    /**
     * JsonObject 문자열을 Map<String, Object> 형식으로 변환해서 반환
     * @param sJsonObject
     * @return
     * @throws Exception
     */
    public Map<String, Object> getJsonStrToMap(String sJsonObject) throws Exception {
        Map<String, Object> mObject = new HashMap<String, Object>();
        
        if(sJsonObject != null && !"".equals(sJsonObject)) {
            mObject = new ObjectMapper().readValue(sJsonObject, new TypeReference<Map<String, Object>>() {});
        }
        
        return mObject;
    }

}
