

revertdeletemove() {
	svn revert .$1
	svn delete --force .$2
	svn move --parents .$1 .$2
}

revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/entry/AbstractPathTopicMapSource.java	/trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/entry/AbstractPathTopicMapSource.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/schema/impl/osl/OSLSchemaReader.java	/trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/schema/impl/osl/OSLSchemaReader.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/utils/rdf/RDFTopicMapWriter.java	/trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/utils/rdf/RDFTopicMapWriter.java
