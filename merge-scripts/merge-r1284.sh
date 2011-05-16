
# directories are being deleted after move, forcefully remove directories

# bring back original version of tree-conflicted directories
svn resolve --accept working trunk/ontopia-maven/ontopia-classify/src/main/javadoc
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/javadoc
svn resolve --accept working trunk/ontopia-maven/ontopia-db2tm/src/main/javadoc
svn resolve --accept working trunk/ontopia-maven/ontopia-navigator/src/main/javadoc
svn resolve --accept working trunk/ontopia-maven/ontopia-webed/src/main/javadoc

# forcefully delete tree-conflicted directories
svn delete --force trunk/ontopia-maven/ontopia-classify/src/main/javadoc
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/javadoc
svn delete --force trunk/ontopia-maven/ontopia-db2tm/src/main/javadoc
svn delete --force trunk/ontopia-maven/ontopia-navigator/src/main/javadoc
svn delete --force trunk/ontopia-maven/ontopia-webed/src/main/javadoc
