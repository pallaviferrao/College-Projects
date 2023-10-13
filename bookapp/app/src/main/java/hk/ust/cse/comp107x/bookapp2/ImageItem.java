package hk.ust.cse.comp107x.bookapp2;

/**
 * Created by Padhavi on 29-06-2016.
 */
/**
 * Created by Padhavi on 29-06-2016.
 */
public class ImageItem {

   // private Bitmap image;
    private String title;
    private String downloadUrl;
    private String bookId;
  //  private String bookIdKey;

    public ImageItem( String title,String downloadUrl,String bookId) {
        super();
       // this.image = image;
        this.downloadUrl=downloadUrl;
        this.title = title;
        this.bookId=bookId;
        //this.bookIdKey=bookIdKey;
    }

    /*public Bitmap getImage() {
        return image;
    }
*/

   /* public void setImage(Bitmap image) {
        this.image = image;
    }
*/
    public String getDownloadUrl(){
        return downloadUrl;
    }
    public String getTitle() {
        return title;
    }

    public String getBookId(){
        return bookId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
/*
    public String getBookIdKey(String bookIdKey){
        return this.bookIdKey=bookIdKey;
    }*/
}

