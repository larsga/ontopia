
# bring back original trunk versions of tree-conflicted file
svn resolve --accept working trunk/ontopia/src/java/net/ontopia/topicmaps/db2tm/db2tm.rnc

# forcefully delete branch version of tree-conflicted file - no textual changes were applied in branch in this revision
svn delete --force trunk/ontopia-maven/ontopia-db2tm/src/main/resources/net/ontopia/topicmaps/db2tm/db2tm.rnc

# re-apply svn:move for tree-conflicted file to include changes in trunk in revisions 1200:HEAD
svn move trunk/ontopia/src/java/net/ontopia/topicmaps/db2tm/db2tm.rnc trunk/ontopia-maven/ontopia-db2tm/src/main/resources/net/ontopia/topicmaps/db2tm/db2tm.rnc
