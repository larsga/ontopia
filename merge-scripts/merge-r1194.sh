
# directory is being moved again, remove in between directory

# bring back original version of tree-conflicted directory
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/nav/utils/deciders

# forcefully delete in between tree-conflicted directory
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/java/net/ontopia/topicmaps/nav/utils/deciders

# patch RDFTopicMapWriter.java
patch -p0 -i merge-r1194-patch.diff 
