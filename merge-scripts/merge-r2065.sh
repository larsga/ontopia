
# file is being deleted after edit, forcefully remove file

# bring back original version of tree-conflicted file
svn resolve --accept working trunk/ontopia/build.xml

# forcefully delete tree-conflicted file
svn delete --force trunk/ontopia/build.xml
