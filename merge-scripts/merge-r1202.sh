

revertdeletemove() {
	svn revert .$1
	svn delete --force .$2
	svn move --parents .$1 .$2
}

revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/db2tm/JDBCDataSource.java	/trunk/ontopia-maven/ontopia-db2tm/src/main/java/net/ontopia/topicmaps/db2tm/JDBCDataSource.java
