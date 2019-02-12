package com.dongzhi.ow.util;

/**
 * @ClassName:     Result.java
 * @Description:   前台访问接口统一返回标准
 * @author         dongzhi
 * @version        V1.0  
 * @Date           2019年2月12日 下午10:43:37
 */
public class Result {

	 public static int SUCCESS_CODE = 0;//成功状态码
	 public static int FAIL_CODE = 1;//失败状态码
	 
	 int code;
	 String message;
	 Object data;
	 
	 private Result(int code, String message, Object data) {
		 this.code = code;
		 this.message = message;
		 this.data = data;
	 }
	 
	 public static Result success() {
		return new Result(SUCCESS_CODE, null, null);
	 }
	 public static Result success(Object data) {
			return new Result(SUCCESS_CODE, "", data);
	 }
	 public static Result fail(String message) {
			return new Result(FAIL_CODE, message, null);
	 }
	 public static Result fail(String message, Object data) {
			return new Result(FAIL_CODE, message, data);
	 }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
