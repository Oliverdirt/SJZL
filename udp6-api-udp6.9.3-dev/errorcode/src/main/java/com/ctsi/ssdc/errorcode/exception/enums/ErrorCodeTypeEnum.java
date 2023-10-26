package com.ctsi.ssdc.errorcode.exception.enums;

import com.ctsi.ssdc.errorcode.exception.IntArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 错误码的类型枚举
 *
 * @author hx
 */
@AllArgsConstructor
@Getter
public enum ErrorCodeTypeEnum implements IntArrayValuable {

    /**
     * 自动生成
     */
    AUTO_GENERATION(1),
    /**
     * 手动编辑
     */
    MANUAL_OPERATION(2);

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ErrorCodeTypeEnum::getCodeType).toArray();

    /**
     * 类型
     */
    public final Integer codeType;

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
