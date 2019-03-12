package com.online.mall.shoppv.trans.bean;

public class CreateOrderResponse {

	
	private int status;
	
	private String msg;
	
	private OrderDetail data;

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

	public OrderDetail getData() {
		return data;
	}

	public void setData(OrderDetail data) {
		this.data = data;
	}
	
	
	class OrderDetail {
		
		private String source;
		
		private String app_id;
		
		private String open_userid;
		
		private String out_order_number;
		
		private String type_status;
		
		private String city_codes;
		
		private String consignee_address;
		
		private String order_title;
		
		private String total_amount;
		
		private String return_url;
		
		private String notify_url;
		
		private String order_number;
		
		private String push_type;

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
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

		public String getOut_order_number() {
			return out_order_number;
		}

		public void setOut_order_number(String out_order_number) {
			this.out_order_number = out_order_number;
		}

		public String getType_status() {
			return type_status;
		}

		public void setType_status(String type_status) {
			this.type_status = type_status;
		}

		public String getCity_codes() {
			return city_codes;
		}

		public void setCity_codes(String city_codes) {
			this.city_codes = city_codes;
		}

		public String getConsignee_address() {
			return consignee_address;
		}

		public void setConsignee_address(String consignee_address) {
			this.consignee_address = consignee_address;
		}

		public String getOrder_title() {
			return order_title;
		}

		public void setOrder_title(String order_title) {
			this.order_title = order_title;
		}

		public String getTotal_amount() {
			return total_amount;
		}

		public void setTotal_amount(String total_amount) {
			this.total_amount = total_amount;
		}

		public String getReturn_url() {
			return return_url;
		}

		public void setReturn_url(String return_url) {
			this.return_url = return_url;
		}

		public String getNotify_url() {
			return notify_url;
		}

		public void setNotify_url(String notify_url) {
			this.notify_url = notify_url;
		}

		public String getOrder_number() {
			return order_number;
		}

		public void setOrder_number(String order_number) {
			this.order_number = order_number;
		}

		public String getPush_type() {
			return push_type;
		}

		public void setPush_type(String push_type) {
			this.push_type = push_type;
		}
		
		
		
	}
	
}
