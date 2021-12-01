package com.hitejinro.snop.work.service;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hitejinro.snop.work.dao.M03071DaoMapper;

/**
 * 용기 시뮬레이션 버전 비교
 * @author 남동희
 *
 */
@Service
public class M03071Service {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(M03071Service.class);

	@Inject
	private M03071DaoMapper m03071DaoMapper;

	public List<Map<String, Object>> searchVerCd(Map<String, Object> params) throws Exception {
		return m03071DaoMapper.searchVerCd(params);
	}

	/**
	 * 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> search(Map<String, Object> params) throws Exception {
		return m03071DaoMapper.search(params);
	}
}
