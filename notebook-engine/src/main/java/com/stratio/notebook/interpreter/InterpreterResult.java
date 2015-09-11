package com.stratio.notebook.interpreter;

import java.io.Serializable;

public class InterpreterResult implements Serializable{
	public static enum Code {
		SUCCESS,
		INCOMPLETE,
		ERROR
	}
	
	public static enum Type {
		TEXT,
		HTML,
		TABLE,
		IMG,
		SVG,
		NULL
	}
	
	Code code;
	Type type;
	String msg;

	public InterpreterResult(Code code) {
		this.code = code;
		this.msg = null;
		this.type = Type.TEXT;
	}


	public InterpreterResult(Code code, String msg) {
		this.code = code;
		this.msg = getData(msg);
		this.type = getType(msg);
	}
	


	/**
	 * Magic is like
	 * %html
	 * %text
	 * ...
	 * @param msg
	 * @return
	 */
	private String getData(String msg){
		if(msg==null) return null;
		
		Type[] types = Type.values();
		for(Type t : types) {
			String magic = "%"+t.name().toLowerCase();
			if(msg.startsWith(magic+" ") || msg.startsWith(magic+"\n")){
				int magicLength = magic.length()+1;
				if(msg.length()>magicLength){
					return msg.substring(magicLength);
				} else {
					return "";
				}
			}
		}
		
		return msg;
	}
	
	
	private Type getType(String msg){
		if(msg==null) return Type.TEXT;
		Type[] types = Type.values();
		for(Type t : types) {
			String magic = "%"+t.name().toLowerCase();
			if(msg.startsWith(magic+" ") || msg.startsWith(magic+"\n")){
				return t;
			}
		}
		return Type.TEXT;
	}
	
	public Code code(){
		return code;
	}
	
	public String message(){
		return msg;
	}
	
	public Type type(){
		return type;
	}
	
	public InterpreterResult type(Type type){
		this.type = type;
		return this;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		InterpreterResult that = (InterpreterResult) o;

		if (code != that.code) return false;
		if (type != that.type) return false;
		return !(msg != null ? !msg.equals(that.msg) : that.msg != null);

	}

	@Override
	public int hashCode() {
		int result = code != null ? code.hashCode() : 0;
		result = 31 * result + (type != null ? type.hashCode() : 0);
		result = 31 * result + (msg != null ? msg.hashCode() : 0);
		return result;
	}


}
