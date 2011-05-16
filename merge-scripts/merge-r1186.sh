

revertdeletemove() {
	svn revert .$1
	svn delete --force .$2
	svn move --parents .$1 .$2
}

revertdeletemove	/trunk/ontopia/src/java/net/ontopia/infoset/impl/basic/URILocator.java	/trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/infoset/impl/basic/URILocator.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/utils/FileUtils.java	/trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/utils/FileUtils.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/utils/StreamUtils.java	/trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/utils/StreamUtils.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/utils/URIUtils.java	/trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/utils/URIUtils.java
