

revertdeletemove() {
	svn revert .$1
	svn delete --force .$2
	svn move --parents .$1 .$2
}

applytestpackagepatch() {
	svn merge -c 1201 branches/ontopia-maven$2 trunk$2 # yes, $1 isn't used
}

revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/db2tm/test/FunctionsTestCase.java	/trunk/ontopia-maven/ontopia-db2tm/src/test/java/net/ontopia/topicmaps/db2tm/FunctionsTestCase.java

# patches to remove .test from package name
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/db2tm/test/FunctionsTestCase.java	/ontopia-maven/ontopia-db2tm/src/test/java/net/ontopia/topicmaps/db2tm/FunctionsTestCase.java
