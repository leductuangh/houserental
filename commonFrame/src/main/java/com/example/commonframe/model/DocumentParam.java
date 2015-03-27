package com.example.commonframe.model;

import com.example.commonframe.model.base.BaseParam;
import com.example.commonframe.util.Constant.RequestFormat;

public class DocumentParam extends BaseParam {

	public DocumentParam(RequestFormat format) {
		super(format);
	}

	@Override
	public byte[] makeRequestBody() {
		return null;
	}

	@Override
	protected byte[] makeJsonBody() {
		return null;
	}

	@Override
	protected byte[] makeXmlBody() {
		return null;
	}

	@Override
	protected byte[] makeStringBody() {
		return null;
	}

}
