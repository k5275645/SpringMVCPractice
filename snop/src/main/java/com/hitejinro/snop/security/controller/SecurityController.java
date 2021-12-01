package com.hitejinro.snop.security.controller;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.hitejinro.snop.common.util.CommonUtils;
import com.hitejinro.snop.common.util.CryptoAES;
import com.hitejinro.snop.common.util.SessionUtil;
import com.hitejinro.snop.security.service.UserDetailsServiceImpl;
import com.hitejinro.snop.security.vo.User;

@Controller
public class SecurityController {

	private static final Logger logger = LoggerFactory.getLogger(SecurityController.class);

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    
    @Inject
    private SessionUtil sessionUtil;
    
    @Inject
    private CommonUtils commonUtils;

	/**
	 * 로그인 페이지 호출
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/security/login", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam Map<String, Object> params) throws Exception {
		logger.info("/security/login");
		
		ModelAndView view = new ModelAndView("/security/login");
		return view;
	}
    
    /**
     * sso를 이용한 로그인 처리 : 탑피스()/원스탑(viewMenu=xxxx) 이용<p>
     * <li> 탑피스 : http://192.168.101.25:18080/snop/security/sso?userId=12345
     * <li> 원스탑 : http://192.168.101.25:18080/snop/security/sso?userId=12345&viewMenu=%2femsb%2fEMSB5070
     * @param params
     * @param model
     * @param req
     * @param res
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/security/sso")
    public ModelAndView sso(@RequestParam Map<String,Object> params, ModelMap model, HttpServletRequest req, HttpServletResponse res) {
        String sUserId = (String)params.get("userId");
        String sInitMenu = StringUtils.isEmpty(params.get("initMenu")) ? "" : (String)params.get("initMenu"); // - 초기에 보여줄 화면(프레임 포함)
        String sViewMenu = StringUtils.isEmpty(params.get("viewMenu")) ? "" : (String)params.get("viewMenu"); // - 보여줄 화면(프레임 미포함)
        String sInitUrl = "/"; // - 보여줄 화면 URL. 초기값은 프레임 포함
        String sRecallYn = StringUtils.isEmpty(params.get("_RECALL_")) ? "" : (String)params.get("_RECALL_"); // - ie에서 재호출된 페이지 여부

        // IE 10 :: Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Trident/6.0)
        // IE 11 :: Mozilla/5.0 (Windows NT 6.3; Trident/7.0; rv:11.0) like Gecko
        // (old)Edge :: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2486.0 Safari/537.36 Edge/13.10586
        // Chromium Edge ::: mozilla/5.0 (windows nt 10.0; win64; x64) applewebkit/537.36 (khtml, like gecko) chrome/76.0.3800.0 safari/537.36 edg/76.0.167.1
        String sUserAgent = StringUtils.isEmpty(req.getHeader("User-Agent")) ? "" : req.getHeader("User-Agent");
        String sBrowserName = "";
        if(sUserAgent.toLowerCase().indexOf("edge") > -1) {
            sBrowserName = "edge";
        } else if(sUserAgent.toLowerCase().indexOf("edg/") > -1) {
            sBrowserName = "chromium based edge (dev or canary)";
        } else if(sUserAgent.toLowerCase().indexOf("opr") > -1) {
            sBrowserName = "opera";
        } else if(sUserAgent.toLowerCase().indexOf("chrome") > -1) {
            sBrowserName = "chrome";
        } else if(sUserAgent.toLowerCase().indexOf("msie") > -1) {
            sBrowserName = "ie10";
        } else if(sUserAgent.toLowerCase().indexOf("trident") > -1) {
            sBrowserName = "ie11";
        } else if(sUserAgent.toLowerCase().indexOf("firefox") > -1) {
            sBrowserName = "firefox";
        } else if(sUserAgent.toLowerCase().indexOf("safari") > -1) {
            sBrowserName = "safari";
        } else {
            sBrowserName = "other";
        }
        commonUtils.debugParams(model);
        
        try {
            // 1. SSO 를 이용한 로그인 처리
            String sClientIp = sessionUtil.getClientIp();
            String sUrl = sessionUtil.getRequestUrl();
            logger.info("###### 로그인 처리 :: userId=" + sUserId + ", RecallYn=" + sRecallYn + ", clientIp=" + sClientIp + ", url : " + sUrl + ", initMenu=" + sInitMenu + ", viewMenu=" + sViewMenu + ", BrowserName=" + sBrowserName + " :: User-Agent : " + sUserAgent); // - IP로는 내부망 구분 불가(일부 공장은 외부IP사용)
            boolean bFlag = false;
            if("LOCAL".equals(sessionUtil.getServerMode()) || "DEV".equals(sessionUtil.getServerMode()) || "PROD".equals(sessionUtil.getServerMode())) {
                // - 내부망(http://localhost:8080/), 개발계(http://192.168.101.30:18080/snop/), 운영계(http://192.168.101.25:18080/snop/) 만 가능
                bFlag = true;
            } else {
                // - 외부망 : 모두 불가
                bFlag = false;
            }
            if(!bFlag) {
                throw new BadCredentialsException( "접근할 수 없는 경로입니다." );
            } else if(StringUtils.isEmpty(sUserId)) {
                throw new UsernameNotFoundException("접속자 정보를 찾을 수 없습니다.");
            }
            
            // - IE 브라우져 처리
            if(sBrowserName.indexOf("ie") > -1 && !"Y".equals(sRecallYn)) {
                // - 최초에 IE브라우져에서 접근시 : Edge로 재호출
                RedirectView rv = new RedirectView("microsoft-edge:" + sUrl);
                rv.setExposeModelAttributes(true);
                
                // - 사번은 암호화 처리 : 사용자의 주소창에 보이는 문제가 발생해서
                sUserId = CryptoAES.getEncrypted(sUserId);

                ModelAndView view = new ModelAndView(rv);
                view.addObject("userId", sUserId);
                view.addObject("initMenu", sInitMenu);
                view.addObject("viewMenu", sViewMenu);
                view.addObject("_RECALL_", "Y"); // - 재호출 여부
                
                logger.info("########## IE 브라우저를 Edge로 재호출 : userId=" + sUserId + ", clientIp=" + sClientIp);
                return view;
                
            } else if(sBrowserName.indexOf("ie") > -1 && "Y".equals(sRecallYn)) {
                // - 재호출로 IE브라우져에서 접근시 : 경고창 후, 종료
                throw new UsernameNotFoundException("IE에서는 S&OP 서비스를 사용할 수 없습니다. 관리자에게 문의하거나, Edge 설치 후 다시 시도해주세요.");
                
            } else if("edge".equals(sBrowserName)) {
                // - 구 Edge로 접근시 : Edge 업그레이드 창으로 이동
                RedirectView rv = new RedirectView("https://www.microsoft.com/ko-kr/edge");
                return new ModelAndView(rv);
                
            } else if("Y".equals(sRecallYn)) {
                // - IE -> 신 Edge로 전환되어서 호출시 : 암호화된 사번의 복호화 처리
                sUserId = CryptoAES.getDecrypted(sUserId);
                if(StringUtils.isEmpty(sUserId)) {
                    // - 복호화시 결과값이 비어있다는 것은, 암호화값이 아니라는 것임
                    throw new BadCredentialsException("허용되지 않은 방법으로 접속을 시도하였습니다.");
                }
            }
            
            User user = userDetailsService.loadUserByUsername(sUserId);
            if(user == null ) throw new UsernameNotFoundException("접속자 정보를 찾을 수 없습니다.");
            logger.info(user.toString());
            
            if(!StringUtils.isEmpty(sInitMenu)) {
                // - 초기 화면이 존재할 경우, 세션에 담아준다.
                sessionUtil.setInitMenu(sInitMenu); // - 초기 화면 연결 주소
            }
            
            Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
    
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            HttpSession session = req.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
            
            // - User Log 기록 : CommonInterceptor 에서 자동 처리
            //sessionUtil.insertUserLog(userDetailsService, req);

            // - 프레임없이 보여줄 화면 연결
            if(!StringUtils.isEmpty(sViewMenu)) {
                sInitUrl = sViewMenu;
                sessionUtil.setInitMenu(""); // - 초기 화면 연결 주소는 지운다.
            }
            
        } catch(UsernameNotFoundException e) {
            logger.info(e.toString() + " :: userId=" + sUserId);
            throw new UsernameNotFoundException(e.getMessage());
        } catch(BadCredentialsException e) {
            logger.info(e.toString() + " :: userId=" + sUserId);
            throw new BadCredentialsException(e.getMessage());
        } catch(Exception e) {
            e.printStackTrace();
            logger.info(e.toString() + " :: userId=" + sUserId);
            throw new RuntimeException(e.getMessage());
        }
        
        RedirectView rv = new RedirectView(req.getContextPath() + sInitUrl);
        rv.setExposeModelAttributes(false);
        ModelAndView view = new ModelAndView(rv);
        return view;
    }
    
}
