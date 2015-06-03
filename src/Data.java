import java.math.BigDecimal;
import java.sql.Timestamp;

public class Data {

	private BigDecimal UtcTime;
	private int TemValue;
	private int RoadID;
	private BigDecimal mmX;
	private BigDecimal mmY;
	private BigDecimal speed;
	private BigDecimal PointPos;
	private Boolean DigDirect;
	private float newspeed;
	private int VehicleID;
	private Timestamp SysTime;

	public BigDecimal getSpeed() {
		return speed;
	}

	public void setSpeed(BigDecimal speed) {
		this.speed = speed;
	}

	public Timestamp getSysTime() {
		return SysTime;
	}

	public void setSysTime(Timestamp sysTime) {
		SysTime = sysTime;
	}

	public float getNewspeed() {
		return newspeed;
	}

	public void setNewspeed(float newspeed) {
		this.newspeed = newspeed;
	}

	public int getVehicleID() {
		return VehicleID;
	}

	public void setVehicleID(int vehicleID) {
		VehicleID = vehicleID;
	}

	public BigDecimal getUtcTime() {
		return UtcTime;
	}

	public void setUtcTime(BigDecimal utcTime) {
		UtcTime = utcTime;
	}

	public int getTemValue() {
		return TemValue;
	}

	public void setTemValue(int temValue) {
		TemValue = temValue;
	}

	public int getRoadID() {
		return RoadID;
	}

	public void setRoadID(int roadID) {
		RoadID = roadID;
	}

	public BigDecimal getMmX() {
		return mmX;
	}

	public void setMmX(BigDecimal mmX) {
		this.mmX = mmX;
	}

	public BigDecimal getMmY() {
		return mmY;
	}

	public void setMmY(BigDecimal mmY) {
		this.mmY = mmY;
	}

	public BigDecimal getPointPos() {
		return PointPos;
	}

	public void setPointPos(BigDecimal pointPos) {
		PointPos = pointPos;
	}

	public Boolean getDigDirect() {
		return DigDirect;
	}

	public void setDigDirect(Boolean digDirect) {
		DigDirect = digDirect;
	}

	@Override
	public String toString() {
		return "Data [UtcTime=" + UtcTime + ", TemValue=" + TemValue
				+ ", RoadID=" + RoadID + ", mmX=" + mmX + ", mmY=" + mmY
				+ ", speed=" + speed + ", PointPos=" + PointPos
				+ ", DigDirect=" + DigDirect + ", newspeed=" + newspeed
				+ ", VehicleID=" + VehicleID + ", SysTime=" + SysTime + "]";
	}

}
