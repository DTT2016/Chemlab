package com.chemlab.objs;

public class Drug {

	//Base Drug information
	private String drug_name;
	private String drug_another_name;
	private String fen_zi_shi;
	private String counting;
	private String standard;
	

	public Drug() {
	}
	
	public void setBaseInfo(String drug_name,String drug_another_name,
	                        String fen_zi_shi,String counting,
	                        String standard) {
		this.drug_name = drug_name;
		this.drug_another_name = drug_another_name;
		this.fen_zi_shi = fen_zi_shi;
		this.counting = counting;
		this.standard = standard;
	}

	public String getDrug_name() {
		return drug_name;
	}

	public void setDrug_name(String drug_name) {
		this.drug_name = drug_name;
	}

	public String getDrug_another_name() {
		return drug_another_name;
	}

	public void setDrug_another_name(String drug_another_name) {
		this.drug_another_name = drug_another_name;
	}

	public String getFen_zi_shi() {
		return fen_zi_shi;
	}

	public void setFen_zi_shi(String fen_zi_shi) {
		this.fen_zi_shi = fen_zi_shi;
	}

	public String getCounting() {
		return counting;
	}

	public void setCounting(String counting) {
		this.counting = counting;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}
}
