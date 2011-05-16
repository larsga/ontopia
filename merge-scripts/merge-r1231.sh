
# directory is being deleted after move

# bring back original version of tree-conflicted directory
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/core/index

# forcefully delete deleted directory
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps/core/index
