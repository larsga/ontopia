
# directory is being deleted after edit, forcefully remove directory

# bring back original version of tree-conflicted directory
svn resolve --accept working trunk/ontopia/src/test-data/config

# forcefully delete tree-conflicted directory
svn delete --force trunk/ontopia/src/test-data/config
