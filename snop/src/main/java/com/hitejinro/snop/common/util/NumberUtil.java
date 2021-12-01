package com.hitejinro.snop.common.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.util.StringUtils;

/**
 * <pre>
 * </pre>
 *
 * @ author     kwonil
 * @ see        ex) NoticeList.jsp
 * @ version    1.0
 * @ reference packages : java.text, common.util.StringUtil
 * @ reference tables   : none
 *
 * **********************************************************************************************
 * @ Modification   Log
 * @ DATE       AUTHOR   DESCRIPTION
 * @
 *
 * **********************************************************************************************
 */

public class NumberUtil
{
    /**
     * <pre>
     *
     * #example
     *          if(NumberUtil.isNull(hitCount)) hitCount = 0;
     * </pre>
     *
     */
    public static boolean isNull(Object source)
    {
        if(source == null) return true;
        else               return false;
    }
    public static boolean isNull(String source)
    {
        if(source == null) return true;
        else               return false;
    }

    /**
     * 해당 값이 숫자인지 체크
     * @param oValue 체크할 값
     * @param isNullError NULL을 오류로 체크할지 여부 : true=Null은 오류, false=Null은 숫자
     * @return
     */
    @SuppressWarnings("unused")
    public static boolean isNumber(String sValue, boolean isNullError) {
        boolean bIsNumber = false;
        if(StringUtils.isEmpty(sValue)) {
            if(isNullError) {
                bIsNumber = false;
            } else {
                bIsNumber = true;
            }
        } else {
            try {
                sValue = sValue.replaceAll(",", "");
                BigDecimal bdValue = new BigDecimal(sValue);
                bIsNumber = true;
            } catch(NullPointerException npe) {
                bIsNumber = false;
            } catch(NumberFormatException nfe) {
                bIsNumber = false;
            }
        }
        
        return bIsNumber;
    }
    
    /**
     * 해당 문자열을 숫자형식으로 반환.
     * <BR/>숫자가 아닐 경우, isNullToZero값에 따라서 다른 값을 반환한다 : isNullToZero=true(0), isNullToZero=false(null)
     * @param oValue 변환할 문자열
     * @param isNullToZero Null인 경우에 "0"으로 변환하는지 여부.@param iDigit 표현할 소수점 자리수. 기본값은 전부 표현(999). ex) 1(1.234 -> 1.2), 2(1.235 -> 1.24), -1(12.34 -> 10)
     * @param roundingMode 표현할 소수점 자리수의 처리 방안 : RoundingMode.HALF_UP(반올림, Default), RoundingMode.HALF_DOWN(반올림과 유사하지만, 중간일 경우 버림)
     *                                                  , RoundingMode.CEILING(양수 무한대로 올림), RoundingMode.FLOOR(음수 무한대로 내림)
     *                                                  , RoundingMode.DOWN(0으로 내림), RoundingMode.UP(0을 벗어나는 방향으로 올림/내림)
     * @param dMultiplyNum 곱하기할 값. 보통 1로 넣고 사용하지 않는다.
     * @return
     */
    public static BigDecimal getNumber(String sValue, boolean isNullToZero, int iDigit, RoundingMode roundingMode, double dMultiplyNum) {
        BigDecimal bdValue = null;
        
        if(!isNumber(sValue, false)) {
            if(isNullToZero) {
                bdValue = BigDecimal.ZERO;
            } else {
                bdValue = null;
            }
        }

        try {
            sValue = sValue.replaceAll(",", "");
            bdValue = new BigDecimal(sValue);
            bdValue = bdValue.multiply(new BigDecimal(dMultiplyNum));
            if(iDigit != 999) {
                if(roundingMode == null) {
                    roundingMode = RoundingMode.HALF_UP;
                }
                bdValue = bdValue.setScale(iDigit, roundingMode);
            }
            
        } catch(NullPointerException npe) {
            if(isNullToZero) {
                bdValue = BigDecimal.ZERO;
            } else {
                bdValue = null;
            }
        } catch(NumberFormatException nfe) {
            if(isNullToZero) {
                bdValue = BigDecimal.ZERO;
            } else {
                bdValue = null;
            }
        }
        
        return bdValue;
    }
}

