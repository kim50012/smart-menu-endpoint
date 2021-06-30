package com.basoft.core.ware.wechat.domain.group;

public class Group {
	/**
	 * 分组id，由微信分配
	 */
	private Integer id;

	/**
	 * 分组名字（30个字符以内），UTF8编码
	 */
	private String name;

	/**
	 * 分组内用户数量
	 */
	private Integer count;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Group [id=" + id + ", name=" + name + ", count=" + count + "]";
	}
}
