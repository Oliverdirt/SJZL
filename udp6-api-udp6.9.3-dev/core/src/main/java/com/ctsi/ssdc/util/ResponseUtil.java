package com.ctsi.ssdc.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

/**
 * 创建 ResponseEntity
 */
public interface ResponseUtil {

    /**
     * 将可选项封装到具有{@link HttpStatus＃OK}状态的{@link ResponseEntity}中，
     * 如果它为空，则返回具有{@link HttpStatus＃NOT_FOUND}的{@link ResponseEntity}。
     * @param <X> 响应类型
     * @param maybeResponse 响应（如果存在）
     * @return 如果存在则返回包含{@code maybeResponse}的响应，否则返回{@link HttpStatus＃NOT_FOUND}
     */
    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse) {
        return wrapOrNotFound(maybeResponse, null);
    }

    /**
     * 将可选项封装到具有{@link HttpStatus＃OK}状态的{@link ResponseEntity}中，
     * 如果它为空，则返回具有{@link HttpStatus＃NOT_FOUND}的{@link ResponseEntity}。
     * @param <X> 响应类型
     * @param maybeResponse 响应（如果存在）
	 * @param header 响应头
     * @return 如果存在则返回包含{@code maybeResponse}的响应，否则返回{@link HttpStatus＃NOT_FOUND}
     */
    public static <X> ResponseEntity<X> wrapOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return maybeResponse.map(response -> ResponseEntity.ok().headers(header).body(response))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
