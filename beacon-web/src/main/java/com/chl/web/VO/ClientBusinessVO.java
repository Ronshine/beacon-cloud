package com.chl.web.VO;

/**
 * 客户响应对象
 */
public class ClientBusinessVO {
    private Long id;
    private String corpname;

    public ClientBusinessVO() {
    }

    public ClientBusinessVO(Long id, String corpname) {
        this.id = id;
        this.corpname = corpname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorpname() {
        return corpname;
    }

    public void setCorpname(String corpname) {
        this.corpname = corpname;
    }

    @Override
    public String toString() {
        return "ClientBusinessVO{" +
                "id=" + id +
                ", corpname='" + corpname + '\'' +
                '}';
    }
}
