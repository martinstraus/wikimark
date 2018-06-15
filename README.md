[![Build Status](https://travis-ci.org/martinstraus/wikimark.svg?branch=master)](https://travis-ci.org/martinstraus/wikimark)

# WikiMark

**WikiMark** is a super simple, minimalistic, no-nonsense [wiki](https://en.wikipedia.org/wiki/Wiki).

All pages are authored using [CommonMark](http://commonmark.org). Search is implemented with 
[Apache Lucece](https://lucene.apache.org). Minimalistic style is provided by [Skeleton](http://getskeleton.com/). 

## FAQ

### Another wiki? 

Yes, another wiki. 

### But why?

![But why?](http://www.reactiongifs.com/r/but-why.gif "But why?")

I needed an extremely simple way to author internal documentation for [my company](http://fit.com.ar) (procedures, 
troblueshootings, projects' info, and the like). I didn't want to use a database. I wanted to be able to back
it up super-easily, just tarring some files. I wanted to serve it in our intranet using minimal resources. I didn't want
to lock myself to any propietary format, and be able to read those documents in pure text form, in a Linux terminal if 
needed; CommonMark is the perfect format for that.

[MediaWiki](https://www.mediawiki.org/) is overkill for what I need. I once tried [Liferay](http://liferay.com)... 
never again.

Thus, __WikiMark__ was born.

## Building from source

__WikiMark__ is built with [Java](http://www.oracle.com/technetwork/java).

Clone the repository and build with [Gradle](https://gradle.org/)

    gradle war

## Running

You can deploy **wikimark** in any [JEE](http://www.oracle.com/technetwork/java/javaee) container, although for the time 
being it's been officially tested only in [Apache Tomcat](http://tomcat.apache.org/).

Deploy ```build/wikimark.war``` on your container of choice. The root directory for wiki pages is 
```${user.home}/.wikimark```.

You should configure security in the container in order to log in and create pages.

**DO NOT run the server as root.**

## File format

Each page is stored in a single page. Metadata comes first, in plain text, and then the content:

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

