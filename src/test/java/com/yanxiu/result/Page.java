package com.yanxiu.result;

public class Page {
	private int pageNum;
	private final int sizeofEachPage = 5;

	public int getSizeofEachPage() {
		return sizeofEachPage;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
}
