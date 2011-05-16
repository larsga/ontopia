
# directory is being deleted after move

# bring back original version of tree-conflicted directory
svn resolve --accept working trunk/ontopia-maven/ontopia-core/src/main/resources/ontopia-version

# forcefully delete deleted directory
svn delete --force trunk/ontopia-maven/ontopia-core/src/main/resources/ontopia-version
