package kr.co.softcampus.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class TestInterceptor7 implements HandlerInterceptor {
	// Controller의 메서드가 호출되기 전에 호출된다
	// false를 반환하게 되면 요청처리에 대한 진행이 중단된다
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("TestInterceptor7 - preHandle");
		return true;
	}

	// Controller의 메서드가 호출된 후 호출된다.
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("TestInterceptor7 - postHandle");
	}

	// View 처리까지 완료되면 호출된다
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		System.out.println("TestInterceptor7 - afterCompletion");
	}
}
