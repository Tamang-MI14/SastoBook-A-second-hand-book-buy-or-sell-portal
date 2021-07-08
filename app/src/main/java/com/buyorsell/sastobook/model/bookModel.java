package com.buyorsell.sastobook.model;

public class bookModel {
    String BookId, BookCategory, BookName, BookPrice, ImageUrl, BookOwner, userId;

    public bookModel() {
    }

    public bookModel(String bookId, String bookCategory, String bookName, String bookPrice, String imageUrl, String bookOwner, String userId) {
        BookId = bookId;
        BookCategory = bookCategory;
        BookName = bookName;
        BookPrice = bookPrice;
        ImageUrl = imageUrl;
        BookOwner = bookOwner;
        this.userId = userId;
    }

    public String getBookId() {
        return BookId;
    }

    public void setBookId(String bookId) {
        BookId = bookId;
    }

    public String getBookCategory() {
        return BookCategory;
    }

    public void setBookCategory(String bookCategory) {
        BookCategory = bookCategory;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public String getBookPrice() {
        return BookPrice;
    }

    public void setBookPrice(String bookPrice) {
        BookPrice = bookPrice;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getBookOwner() {
        return BookOwner;
    }

    public void setBookOwner(String bookOwner) {
        BookOwner = bookOwner;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}