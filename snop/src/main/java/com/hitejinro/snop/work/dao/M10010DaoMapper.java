package com.hitejinro.snop.work.dao;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

/**
 * SnOP회의 > 주간S&OP회의 화면준비
 * @author 유기후
 *
 */
@Repository
public interface M10010DaoMapper {
    
    /**
     * 데이터 조회
     * @param params { }
     * @return [{ MENU_CD, MENU_NM, MENU_DESC, MENU_NM_PATH, SEQ, RMKS, MENU_VAR }]
     * @throws Exception
     */
    public List<Map<String, Object>> search(Map<String, Object> params) throws Exception;
    
    /**
     * 최하위 메뉴 리스트 조회
     * @param params {  }
     * @return [{ MENU_CD, MEMU_NM, MENU_DESC, MENU_NM_PATH, LVL, ... }]
     * @throws Exception
     */
    public List<Map<String, Object>> selectLeafMenuList(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 추가/수정
     * @param params { userId, updateList:[{}] }
     * @return
     * @throws Exception
     */
    public int update(Map<String, Object> params) throws Exception;
    
    /**
     * 데이터 저장 : 삭제
     * @param params { deleteList:[{}] }
     * @return
     * @throws Exception
     */
    public int delete(Map<String, Object> params) throws Exception;
    
}
