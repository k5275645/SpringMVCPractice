package com.hitejinro.snop.security.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.security.service.UserDetailsServiceImpl;
import com.hitejinro.snop.security.vo.User;

/**
 * Interceptor Class
 * @author ykw
 *
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {
    @SuppressWarnings("unused")
    private static final Logger logger = LoggerFactory.getLogger(CommonInterceptor.class);
    
    @Inject
    private SessionUtil sessionUtil;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    /**
     * Client(Local) 에서 Controller(Server) 요청하기 전
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        // - User Log 기록
        try {
            sessionUtil.insertUserLog(userDetailsService, request);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return super.preHandle(request, response, handler);
    }
    
    /**
     * Controller(Server) 에서 Client(Local) 응답하기 전
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
        if(modelAndView != null && !StringUtils.isEmpty(modelAndView.getViewName()) && modelAndView.getViewName().indexOf("/security/sso") < 0) { // - jsp 호출일때만 처리
            // - css, js 등의 파일에 대한 변환일자 관리
            modelAndView.addObject("COMMON_CSS_VER", "20210901_01");
            modelAndView.addObject("JQUERY_MULTISELECT_CSS_VER", "20210713_01");
            modelAndView.addObject("JQUERY_UI_CSS_VER", "20210713_01");

            modelAndView.addObject("COMMON_UTILS_JS_VER", "20210810_01");
            modelAndView.addObject("JQUERY_MULTISELECT_JS_VER", "20210727_01");
            modelAndView.addObject("COMMON_COMBO_JS_VER", "20210910_01");
            modelAndView.addObject("DATE_JS_VER", "20210817_01");
            
            // - 사용자의 메뉴권한 설정
            User userInfo = sessionUtil.getUserInfo();
            if(userInfo != null && userInfo.getUserAuthMenuBtnList() != null) {
                // - { 화면ID + "_ALLOW_SELECT" : "Y", 화면ID + "_ALLOW_SAVE" : "Y", 화면ID + "_ALLOW_EXEC" : "Y" }
                modelAndView.addAllObjects(userInfo.getUserAuthMenuBtnList());
                // - 사용자의 권한코드 넘기기 : USER_AUTH_CD
                modelAndView.addObject("USER_AUTH_CD", userInfo.getAuthCd());
                
            }
        }
        
        super.postHandle(request, response, handler, modelAndView);
    }
    
    /**
     * @param request
     * @param response
     * @param handler
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
