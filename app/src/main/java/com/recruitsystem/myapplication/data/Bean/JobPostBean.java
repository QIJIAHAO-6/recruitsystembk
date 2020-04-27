package com.recruitsystem.myapplication.data.Bean;

public class JobPostBean {
    private Integer postId;
    private String postName;
    private String company;
    private String salaryRange;
    private String postDescription;
    private String postLimitation;
    private String postType;
    private String postReleaseType;
    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostLimitation() {
        return postLimitation;
    }

    public void setPostLimitation(String postLimitation) {
        this.postLimitation = postLimitation;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostReleaseType() {
        return postReleaseType;
    }

    public void setPostReleaseType(String postReleaseType) {
        this.postReleaseType = postReleaseType;
    }

    @Override
    public String toString() {
        return "JobPostBean{" +
                "postId=" + postId +
                ", postName='" + postName + '\'' +
                ", company='" + company + '\'' +
                ", salaryRange='" + salaryRange + '\'' +
                ", postDescription='" + postDescription + '\'' +
                ", postLimitation='" + postLimitation + '\'' +
                ", postType='" + postType + '\'' +
                ", postReleaseType='" + postReleaseType + '\'' +
                '}';
    }
}
