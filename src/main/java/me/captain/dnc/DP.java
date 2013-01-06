package me.captain.dnc;

import com.avaje.ebean.validation.NotEmpty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "disp_names")
public class DP
{
	
	@Id
	@GeneratedValue
	private int id;
	
	@NotEmpty
	private String PlayerName;
	
	@NotEmpty
	private String DisplayName;
	
	public String getDisplayName()
	{
		return this.DisplayName;
	}
	
	public void setDisplayName(String DisplayName)
	{
		this.DisplayName = DisplayName;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public String getPlayerName()
	{
		return this.PlayerName;
	}
	
	public void setPlayerName(String PlayerName)
	{
		this.PlayerName = PlayerName;
	}
}