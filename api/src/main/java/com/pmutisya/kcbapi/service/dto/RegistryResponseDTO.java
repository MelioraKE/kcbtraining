package com.pmutisya.kcbapi.service.dto;

import java.io.Serializable;

public class RegistryResponseDTO implements Serializable {
    private String code;

    private String desc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "RegistryResponseDTO{" + "code='" + code + '\'' + ", desc='" + desc + '\'' + '}';
    }
}
