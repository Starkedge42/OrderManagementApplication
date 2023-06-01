/***
 * We are creating our own message response structure instead of using
 * default error response provided by Spring Boot.
 */

package com.ordermanagement.apigateway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ErrorMessage {

    private int statusCode;
    private Date timestamp;
    private String message;
    private String description;
}
