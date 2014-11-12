package com.acctrue.tts.enums;

public enum StoreTypeInfoEnum {
	ProduceIn(1,"生产入库"),
	ReworkOut(2,"返工出库"),
	PurchaseIn(3,"采购入库"),
	ReturnOut(4,"退货出库"),
	SaleOut(5,"销售出库"),
	ReturnIn(6,"退货入库"),
	SaleOutX(7,"零售出库"),
	ReturnInX(8,"零售退货"),
	AllocateIn(9,"调拨入库"),
	AllocateOut(10,"调拨出库"),
	CheckIn(11,"盘点盈余"),
	CheckOut(12,"盘点亏损"),
	DestroyOut(13,"销毁出库"),
	TestingOut(14,"取检出库"),
	MissingOut(15,"报失出库"),
	OtherIn(16,"其它入库"),
	OtherOut(17,"其它出库");
	
	private int id;
	private String name;

	private StoreTypeInfoEnum(int id, String name) {
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
