package org.socialsignin.spring.data.dynamodb.domain.sample;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * Domain class for auditing functionality testing.
 *
 * @author Vito Limandibhrata
 */
@DynamoDBTable(tableName = "auditableUser")
public class AuditableUser {

    @DynamoDBHashKey
    @DynamoDBAutoGeneratedKey
    private String id;

    private String firstname;

    @CreatedBy
    private AuditableUser createdBy;

    @CreatedDate
    private Date createdAt;

    public AuditableUser() {}

    public AuditableUser(String firstName) {
        this.firstname = firstName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstName) {
        this.firstname = firstName;
    }

    public AuditableUser getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(AuditableUser createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

}
