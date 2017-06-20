# wikimark

_wikimark_ is a [wiki](https://en.wikipedia.org/wiki/Wiki) built in Java, in which all pages are written using [CommonMark](http://commonmark.org)

## Building

Clone the repository and build with [Gradle](https://gradle.org/)

    gradle war

## Running

Deploy ```build\wikimark.war``` on your container of choice. The root directory for wiki pages is 
```${user.home}/.wikimark```.
