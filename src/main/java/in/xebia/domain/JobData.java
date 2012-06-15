package in.xebia.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class JobData {
	
	@Id
	private Long id;
	
	@Column(unique=true)
	private String jobName;
	
	@Column(length=2000)
	private String jobDescription;
	
	private String recipients;
	
	private Long repeatInterval;
	
	private Date startDateTime;
	
	private Boolean active;
	
	public JobData(){
		
	}
	public JobData(String jobName, String recipients, Long repeatInterval,
			Date startDateTime, Boolean active) {
		super();
		this.jobName = jobName;
		this.recipients = recipients;
		this.repeatInterval = repeatInterval;
		this.startDateTime = startDateTime;
		this.active = active;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getRecipients() {
		return recipients;
	}

	public void setRecipients(String recipients) {
		this.recipients = recipients;
	}

	public Long getRepeatInterval() {
		return repeatInterval;
	}

	public void setRepeatInterval(Long repeatInterval) {
		this.repeatInterval = repeatInterval;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
	public Boolean getActive() {
		return active;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	
}
