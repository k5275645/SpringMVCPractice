package com.hitejinro.snop.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.hitejinro.snop.security.service.UserDetailsServiceImpl;
import com.hitejinro.snop.security.vo.User;

@Component
public class SessionUtil {
    
	private static final Logger logger = LoggerFactory.getLogger(SessionUtil.class);
    
    // - 개발/운영계(내부망) URL
    public final static String PROD_URL = "http://192.168.101.25:18080/";
    public final static String DEV_URL = "http://192.168.101.30:18080/";
    public final static String LOCAL_URL = "http://localhost";

    private static String INIT_MENU = "INIT_MENU";
    
    @Inject
    private CommonUtils commonUtils;
    
    /**
     * session의 User 객체 반환
     * @return
     */
    public User getUserInfo() {
        User user = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        if (auth != null && auth.getPrincipal() != null && auth.getPrincipal() instanceof User) {
            user = (User) auth.getPrincipal();
        } else {
            user = new User();
        }
        return user;
    }
    
    /**
     * 변수에 사용자 정보를 넣어준다. : userId, userNm
     * @param params
     * @return
     */
    public Map<String,Object> setUserInfoParam(Map<String,Object> params) {
        if(params == null) params = new HashMap<String,Object>();
        params.put("userId", getUserId());
        params.put("userNm", getUserNm());
        return params;
    }

    /**
     * 변수에 사용자 정보를 넣어준다. : userId, userNm
     * @param model
     * @return
     */
    public Map<String,Object> setUserInfoModel(ModelMap model) {
        if(model == null) model = new ModelMap();
        model.addAttribute("userId", getUserId());
        model.addAttribute("userNm", getUserNm());
        return model;
    }

    /**
     * 접속자의 USER_ID를 반환
     * @return
     */
    public String getUserId() {
        String sUserId = "";
        if(getUserInfo() != null) sUserId = getUserInfo().getUsername();
        return sUserId;
    }

    /**
     * 접속자의 USER_NM를 반환
     * @return
     */
    public String getUserNm() {
        String sUserNm = "";
        if(getUserInfo() != null) sUserNm = getUserInfo().getUserNm();
        return sUserNm;
    }

    /**
     * 세션에서 특정 Key를 반환
     * @param sKey
     * @return
     */
    public Object getSessionValue(String sKey) {
        if(getRequest() != null) {
            return getSession().getAttribute(sKey);
        } else {
            return null;
        }
    }

    /**
     * 세션에 특정 Key에 값을 셋팅
     * @param sKey
     * @param oValue
     */
    public void setSessionValue(String sKey, Object oValue) {
        if(getRequest() != null) {
            getSession().setAttribute(sKey, oValue);
        }
    }

    /**
     * 초기화면 URL을 담는다.
     * @param sInitMenuUrl
     */
    public void setInitMenu(String sInitMenuUrl) {
        setSessionValue(INIT_MENU, sInitMenuUrl);
    }

    /**
     * 초기화면 URL을 반환한다.
     * @return
     */
    public String getInitMenu() {
        return (String)getSessionValue(INIT_MENU);
    }

    /**
     * 세션을 반환
     * @return
     */
    private HttpSession getSession() {
        if(getRequest() != null) {
            return getRequest().getSession();
        }
        return null;
    }

    /**
     * Request를 반환
     * @return
     */
    public HttpServletRequest getRequest() {
        if ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes() == null) {
            return null;
        }
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 접근자의 IP를 반환한다.
     * @return
     */
    public String getClientIp() {
        return getClientIp(getRequest());
    }

    /**
     * 접근자의 IP를 반환한다.
     * @param request
     * @return
     */
    public String getClientIp(HttpServletRequest request) {
        String sIp = "";
        if(request != null) {
            sIp = request.getHeader("X-FORWARDER-FOR");
            if (sIp == null) {
                sIp = request.getHeader("Proxy-Client-IP");
            }
            if (sIp == null) {
                sIp = request.getHeader("WL-Proxy-Client-IP");
            }
            if (sIp == null) {
                sIp = request.getHeader("HTTP_CLIENT_IP");
            }
            if (sIp == null) {
                sIp = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if(sIp == null) {
                sIp = request.getRemoteAddr();
            }
        }
        return sIp;
    }

    /**
     * Request URL을 반환한다.
     * @return
     */
    public String getRequestUrl() {
        return getRequestUrl(getRequest());
    }

    /**
     * Request URL을 반환한다.
     * @return
     */
    public String getRequestUrl(HttpServletRequest request) {
        String sReqUrl = "";
        if(request != null) {
            sReqUrl = request.getRequestURL().toString();
        }
        return sReqUrl;
    }
    
    /**
     * 서버의 URL을 반환한다.
     * @param req
     * @return ex) "http://localhost:8080/" or "http://192.168.101.64:8080/" or "http://ems.hitejinro.com:8080/"
     */
    public String getServerUrl(HttpServletRequest req) {
        String sServerUrl = "";
        String sUrl = getRequestUrl(req);
        if(StringUtils.countOccurrencesOf(sUrl, "/") > 2) {
            int iLen = sUrl.length();
            int iCnt = 0;
            for(int i = 0 ; i < iLen ; i++) {
                if("/".equals(sUrl.substring(i,i+1))) iCnt++;
                if(iCnt == 3) {
                    sServerUrl = sUrl.substring(0, i+1);
                    break;
                }
            }
        }
        return sServerUrl;
    }
    
    /**
     * 서버의 URL을 반환한다.
     * @return ex) "http://localhost:8080/" or "http://192.168.101.64:8080/" or "http://ems.hitejinro.com:8080/"
     */
    public String getServerUrl() {
        return getServerUrl(getRequest());
    }
    
    /**
     * 서버의 모드(로컬/개발/운영)를 반환한다.
     * <li>로컬 : LOCAL
     * <li>개발 : DEV
     * <li>운영 : PROD
     * @param req
     * @return
     */
    public String getServerMode(HttpServletRequest req) {
        String sServerMode = "";
        String sUrl = getRequestUrl(req);
        if(sUrl != null) {
            if(sUrl.indexOf(SessionUtil.LOCAL_URL) == 0) {
                sServerMode = "LOCAL";
            } else if(sUrl.indexOf(SessionUtil.DEV_URL) == 0) {
                sServerMode = "DEV";
            } else if(sUrl.indexOf(SessionUtil.PROD_URL) == 0) {
                sServerMode = "PROD";
            }
        }
        return sServerMode;
    }
    
    /**
     * 서버의 모드(로컬/개발/운영)를 반환한다.
     * <li>로컬 : LOCAL
     * <li>개발 : DEV
     * <li>운영 : PROD
     * @return
     */
    public String getServerMode() {
        return getServerMode(getRequest());
    }
    
    /**
     * User Log 생성
     * <LI>Default : CommonInterceptor.preHandle()
     * <LI>ID/PWD 로그인 : UserLoginSuccessHandler.onAuthenticationSuccess()
     * <LI>logout : SignController.logout()
     * 
     * @param userSecuService
     * @param request
     */
    public void insertUserLog(Object oService, HttpServletRequest request) {
        try {
            Map<String, Object> mUserLogParam = getUserLogParam(request);
            /*
            */
            // - 디버깅 예외 체크 :
            String sMenuUrl = mUserLogParam.get("menuUrl") == null ? "" : (String)mUserLogParam.get("menuUrl");
            String sUserId = mUserLogParam.get("userId") == null ? "" : (String)mUserLogParam.get("userId");
            
            if(sMenuUrl.indexOf("common/checkSession") != 0                 // - "common/checkSession"(세션체크용)
               && sMenuUrl.indexOf("resources/") != 0                       // - "resources/..."(이미지, js 등)
               && sMenuUrl.indexOf("user/loginPage") != 0                   // - 로그인 페이지
               && sMenuUrl.indexOf("common/main") != 0                     // - 메인 프레임
               && sMenuUrl.indexOf("common/") != 0 && sMenuUrl.indexOf("commonCombo/") != 0 // - common쪽 호출은 모두 제외
               && !StringUtils.isEmpty(sMenuUrl)                            // - 메인프레임 제외
               && !StringUtils.isEmpty(sUserId)                             // - 로그인 전과 같은 경우 제외 : sso 로그인시에는 로그인처리전에 Interceptor가 발생하면서 로깅을 하려는 문제. 로그인 처리 후에 재 호출할 예정
              ) {
                // - 디버깅 처리
                String sLogStr = "";
                sLogStr += "\n" + "############################";
                sLogStr += "\n" + "serverMode :: " + mUserLogParam.get("serverMode");
                sLogStr += "\n" + "requestUrl :: " + mUserLogParam.get("requestUrl");
                sLogStr += "\n" + "serverUrl :: " + mUserLogParam.get("serverUrl");
                sLogStr += "\n" + "menuUrl :: " + mUserLogParam.get("menuUrl");
                sLogStr += "\n" + "userId :: " + mUserLogParam.get("userId");
                sLogStr += "\n" + "clientIp :: " + mUserLogParam.get("clientIp");
                sLogStr += "\n" + "parameter :: " + mUserLogParam.get("parameter");
                sLogStr += "\n" + "############################";
                if(true) logger.info(sLogStr);
                
                // - User Log 테이블(SCMU.W_SOP_TB_USER_LOG_F)에 넣기 :: 쿼리(com.hitejinro.snop.security.dao.SecurityDaoMapper.insertUserLog) 참고
                if(oService instanceof UserDetailsServiceImpl) {
                    ((UserDetailsServiceImpl)oService).insertUserLog(mUserLogParam);
                }
            }
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * User Log를 위한 Map 반환
     * @param request
     * @return { serverMode, serverUrl, requestUrl, clientIp, menuUrl, parameter, userId }
     * @throws Exception
     */
    private Map<String, Object> getUserLogParam(HttpServletRequest request) throws Exception {
        Map<String, Object> mParams = new HashMap<String, Object>();

        String sServerMode = getServerMode(request);                // - LOCAL, DEV, PROD
        String sRequestUrl = getRequestUrl(request);                // - http://localhost:8080/snop/work/
        String sServerUrl = getServerUrl(request);                  // - http://localhost:8080/
        String sMenuUrl = "";                                       // - work/M02010
        if(sServerUrl != null && sRequestUrl != null && sRequestUrl.indexOf(sServerUrl) > -1) {
            sMenuUrl = sRequestUrl.substring(sServerUrl.length());
            if(sMenuUrl.indexOf("snop/") == 0) {
                sMenuUrl = sMenuUrl.substring("snop/".length());
            }
        }
        String sUserId = getUserId();
        String sClientIp = getClientIp(request);
        
        Enumeration<String> eParameterNames = null;
        if(request != null) eParameterNames = request.getParameterNames();
        String sParamsStr = "";

        if (eParameterNames != null && eParameterNames.hasMoreElements()) {
            while (eParameterNames.hasMoreElements()) {
                String sKey = (String) eParameterNames.nextElement();
                if("passwd".equalsIgnoreCase(sKey)) continue; // - 패스워드는 제외
                if("password".equalsIgnoreCase(sKey)) continue; // - 패스워드는 제외
                String sVal = request.getParameter(sKey);
                if(sVal != null && sVal.length() > 100) sVal = sVal.substring(0, 100); // - 파라메터의 값은 100글자로 제한
                sParamsStr += ("".equals(sParamsStr) ? "" : ", ") + sKey + "=" + sVal;
            }
        } else {
            // - Request Parameter 방식이 아닌, RequestBody 방식으로 넘어오는 경우 체크
            try {
                String sBody = getBody(request);
                Map<String, Object> mParam = commonUtils.getJsonStrToMap(sBody);
                if (mParam != null && !mParam.isEmpty()) {
                    Iterator<String> itr = mParam.keySet().iterator();
                    for (; itr.hasNext();) {
                        String sKey = itr.next();
                        if("passwd".equalsIgnoreCase(sKey)) continue; // - 패스워드는 제외
                        if("password".equalsIgnoreCase(sKey)) continue; // - 패스워드는 제외
                        String sVal = mParam.get(sKey).toString();
                        if(sVal != null && sVal.length() > 100) sVal = sVal.substring(0, 100); // - 파라메터의 값은 100글자로 제한
                        sParamsStr += ("".equals(sParamsStr) ? "" : ", ") + sKey + "=" + sVal;
                    }
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        
        mParams.put("serverMode", sServerMode);
        mParams.put("requestUrl", sRequestUrl);
        mParams.put("serverUrl", sServerUrl);
        mParams.put("menuUrl", sMenuUrl);
        mParams.put("userId", sUserId);
        mParams.put("clientIp", sClientIp);
        mParams.put("parameter", sParamsStr);

        return mParams;
    }
    
    /**
     * request에서 Body 부분을 반환
     * @param request
     * @return
     * @throws IOException
     */
    private String getBody(HttpServletRequest request) throws IOException {
        
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
 
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }
 
        body = stringBuilder.toString();
        return body;
    }
    
}
