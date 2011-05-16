
# directories are being moved again, remove in between directories

# bring back original version of tree-conflicted directories
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/nav2
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/nav2
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/schema/impl/osl/nav
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/nav
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/utils/ontojsp
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/infoset/fulltext/topicmaps/nav
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/javadoc/net/ontopia/topicmaps/nav
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/javadoc/net/ontopia/topicmaps/nav2

# forcefully delete in between tree-conflicted directories
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/nav2
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/nav2
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/schema/impl/osl/nav
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/nav
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/utils/ontojsp
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/infoset/fulltext/topicmaps/nav
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/javadoc/net/ontopia/topicmaps/nav
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/javadoc/net/ontopia/topicmaps/nav2

revertdeletemove() {
	svn revert .$1
	svn delete --force .$2
	svn move --parents .$1 .$2
}

applytestpackagepatch() {
	svn merge -c 1183 branches/ontopia-maven$2 trunk$2 # yes, $1 isn't used # yes, revision is correct
}


revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/nav2/impl/basic/NavigatorApplication.java	/trunk/ontopia-maven/ontopia-navigator/src/main/java/net/ontopia/topicmaps/nav2/impl/basic/NavigatorApplication.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/nav2/portlets/taglib/RelatedTag.java	/trunk/ontopia-maven/ontopia-navigator/src/main/java/net/ontopia/topicmaps/nav2/portlets/taglib/RelatedTag.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/nav2/taglibs/logic/ExternalFunctionTag.java	/trunk/ontopia-maven/ontopia-navigator/src/main/java/net/ontopia/topicmaps/nav2/taglibs/logic/ExternalFunctionTag.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/utils/ontojsp/FakeServletContext.java	/trunk/ontopia-maven/ontopia-navigator/src/main/java/net/ontopia/utils/ontojsp/FakeServletContext.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/utils/ontojsp/JSPPageReader.java	/trunk/ontopia-maven/ontopia-navigator/src/main/java/net/ontopia/utils/ontojsp/JSPPageReader.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/nav2/impl/framework/test/JSPAttributeTest.java	/trunk/ontopia-maven/ontopia-navigator/src/test/java/net/ontopia/topicmaps/nav2/impl/framework/JSPAttributeTest.java

applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/nav2/impl/framework/test/JSPAttributeTest.java	/ontopia-maven/ontopia-navigator/src/test/java/net/ontopia/topicmaps/nav2/impl/framework/JSPAttributeTest.java
