
# directories are being deleted after move, forcefully remove directories

# bring back original version of tree-conflicted directories
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/utils/canonical
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/utils/various
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/schema
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/xml/tmxmlWriter
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/query/core

# forcefully delete tree-conflicted directories
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/utils/canonical
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/utils/various
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/schema
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/xml/tmxmlWriter
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/query/core
