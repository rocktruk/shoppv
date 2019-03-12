package com.online.mall.shoppv.trans.bean;

public class OrderStatusUpdateResponse {

	private int status;
	
	private String msg;
	
	private String data;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	class Details {
		
		private String order_number;
		
		private String app_id;
		
		private String open_userid;

		public String getOrder_number() {
			return order_number;
		}

		public void setOrder_number(String order_number) {
			this.order_number = order_number;
		}

		public String getApp_id() {
			return app_id;
		}

		public void setApp_id(String app_id) {
			this.app_id = app_id;
		}

		public String getOpen_userid() {
			return open_userid;
		}

		public void setOpen_userid(String open_userid) {
			this.open_userid = open_userid;
		}
		
		
		
		
	}
	
}
