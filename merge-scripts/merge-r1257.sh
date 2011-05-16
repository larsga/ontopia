
# directories are being moved again, remove in between directories

# bring back original version of tree-conflicted directories
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/utils/tmrap
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/utils/tmrap
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/impl/remote

# forcefully delete in between tree-conflicted directories
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/java/net/ontopia/topicmaps/utils/tmrap
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/utils/tmrap
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/impl/remote
