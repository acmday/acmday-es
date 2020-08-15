package com.acmday.springboot.es.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author acmday.
 * @date 2020/8/10.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    /**
     * 学号
     */
    private Integer id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 年龄
     */
    private Byte age;
}
