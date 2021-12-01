package com.hitejinro.snop.common.util;

/**
 * 공통 상수 클래스
 * @author ykw
 *
 */
public class Const {
    
    /**
     * 처리 결과의 상태 변수명 : ex) S(성공), F(실패)
     */
    public static final String RESULT_FLAG = "_RESULT_FLAG";
    
    /**
     * 처리 결과의 메시지 변수명
     */
    public static final String RESULT_MSG = "_RESULT_MSG";
    
    /**
     * TreeGrid의 Header 형식 명칭.
     */
    public static final String GRID_HEADER = "Header";

    /**
     * TreeGrid의 Data 형식 명칭.
     * <p>※ TreeGrid 사이트 참고 : {Body : [ [] ]}
     */
    public static final String GRID_BODY = "Body";
    
    /**
     * 추가적으로 전송할 데이터 명칭
     */
    public static final String ATTR = "Attr";
    
    /**
     * TreeGrid의 Row Status 변수명
     * <p>※ commonUtils.js의 "cfn_getGridChangedInfo" 펑션 참고
     */
    public static final String ROW_STATUS = "action";
    /**
     * TreeGrid의 Row Status 중 신규 행 상태값
     * <p>※ commonUtils.js의 "cfn_getGridChangedInfo" 펑션 참고
     */
    public static final String ROW_STATUS_INSERT = "insert";
    /**
     * TreeGrid의 Row Status 중 수정 행 상태값
     * <p>※ commonUtils.js의 "cfn_getGridChangedInfo" 펑션 참고
     */
    public static final String ROW_STATUS_UPDATE = "update";
    /**
     * TreeGrid의 Row Status 중 삭제 행 상태값
     * <p>※ commonUtils.js의 "cfn_getGridChangedInfo" 펑션 참고
     */
    public static final String ROW_STATUS_DELETE = "delete";
    
}
