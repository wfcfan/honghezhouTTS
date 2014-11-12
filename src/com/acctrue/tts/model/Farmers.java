package com.acctrue.tts.model;

public class Farmers {
	private String farmerId;
	private String farmerName;

	public Farmers() {
		;
	}

	public Farmers(String fid, String fName) {
		this();
		this.farmerId = fid;
		this.farmerName = fName;
	}
	
	public Farmers(int fid,String fName){
		this(String.valueOf(fid),fName);
	}

	public String getFarmerId() {
		return farmerId;
	}

	public void setFarmerId(String farmerId) {
		this.farmerId = farmerId;
	}

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		if(obj instanceof Farmers){
			Farmers f = (Farmers)obj;
			return farmerId.equals(f.getFarmerId());
		}
		return false;
	}

}
