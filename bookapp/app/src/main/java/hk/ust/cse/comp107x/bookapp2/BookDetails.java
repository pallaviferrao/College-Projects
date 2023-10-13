package hk.ust.cse.comp107x.bookapp2;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Padhavi on 05-07-2016.
 */
public class BookDetails extends AppCompatActivity {

    private String bookName,authorName,isbnNo,description,owner;
    private String downloadUrl;



    public BookDetails() {
    }

    public BookDetails(String bookName, String authorName, String isbnNo, String description,String downloadUrl,String owner) {
        this.bookName = bookName;
        this.authorName = authorName;
        this.isbnNo = isbnNo;
        this.description = description;
        this.downloadUrl=downloadUrl;
        this.owner=owner;
    }

    public String getBookName(){
        return bookName;
    }
    public String getAuthorName(){
        return authorName;
    }
    public String getIsbnNo(){
        return isbnNo;
    }
    public String getDescription(){
        return description;
    }
    public String getDownloadUrl()
    {
        return downloadUrl;
    }
    public String getOwner()
    {
        return owner;
    }
}
