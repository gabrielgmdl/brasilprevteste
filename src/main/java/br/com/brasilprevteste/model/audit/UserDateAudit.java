package br.com.brasilprevteste.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdBy", "updatedBy"}, allowGetters = true)
public abstract class UserDateAudit extends DateAudit {

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdByUser;

    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedByUser;

    @PrePersist
    public void prePersist() {
        String createdByUser = getUsernameOfAuthenticatedUser();
        this.createdByUser = createdByUser;
        this.modifiedByUser = createdByUser;
    }

    @PreUpdate
    public void preUpdate() {
        this.modifiedByUser = getUsernameOfAuthenticatedUser();
    }

    private String getUsernameOfAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {
            return "Sistema";
        }
        return authentication.getName();
    }

}