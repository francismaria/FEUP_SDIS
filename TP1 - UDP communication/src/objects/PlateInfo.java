package objects;

public class PlateInfo {

	private String plateNumber, ownerName;
	
	public PlateInfo(String plateNumber, String ownerName){
		this.plateNumber = plateNumber;
		this.ownerName = ownerName;
	}
	
	public String getPlateNumber(){
		return plateNumber;
	}
	
	public String getOwnerName(){
		return ownerName;
	}
}
