package tn.batch.cms.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import tn.batch.cms.model.Book;

public class BookItemProcessor implements ItemProcessor<Book, Book> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BookItemProcessor.class);

	//trying to change/manipulate the data inside the processor to send it to writer processed
	@Override
	public Book process(final Book book) throws Exception {
		String id = book.getId();
        String title = book.getTitle().toLowerCase();
        String author = book.getAuthor().toUpperCase();

        Book transformedBook = new Book(id,title, author, book.getReleaseDate());
        LOGGER.info("Converting ( {} ) into ( {} )", book, transformedBook);

        return transformedBook;
	}
	
	

}
