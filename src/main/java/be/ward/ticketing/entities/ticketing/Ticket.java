package be.ward.ticketing.entities.ticketing;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "ticket")
public class Ticket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "assigned_group")
    private String assignedGroup;

    @Column(name = "assigned_user")
    private String assignedUser;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "creator")
    private String creator;

    @Column(name = "description")
    private String description;

    @Column(name = "due_at")
    private Date dueAt;

    @Column(name = "lock_version")
    private Integer lockVersion;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @Column(name = "topic_text")
    private String topicText;

    @ManyToOne
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @ManyToOne
    @JoinColumn(name = "priority_id")
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "source_id")
    private Source source;

    @ManyToOne
    @JoinColumn(name = "ticket_type_id")
    private TicketType ticketType;

    public Ticket() {
    }

    public Ticket(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getAssignedGroup() {
        return assignedGroup;
    }

    public void setAssignedGroup(String assignedGroup) {
        this.assignedGroup = assignedGroup;
    }

    public String getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(String assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueAt() {
        return dueAt;
    }

    public void setDueAt(Date dueAt) {
        this.dueAt = dueAt;
    }

    public Integer getLockVersion() {
        return lockVersion;
    }

    public void setLockVersion(Integer lockVersion) {
        this.lockVersion = lockVersion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getTopicText() {
        return topicText;
    }

    void setTopicText(String topicText) {
        this.topicText = topicText;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public TicketType getTicketType() {
        return ticketType;
    }

    public void setTicketType(TicketType ticketType) {
        this.ticketType = ticketType;
    }
}