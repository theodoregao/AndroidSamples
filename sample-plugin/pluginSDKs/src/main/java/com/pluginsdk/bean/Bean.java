/**
 * User.java
 * com.youku.pluginsdk.bean
 * <p/>
 * Function�� TODO
 * <p/>
 * ver     date      		author
 * ��������������������������������������������������������������������
 * 2014-10-20 		Administrator
 * <p/>
 * Copyright (c) 2014, TNT All Rights Reserved.
 */

package com.pluginsdk.bean;


/**
 * ClassName:User
 *
 * @author jiangwei
 * @version
 * @since Ver 1.1
 * @Date 2014-10-20		����1:35:16
 */
public class Bean implements com.pluginsdk.interfaces.IBean {

    /**
     *
     */
    private String name = "���������ڲ�����������õĳ�ʼ��������";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

