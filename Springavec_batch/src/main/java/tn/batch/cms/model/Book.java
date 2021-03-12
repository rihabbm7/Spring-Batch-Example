package tn.batch.cms.model;

public class Book {
	
	 private String id;
	    private String title;
	    private String author;
	    private String releaseDate;
	    
	    
	    
	    
	    
		public Book() {
			
		}
		
		public Book(String id, String title, String author, String releaseDate) {
			super();
			this.id = id;
			this.title = title;
			this.author = author;
			this.releaseDate = releaseDate;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getAuthor() {
			return author;
		}
		public void setAuthor(String author) {
			this.author = author;
		}
		public String getReleaseDate() {
			return releaseDate;
		}
		public void setReleaseDate(String releaseDate) {
			this.releaseDate = releaseDate;
		}
	    
	    
	    
}
