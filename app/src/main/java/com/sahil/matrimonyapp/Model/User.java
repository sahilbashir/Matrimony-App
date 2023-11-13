package com.sahil.matrimonyapp.Model;

import android.os.Parcelable;

import androidx.dynamicanimation.animation.SpringAnimation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class User {

    private String userID ;
    private String email ;
    private String password ;
    private String imageUrl ;
    private String fullName ;
    private String profession ;
    private String adress ;
    private String birthDate ;
    private int age ;
    private String gender ;
    private String number ;
    private boolean isMarried ;
    private String bio ;


    private List<Integer> hobbies ;

    private List<String> imagesUser ;


    public User(){

    }

    public User(String email, String password, String imageUrl, String fullName, String profession,
                String adress, String birthDate, int age, String gender, String number, boolean isMarried) {
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        this.profession = profession;
        this.adress = adress;
        this.birthDate = birthDate;
        this.age = age;
        this.gender = gender;
        this.number = number;
        this.isMarried = isMarried;
    }

    public User(String email, String password, String imageUrl, String fullName, String profession, String adress, String birthDate, int age, String gender,
                String number, boolean isMarried, String image1, String image2, String image3, String image4) {
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.fullName = fullName;
        this.profession = profession;
        this.adress = adress;
        this.birthDate = birthDate;
        this.age = age;
        this.gender = gender;
        this.number = number;
        this.isMarried = isMarried;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isMarried() {
        return isMarried;
    }

    public void setMarried(boolean married) {
        isMarried = married;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Integer> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<Integer> hobbies) {
        this.hobbies = hobbies;
    }

    public List<String> getImagesUser() {
        return imagesUser;
    }

    public void setImagesUser(List<String> imagesUser) {
        this.imagesUser = imagesUser;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
