
# directories are being deleted after move

# bring back original version of tree-conflicted directories
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/query/parser

# forcefully delete deleted directories
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/utils
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/resources/net/ontopia/topicmaps/query/parser
