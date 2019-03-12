package com.online.mall.shoppv.trans.bean;

public class PaymentNotifyResponse {

	
	private int status;
	
	private String msg;
	
	private Detail data;

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

	public Detail getData() {
		return data;
	}

	public void setData(Detail data) {
		this.data = data;
	}
	
	class Detail {
		
		private String source;
		
		private String res_msg;
		
		private String order_number;
		
		private String open_userid;
		
		private String res_status;

		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getRes_msg() {
			return res_msg;
		}

		public void setRes_msg(String res_msg) {
			this.res_msg = res_msg;
		}

		public String getOrder_number() {
			return order_number;
		}

		public void setOrder_number(String order_number) {
			this.order_number = order_number;
		}

		public String getOpen_userid() {
			return open_userid;
		}

		public void setOpen_userid(String open_userid) {
			this.open_userid = open_userid;
		}

		public String getRes_status() {
			return res_status;
		}

		public void setRes_status(String res_status) {
			this.res_status = res_status;
		}
		
		
	}
	
}
