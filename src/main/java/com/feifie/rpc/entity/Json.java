package com.feifie.rpc.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author kevin
 * @date 2018/12/9
 * @since 0.1.0
 **/
@Getter
@Setter
public class Json implements Serializable {
    private Boolean success;
    private String msg;
    private Object obj;
}
