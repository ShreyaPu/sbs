package com.alphax.vo.mb;

/**
 * @author A106744104
 *
 */
public class BAWrapperDTO {

	private MasterDataBA_DTO ba40Object;
	private MasterDataBA_DTO ba44Object;
	private MasterDataBA_DTO ba45Object;
	private MasterDataBA_DTO ba49Object;
	private DeparturesBA_DTO ba09Object;
	
	public BAWrapperDTO() {
	
	}

	public MasterDataBA_DTO getBa40Object() {
		return ba40Object;
	}

	public void setBa40Object(MasterDataBA_DTO ba40Object) {
		this.ba40Object = ba40Object;
	}

	public MasterDataBA_DTO getBa44Object() {
		return ba44Object;
	}

	public void setBa44Object(MasterDataBA_DTO ba44Object) {
		this.ba44Object = ba44Object;
	}

	public MasterDataBA_DTO getBa45Object() {
		return ba45Object;
	}

	public void setBa45Object(MasterDataBA_DTO ba45Object) {
		this.ba45Object = ba45Object;
	}

	public MasterDataBA_DTO getBa49Object() {
		return ba49Object;
	}

	public void setBa49Object(MasterDataBA_DTO ba49Object) {
		this.ba49Object = ba49Object;
	}

	/**
	 * @return the ba09Object
	 */
	public DeparturesBA_DTO getBa09Object() {
		return ba09Object;
	}

	/**
	 * @param ba09Object the ba09Object to set
	 */
	public void setBa09Object(DeparturesBA_DTO ba09Object) {
		this.ba09Object = ba09Object;
	}
	
}