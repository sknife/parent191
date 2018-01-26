package cn.itcast.core.bean.product;

import java.io.Serializable;

/**
 * Created by huyongjin on 2017/1/6.
 */
public class BrandQuery implements Serializable {
    private static final long serialVersionUID = 2708704362261204738L;
    //品牌id bigint
    private Long id;
    //品牌名称
    private String name;
    //描述
    private String description;
    //图片地址
    private String img_url;
    //网址
    private String web_site;
    //排序 越大越前
    private Integer sort;
    //是否可用 0不可用，1可用 tinyint
    private Integer isDisplay;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getWeb_site() {
        return web_site;
    }

    public void setWeb_site(String web_site) {
        this.web_site = web_site;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }


    public Integer getIsDisplay() {
		return isDisplay;
	}

	public void setIsDisplay(Integer isDisplay) {
		this.isDisplay = isDisplay;
	}

	@Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", img_url='" + img_url + '\'' +
                ", web_site='" + web_site + '\'' +
                ", sort=" + sort +
                ", isDisplay=" + isDisplay +
                '}';
    }

    //附加字段
    //当前页
    private Integer pageNo = 1;
    //每页条数
    private Integer pageSize = 10;
    //开始行
    private Integer startRow;

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        //计算开始行
        this.startRow = (pageNo - 1) * pageSize;
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        //计算开始行
        this.startRow = (pageNo - 1) * pageSize;
        this.pageNo = pageNo;
    }

}
