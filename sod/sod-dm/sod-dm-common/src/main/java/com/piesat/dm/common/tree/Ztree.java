package com.piesat.dm.common.tree;

public class Ztree {
	private String id;
	private String pId;
	private String name;
	private boolean isParent;
	private boolean open;
	private Integer canEdit;

	public Ztree() {
		super();
	}

	public Ztree(String id, String pId, String name, boolean isParent) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.isParent = isParent;
	}

	public Ztree(String id, String pId, String name, boolean isParent, Integer canEdit) {
		super();
		this.id = id;
		this.pId = pId;
		this.name = name;
		this.isParent = isParent;
		this.canEdit = canEdit;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

	public Integer getCanEdit() {
		return canEdit;
	}

	public void setCanEdit(Integer canEdit) {
		this.canEdit = canEdit;
	}
}
