

# bring back original trunk versions of tree-conflicted files
svn resolve --accept working trunk/ontopia/src/java/net/ontopia/topicmaps/utils/ctm/ctm.flex
svn resolve --accept working trunk/ontopia/src/java/net/ontopia/topicmaps/utils/ctm/ctm.g
svn resolve --accept working trunk/ontopia/src/java/net/ontopia/topicmaps/utils/ltm/ltm.g
svn resolve --accept working trunk/ontopia/src/java/net/ontopia/topicmaps/query/parser/tolog.g

# forcefully delete branch version of tree-conflicted files - no textual changes were applied in branch in this revision
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ctm/ctm.flex
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ctm/ctm.g
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/query/parser/tolog.g
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ltm/ltm.g

# re-apply svn:move for tree-conflicted files to include changes in trunk in revisions 1183:HEAD
svn move trunk/ontopia/src/java/net/ontopia/topicmaps/utils/ctm/ctm.flex   trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ctm/ctm.flex
svn move trunk/ontopia/src/java/net/ontopia/topicmaps/utils/ctm/ctm.g      trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ctm/ctm.g
svn move trunk/ontopia/src/java/net/ontopia/topicmaps/query/parser/tolog.g trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/query/parser/tolog.g
svn move trunk/ontopia/src/java/net/ontopia/topicmaps/utils/ltm/ltm.g      trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils/ltm/ltm.g



revertdeletemove() {
	svn revert .$1
	svn delete --force .$2
	svn move --parents .$1 .$2
}

applytestpackagepatch() {
	svn merge -c 1183 branches/ontopia-maven$2 trunk$2 # yes, $1 isn't used
}



revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/core/events/test/TopicModificationTests.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/core/events/TopicModificationTests.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/entry/test/AbstractURLTopicMapReferenceTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/entry/AbstractURLTopicMapReferenceTest.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/impl/basic/test/PackageTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/impl/basic/PackageTest.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/impl/rdbms/test/ObjectLookupTests.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/impl/rdbms/ObjectLookupTests.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/impl/rdbms/test/PrefetcherTests.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/impl/rdbms/PrefetcherTests.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/impl/rdbms/test/RDBMSBackendTests.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/impl/rdbms/RDBMSBackendTests.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/core/test/AbstractQueryTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/core/AbstractQueryTest.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/core/test/InsertTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/core/InsertTest.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/core/test/QueryProcessorTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/core/QueryProcessorTest.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/core/test/ValueLikePredicateTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/core/ValueLikePredicateTest.java
# revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/AbstractRDBMSQueryTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/AbstractRDBMSQueryTest.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/ParsedQueryTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/ParsedQueryTest.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/QueryProcessorTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/QueryProcessorTest.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/QueryResultTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/QueryResultTest.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/RDBMSTestUtils.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/RDBMSTestUtils.java
# revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/schema/impl/osl/test/SchemaTestGenerator.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/schema/impl/osl/SchemaTestGenerator.java
# revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/utils/ctm/test/CTMInvalidTestGenerator.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/utils/ctm/CTMInvalidTestGenerator.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/utils/test/QNameTests.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/utils/QNameTests.java
# revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/utils/rdf/test/RDFWriterTestGenerator.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/utils/rdf/RDFWriterTestGenerator.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/xml/test/AbstractCanonicalTests.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/xml/AbstractCanonicalTests.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/xml/test/AbstractXMLTestCase.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/xml/AbstractXMLTestCase.java
revertdeletemove	/trunk/ontopia/src/java/net/ontopia/topicmaps/xml/test/XTM2ExporterTest.java	/trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/xml/XTM2ExporterTest.java

# patches to remove .test from package names
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/core/events/test/TopicModificationTests.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/core/events/TopicModificationTests.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/entry/test/AbstractURLTopicMapReferenceTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/entry/AbstractURLTopicMapReferenceTest.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/impl/basic/test/PackageTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/impl/basic/PackageTest.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/impl/rdbms/test/ObjectLookupTests.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/impl/rdbms/ObjectLookupTests.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/impl/rdbms/test/PrefetcherTests.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/impl/rdbms/PrefetcherTests.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/impl/rdbms/test/RDBMSBackendTests.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/impl/rdbms/RDBMSBackendTests.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/core/test/AbstractQueryTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/core/AbstractQueryTest.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/core/test/InsertTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/core/InsertTest.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/core/test/QueryProcessorTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/core/QueryProcessorTest.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/core/test/ValueLikePredicateTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/core/ValueLikePredicateTest.java
## applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/AbstractRDBMSQueryTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/AbstractRDBMSQueryTest.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/ParsedQueryTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/ParsedQueryTest.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/QueryProcessorTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/QueryProcessorTest.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/QueryResultTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/QueryResultTest.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/query/impl/rdbms/test/RDBMSTestUtils.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/query/impl/rdbms/RDBMSTestUtils.java
## applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/schema/impl/osl/test/SchemaTestGenerator.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/schema/impl/osl/SchemaTestGenerator.java
## applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/utils/ctm/test/CTMInvalidTestGenerator.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/utils/ctm/CTMInvalidTestGenerator.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/utils/test/QNameTests.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/utils/QNameTests.java
## applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/utils/rdf/test/RDFWriterTestGenerator.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/utils/rdf/RDFWriterTestGenerator.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/xml/test/AbstractCanonicalTests.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/xml/AbstractCanonicalTests.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/xml/test/AbstractXMLTestCase.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/xml/AbstractXMLTestCase.java
applytestpackagepatch	/trunk/ontopia/src/java/net/ontopia/topicmaps/xml/test/XTM2ExporterTest.java	/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/xml/XTM2ExporterTest.java
