
# directory is being deleted after move, forcefully remove directory

# bring back original version of tree-conflicted directory
svn resolve --accept working trunk/ontopia-maven/ontopia-webed/src/main/resources

# forcefully delete tree-conflicted directory
svn delete --force trunk/ontopia-maven/ontopia-webed/src/main/resources
