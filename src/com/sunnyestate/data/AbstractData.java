package com.sunnyestate.data;

import java.io.Serializable;

import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractData extends BaseData implements IData,
		Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	public static enum Status {
		NEW, OLD, UPDATE, DEL
	};

	protected Status status = Status.NEW;

	@Override
	public void read(SQLiteDatabase db) {
		return;
	}

	@Override
	public void write(SQLiteDatabase db) {
		return;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

}
