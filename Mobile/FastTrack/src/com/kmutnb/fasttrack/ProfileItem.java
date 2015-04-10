package com.kmutnb.fasttrack;

public class ProfileItem {

	private String item_title;
	private String item_value;
	
	
	public ProfileItem(String item_title, String item_value) {
		super();
		this.item_title = item_title;
		this.item_value = item_value;
	}
	public String getItem_title() {
		return item_title;
	}
	public void setItem_title(String item_title) {
		this.item_title = item_title;
	}
	public String getItem_value() {
		return item_value;
	}
	public void setItem_value(String item_value) {
		this.item_value = item_value;
	}
	
}
