# wikimark

**wikimark** is a [wiki](https://en.wikipedia.org/wiki/Wiki) built with [Java](http://www.oracle.com/technetwork/java), 
in which all pages are written using [CommonMark](http://commonmark.org).
Search is implemented with [Apache Lucece](https://lucene.apache.org). Minimalistic style is provided by
 [Skeleton](http://getskeleton.com/). You can deploy **wikimark** in any [JEE](http://www.oracle.com/technetwork/java/javaee)
 container, although for the time being it's been officially tested only in [Apache Tomcat](http://tomcat.apache.org/).

## Building from source

Clone the repository and build with [Gradle](https://gradle.org/)

    gradle war

## Running

Deploy ```build/wikimark.war``` on your container of choice. The root directory for wiki pages is 
```${user.home}/.wikimark```.

## File format

All pages are stored in files with the following format:

    title=The title
    author=The author
    keywords=keyword 1,keyword 2
    CommonMark content

Example:

    title=Test page
    author=Martín Straus
    keywords=java,wiki,commonmark
    #Wikimark
    This is the content of a sample page

