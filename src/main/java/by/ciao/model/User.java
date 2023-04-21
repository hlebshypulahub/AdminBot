package by.ciao.model;

public class User {

    private String id;
    private String username;
    private String fullName;
    private String referral;
    private String phone;
    private String englishLvl;
    private String numOfCorrectAnswers;
    private String testCompletionDate;

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", fullName='" + fullName + '\'' +
                ", referral='" + referral + '\'' +
                ", phone='" + phone + '\'' +
                ", englishLvl='" + englishLvl + '\'' +
                ", numOfCorrectAnswers='" + numOfCorrectAnswers + '\'' +
                ", testCompletionDate='" + testCompletionDate + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getReferral() {
        return referral;
    }

    public void setReferral(String referral) {
        this.referral = referral;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEnglishLvl() {
        return englishLvl;
    }

    public void setEnglishLvl(String englishLvl) {
        this.englishLvl = englishLvl;
    }

    public String getNumOfCorrectAnswers() {
        return numOfCorrectAnswers;
    }

    public void setNumOfCorrectAnswers(String numOfCorrectAnswers) {
        this.numOfCorrectAnswers = numOfCorrectAnswers;
    }

    public String getTestCompletionDate() {
        return testCompletionDate;
    }

    public void setTestCompletionDate(String testCompletionDate) {
        this.testCompletionDate = testCompletionDate;
    }
}
