package com.ydh.redsheep.netty.netty.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
*
* @author : yangdehong
* @date : 2019-10-17 20:57
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageBO implements Serializable {
    private Integer id;
    private String content;
}
