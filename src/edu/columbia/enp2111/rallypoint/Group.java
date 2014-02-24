package edu.columbia.enp2111.rallypoint;

public class Group
{
    public String group_id;
    public String owner_email;
    public String network;
    public String start_location;
    public String end_location;
    public String departure_date_time;
    public String direction;
    public String member_1;
    public String member_2;
    public String member_3;
    public String created_at;
	public String updated_at;

	/**
	 * Constructor with all params.
	 */
	public Group(String groupId, String ownerEmail, String network, 
			String startLocation, String endLocation, String dateTime,
			String direction, String member1, String member2, String member3,
			String createdAt, String updatedAt)
	{
		this.group_id = groupId;
		this.owner_email = ownerEmail;
		this.network = network;
		this.start_location = startLocation;
		this.end_location = endLocation;
		this.departure_date_time = dateTime;
		this.direction = direction;
		this.member_1 = member1;
		this.member_2 = member2;
		this.member_3 = member3;
		this.created_at = createdAt;
		this.updated_at = updatedAt;
	}
	
	/** Constructor without member IDs. */
	public Group(String groupId, String ownerEmail, String network, 
			String startLocation, String endLocation, String dateTime,
			String direction, String createdAt, String updatedAt)
	{
		this(groupId, ownerEmail, network, startLocation, endLocation,
				dateTime, direction, null, null, null, createdAt, updatedAt);
	}
	
	public String getGroupId()
	{
		return group_id;
	}
	
	public String getOwnerEmail()
	{
		return owner_email;
	}

	/** Returns all the instance variables of this group. */
	public String toString()
	{
		return "Group ID: " + group_id + " Owner email: " + owner_email + 
				" | network: " + network + " | Start location: " + 
				start_location + " | End location: " + end_location +
				" | Departure date/time: " + departure_date_time + 
				" | Direction " + direction + " | Member 1: " + member_1 +
				" | Member 2: " + member_2 + " | Member 3: " + member_3 + 
				" | Created at: " + created_at + " | Updated at: " + 
				updated_at + ".";
	}
}
