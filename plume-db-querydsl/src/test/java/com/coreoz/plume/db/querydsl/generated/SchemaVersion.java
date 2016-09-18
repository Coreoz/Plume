package com.coreoz.plume.db.querydsl.generated;

import javax.annotation.Generated;
import com.querydsl.sql.Column;

/**
 * SchemaVersion is a Querydsl bean type
 */
@Generated("com.coreoz.plume.db.querydsl.generation.IdBeanSerializer")
public class SchemaVersion {

    @Column("checksum")
    private Integer checksum;

    @Column("description")
    private String description;

    @Column("execution_time")
    private Integer executionTime;

    @Column("installed_by")
    private String installedBy;

    @Column("installed_on")
    private java.time.LocalDateTime installedOn;

    @Column("installed_rank")
    private Integer installedRank;

    @Column("script")
    private String script;

    @Column("success")
    private Boolean success;

    @Column("type")
    private String type;

    @Column("version")
    private String version;

    public Integer getChecksum() {
        return checksum;
    }

    public void setChecksum(Integer checksum) {
        this.checksum = checksum;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Integer executionTime) {
        this.executionTime = executionTime;
    }

    public String getInstalledBy() {
        return installedBy;
    }

    public void setInstalledBy(String installedBy) {
        this.installedBy = installedBy;
    }

    public java.time.LocalDateTime getInstalledOn() {
        return installedOn;
    }

    public void setInstalledOn(java.time.LocalDateTime installedOn) {
        this.installedOn = installedOn;
    }

    public Integer getInstalledRank() {
        return installedRank;
    }

    public void setInstalledRank(Integer installedRank) {
        this.installedRank = installedRank;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (installedRank == null) {
            return super.equals(o);
        }
        if (!(o instanceof SchemaVersion)) {
            return false;
        }
        SchemaVersion obj = (SchemaVersion) o;
        return installedRank.equals(obj.installedRank);
    }

    @Override
    public int hashCode() {
        if (installedRank == null) {
            return super.hashCode();
        }
        final int prime = 31;
        int result = 1;
        result = prime * result + installedRank.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SchemaVersion#" + installedRank;
    }

}

