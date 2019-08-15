package com.trackray.module.inner.codeAudit;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Rule {
	private String name;
	private String id;
	private String regexp;
	private String decription;
	private String recommendation;
	private String reference;
	private boolean where;
	private String filenameReg;
	private String contentReg;
}
