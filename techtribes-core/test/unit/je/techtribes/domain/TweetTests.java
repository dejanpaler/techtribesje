package je.techtribes.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TweetTests {

    private ContentSource contentSource;
    private Tweet tweet;
    
    @Before
    public void setUp() throws Exception {
        contentSource = new Person();
        contentSource.setTwitterId("simonbrown");
    }

    @Test
    public void test_GetPermalink() {
        tweet = new Tweet(contentSource, 1234567890, "body", new Date());
        assertEquals("http://twitter.com/simonbrown/status/1234567890", tweet.getPermalink());
    }

    @Test
    public void test_getBodyAsHtml_HasNoEffect_ReturnsTheOriginalBodyWhenThereAreNoUrls() {
        tweet = new Tweet(contentSource, 1234567890, "Here is a string without hyperlinks", new Date());
        assertEquals("Here is a string without hyperlinks", tweet.getBodyAsHtml());
    }

    @Test
    public void test_getBodyAsHtml_CreatesAHtmlHyperlink_WhenTheStringContainsOneHttpUrl() {
        tweet = new Tweet(contentSource, 1234567890, "Here is a link http://www.google.com", new Date());
        assertEquals("Here is a link <a href=\"http://www.google.com\" target=\"_blank\">http://www.google.com</a>", tweet.getBodyAsHtml());
    }

    @Test
    public void test_getBodyAsHtml_CreatesAHtmlHyperlink_WhenTheStringContainsOneHttpsUrl() {
        tweet = new Tweet(contentSource, 1234567890, "Here is a link https://www.google.com", new Date());
        assertEquals("Here is a link <a href=\"https://www.google.com\" target=\"_blank\">https://www.google.com</a>", tweet.getBodyAsHtml());
    }

    @Test
    public void test_getBodyAsHtml_CreatesAHtmlHyperlink_WhenTheStringContainsMoreThanOneUrl() {
        tweet = new Tweet(contentSource, 1234567890, "Here is a link http://www.google.com and another http://www.yahoo.com", new Date());
        assertEquals("Here is a link <a href=\"http://www.google.com\" target=\"_blank\">http://www.google.com</a> and another <a href=\"http://www.yahoo.com\" target=\"_blank\">http://www.yahoo.com</a>", tweet.getBodyAsHtml());
    }

    @Test
    public void test_getBodyAsHtml_CreatesATwitterLink_WhenTheStringContainsOneAtTwitterId() {
        tweet = new Tweet(contentSource, 1234567890, "RT @techtribesje: blah blah blah", new Date());
        assertEquals("RT <a href=\"/twitter/techtribesje\">@techtribesje</a>: blah blah blah", tweet.getBodyAsHtml());
    }

    @Test
    public void test_getBodyAsHtml_CreatesATwitterLink_WhenTheStringContainMoreThanOneAtTwitterId() {
        tweet = new Tweet(contentSource, 1234567890, "RT @techtribesje: blah blah blah @simonbrown", new Date());
        assertEquals("RT <a href=\"/twitter/techtribesje\">@techtribesje</a>: blah blah blah <a href=\"/twitter/simonbrown\">@simonbrown</a>", tweet.getBodyAsHtml());
    }

    @Test
    public void test_getBodyAsHtml_IdentifiesAHashTag_WhenTheStringContainsAHashTag() {
        tweet = new Tweet(contentSource, 1234567890, "This is a #hashtag", new Date());
        assertEquals("This is a <a href=\"/search?q=%23hashtag\">#hashtag</a>", tweet.getBodyAsHtml());
    }

    @Test
    public void test_getBodyAsHtml_IdentifiesAHashTag_WhenTheStringContainsAHashTagSurroundedByDisallowedCharacters() {
        tweet = new Tweet(contentSource, 1234567890, "This is a (#hashtag)", new Date());
        assertEquals("This is a (<a href=\"/search?q=%23hashtag\">#hashtag</a>)", tweet.getBodyAsHtml());

        tweet = new Tweet(contentSource, 1234567890, "This is a #hashtag.", new Date());
        assertEquals("This is a <a href=\"/search?q=%23hashtag\">#hashtag</a>.", tweet.getBodyAsHtml());

        tweet = new Tweet(contentSource, 1234567890, "This is a ##hashtag.", new Date());
        assertEquals("This is a #<a href=\"/search?q=%23hashtag\">#hashtag</a>.", tweet.getBodyAsHtml());
    }

}
