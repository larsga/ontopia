
# directories are being deleted after move, forcefully remove directories

# bring back original version of tree-conflicted directories
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/utils

# forcefully delete tree-conflicted directories
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/topicmaps
svn delete --force trunk/ontopia-maven/ontopia-core/src/test/resources/net/ontopia/utils
